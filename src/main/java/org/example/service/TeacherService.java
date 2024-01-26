package org.example.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.mappers.MeetingMapper;
import org.example.mappers.MeetingMinutesMapper;
import org.example.mappers.UserMapper;
import org.example.models.Meeting;
import org.example.models.MeetingMinutes;
import org.example.models.MeetingParticipant;
import org.example.request.*;
import org.example.response.*;


import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class TeacherService {

    private MeetingMapper meetingMapper;
    private MeetingMinutesMapper meetingMinutesMapper;

    private UserMapper userMapper;
    public ViewScheduleResponse viewSchedule(ViewScheduleRequest request) {
        int page = request.getPage() - 1;
        int size =  request.getSize();

        Page<Meeting> meetingPage = meetingRepository.findSchedule(
                request.getSession(), (Pageable) PageRequest.of(page, size));

        List<TimeSlot> timeSlots = meetingPage.getContent().stream()
                .map(this::mapToTimeSlot)
                .collect(Collectors.toList());

        ViewScheduleResponse response = new ViewScheduleResponse();
        response.setCode("VIEW_SCHEDULE_OK");
        response.setLists(timeSlots);
        response.setMetadata(createMetadata(meetingPage));

        return response;
    }

    //declare slot
    public JsonObject declareSlots(DeclareSlotsRequest request) {
        Long userId = request.getUserId(); //teacher id
        MeetingInformation meetingInfo = request.getMeetingInformation();

        LocalDateTime startTime = LocalDateTime.parse(meetingInfo.getStartTime());
        LocalDateTime endTime = LocalDateTime.parse(meetingInfo.getEndTime());

        Meeting meeting = new Meeting();
        meeting.setTeacherId(userId);
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        meeting.setSlotType(meetingInfo.getMeetingType());
        meeting.setSlotAvailable(meetingInfo.getSlotAvailable());

        MeetingMinutes meetingMinutes = new MeetingMinutes();
        meetingMinutes.setContent(meetingInfo.getContent());
        meeting.setMeetingMinutes(meetingMinutes);

        meetingMapper.insertMeeting(meeting);


        DeclareSlotsResponse response = new DeclareSlotsResponse();
        response.setStatus("DECLARE_SLOTS_OK");
        response.setMessage("Time slots declared successfully");

        Gson gson = new Gson();

        return gson.toJsonTree(response).getAsJsonObject();
    }

    //edit meeting
    public EditMeetingResponse editMeeting(EditMeetingRequest request) {
         Long userId = request.getUserId();
        MeetingInformation meetingInfo = request.getMeetingInformation();

        LocalDateTime startTime = LocalDateTime.parse(meetingInfo.getStartTime());
        LocalDateTime endTime = LocalDateTime.parse(meetingInfo.getEndTime());

        Optional<Meeting> optionalMeeting = meetingMapper.getMeetingById(request.getMeetingId());

        if (optionalMeeting.isPresent()) {
            Meeting meeting = optionalMeeting.get();
            if (meeting.getTeacherId().equals(userId)) {
                meeting.setStartTime(startTime);
                meeting.setEndTime(endTime);
                meeting.setSlotType(meetingInfo.getMeetingType());
                meeting.setSlotAvailable(meetingInfo.getSlotAvailable());

                //xử lý xung đột thời gian với cuôc gọi khác.

                MeetingMinutes meetingMinutes = meeting.getMeetingMinutes();
                if (meetingMinutes != null) {
                    meetingMinutes.setContent(meetingInfo.getContent());
                }

                meetingMapper.insertMeeting(meeting);

                EditMeetingResponse response = new EditMeetingResponse();
                response.setStatus("EDIT_MEETING_OK");
                response.setMessage("Meeting edited successfully");
                return response;
            } else {
                // unthorized
                return createErrorResponse("Unauthorized", "User is not authorized to edit this meeting");
            }
        } else {
            // not exist
            return createErrorResponse("Meeting Not Found", "Meeting with ID " + request.getMeetingId()+ " not found");
        }
    }


    //view history  of past meeting.
    public ViewHistoryScheduleResponse viewHistorySchedule(ViewHistoryScheduleRequest request) {
        String session = request.getSession();
        String meetingType = request.getMeetingType();
        int page = request.getPage();
        int size = request.getSize();
        String sort = request.getSort();

        // query database to get history
        Page<Meeting> meetingsPage = meetingRepository.findHistorySchedule(session, meetingType, (Pageable) PageRequest.of(page - 1, size, Sort.Direction.fromString(sort)));

        ViewHistoryScheduleResponse response = new ViewHistoryScheduleResponse();
        response.setCode("VIEW_HISTORY_SCHEDULE_OK");
        response.setLists(mapMeetingInfoList(meetingsPage.getContent()));
        response.setMetadata(mapMetadata(meetingsPage));

        return response;
    }

    private List<MeetingDetails> mapMeetingInfoList(List<Meeting> meetings) {
        return meetings.stream()
                .map(this::mapMeetingInfo)
                .collect(Collectors.toList());
    }

    private MeetingDetails mapMeetingInfo(Meeting meeting) {
        MeetingDetails meetingInfo = new MeetingDetails();
        meetingInfo.setMeetingId(meeting.getId());
        meetingInfo.setStartTime(meeting.getStartTime());
        meetingInfo.setEndTime(meeting.getEndTime());
        meetingInfo.setListStudent(mapStudentList(meeting.getMeetingParticipants(), userRepository));
        meetingInfo.setMeetingMinutes(meeting.getMeetingMinutes() != null ? meeting.getMeetingMinutes().getContent() : null);
        return meetingInfo;
    }

    //Lấy ra danh sách tên học sinh từ danh sách students
    private List<String> mapStudentList(List<MeetingParticipant> meetingParticipants, UserRepository studentRepository) {
        return meetingParticipants.stream()
                .map(participant -> getStudentNameById(participant.getStudent_id(), studentRepository))
                .collect(Collectors.toList());
    }

    private String getStudentNameById(Long studentId, UserRepository studentRepository) {
        Optional<User> studentOptional = studentRepository.findById(studentId);
        return studentOptional.map(User::getFullname).orElse(null);
    }

    private Metadata mapMetadata(Page<Meeting> meetingsPage) {
        Metadata metadata = new Metadata();
        metadata.setHasNextPage(meetingsPage.hasNext());
        metadata.setHasPreviousPage(meetingsPage.hasPrevious());
        metadata.setTotalPage(meetingsPage.getTotalPages());
        metadata.setTotalRow((int) meetingsPage.getTotalElements());
        return metadata;
    }


///create error response of edit meeting
    private EditMeetingResponse createErrorResponse(String status, String message) {
        EditMeetingResponse response = new EditMeetingResponse();
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }

    private TimeSlot mapToTimeSlot(Meeting meeting) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setMeetingId(meeting.getId());
        timeSlot.setStartTime(meeting.getStartTime());
        timeSlot.setEndTime(meeting.getEndTime());
        timeSlot.setSlotType(meeting.getSlotType());
        timeSlot.setRemainingSlot(meeting.getSlotAvailable());
        return timeSlot;
    }

    private Metadata createMetadata(Page<Meeting> meetingPage) {
        Metadata metadata = new Metadata();
        metadata.setHasNextPage(meetingPage.hasNext());
        metadata.setHasPreviousPage(meetingPage.hasPrevious());
        metadata.setTotalPage(meetingPage.getTotalPages());
        metadata.setTotalRow((int) meetingPage.getTotalElements());
        return metadata;
    }



}
