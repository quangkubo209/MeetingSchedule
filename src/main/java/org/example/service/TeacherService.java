package org.example.service;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.mappers.MeetingMapper;
import org.example.mappers.MeetingMinutesMapper;
import org.example.mappers.UserMapper;
import org.example.mappers.UserSessionsMapper;
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
    private UserSessionsMapper userSessionsMapper;
    public TeacherService(MeetingMapper meetingMapper) {
        this.meetingMapper = meetingMapper;
    }

    public JsonObject handleViewScheduleRequest(ViewScheduleRequest request) {
        JsonObject response = new JsonObject();
        Long teacherId = userSessionsMapper.getTeacherIdBySession(request.getSession());
        int page = request.getPage();
        int size = request.getSize();
        String sort = request.getSort().toUpperCase();
        int offset = (page - 1) * size;

        List<Meeting> meetings = meetingMapper.findMeetingsForTeacher(teacherId, offset, size, sort);

        // Giả sử bạn đã có logic để tính totalRows
        int totalRows = meetings.size(); // Truy vấn hoặc tính toán tổng số hàng
        int totalPage = (int) Math.ceil((double) totalRows / size);
        boolean hasNextPage = page < totalPage;
        boolean hasPreviousPage = page > 1;

        response.addProperty("code", "VIEW_SCHEDULE_OK");
        JsonArray listArray = getJsonElements(meetings);
        response.add("lists", listArray);
        JsonObject metadata = new JsonObject();
        metadata.addProperty("hasNextPage", hasNextPage);
        metadata.addProperty("hasPreviousPage", hasPreviousPage);
        metadata.addProperty("totalPage", totalPage);
        metadata.addProperty("totalRow", totalRows);
        response.add("metadata", metadata);

        return response;
    }

    private static JsonArray getJsonElements(List<Meeting> meetings) {
        JsonArray listArray = new JsonArray();
        for (Meeting meeting : meetings) {
            JsonObject meetingJson = new JsonObject();
            meetingJson.addProperty("meeting_id", meeting.getId());
            meetingJson.addProperty("start_time", meeting.getStartTime().toString());
            meetingJson.addProperty("end_time", meeting.getEndTime().toString());
            meetingJson.addProperty("slot_type", meeting.getSlotType());
            meetingJson.addProperty("remaining_slot", meeting.getSlotAvailable());
            listArray.add(meetingJson);
        }
        return listArray;
    }

    //declare slot
    public JsonObject declareSlot(DeclareSlotsRequest request) {
        JsonObject response = new JsonObject();
        Long teacherId = request.getUserId();
        MeetingInformation meetingInfo = request.getMeetingInformation();

        Meeting meeting = new Meeting();
        meeting.setTeacherId(teacherId);
        meeting.setStartTime(meetingInfo.getStartTime());
        meeting.setEndTime(meetingInfo.getEndTime());
        meeting.setSlotType(meetingInfo.getMeetingType());
        meeting.setSlotAvailable(meetingInfo.getSlotAvailable());

        meetingMapper.declareTimeSlot(meeting);

        MeetingMinutes meetingMinutes = new MeetingMinutes();
        meetingMinutes.setMeetingId(meeting.getId()); // Giả sử ID được tự động tạo sau khi lưu meeting
        meetingMinutes.setContent(meetingInfo.getContent());
        meetingMinutesMapper.insertMeetingMinutes(meetingMinutes);

        response.addProperty("status", "DECLARE_SLOTS_OK");
        response.addProperty("message", "Time slots declared successfully");

        return response;
    }

    //edit meeting
//    public EditMeetingResponse editMeeting(EditMeetingRequest request) {
//         Long userId = request.getUserId();
//        MeetingInformation meetingInfo = request.getMeetingInformation();
//
//        LocalDateTime startTime = LocalDateTime.parse(meetingInfo.getStartTime());
//        LocalDateTime endTime = LocalDateTime.parse(meetingInfo.getEndTime());
//
//        Optional<Meeting> optionalMeeting = meetingMapper.getMeetingById(request.getMeetingId());
//
//        if (optionalMeeting.isPresent()) {
//            Meeting meeting = optionalMeeting.get();
//            if (meeting.getTeacherId().equals(userId)) {
//                meeting.setStartTime(startTime);
//                meeting.setEndTime(endTime);
//                meeting.setSlotType(meetingInfo.getMeetingType());
//                meeting.setSlotAvailable(meetingInfo.getSlotAvailable());
//
//                //xử lý xung đột thời gian với cuôc gọi khác.
//
//                MeetingMinutes meetingMinutes = meeting.getMeetingMinutes();
//                if (meetingMinutes != null) {
//                    meetingMinutes.setContent(meetingInfo.getContent());
//                }
//
//                meetingMapper.insertMeeting(meeting);
//
//                EditMeetingResponse response = new EditMeetingResponse();
//                response.setStatus("EDIT_MEETING_OK");
//                response.setMessage("Meeting edited successfully");
//                return response;
//            } else {
//                // unthorized
//                return createErrorResponse("Unauthorized", "User is not authorized to edit this meeting");
//            }
//        } else {
//            // not exist
//            return createErrorResponse("Meeting Not Found", "Meeting with ID " + request.getMeetingId()+ " not found");
//        }
//    }

    public JsonObject editMeeting(EditMeetingRequest request) {
        JsonObject response = new JsonObject();
        Long teacherId = request.getUserId();
        MeetingInformation meetingInfo = request.getMeetingInformation();

        Meeting meeting = new Meeting();
        meeting.setTeacherId(teacherId);
        meeting.setStartTime(meetingInfo.getStartTime());
        meeting.setEndTime(meetingInfo.getEndTime());
        meeting.setSlotType(meetingInfo.getMeetingType());
        meeting.setSlotAvailable(meetingInfo.getSlotAvailable());

        // Cập nhật thông tin meeting
        meetingMapper.updateMeeting(meeting);

        // Cập nhật thông tin MeetingMinutes
        MeetingMinutes meetingMinutes = new MeetingMinutes();
        meetingMinutes.setMeetingId(meeting.getId());
        meetingMinutes.setContent(meetingInfo.getContent());
        meetingMinutesMapper.updateMeetingMinutes(meetingMinutes);

        response.addProperty("status", "EDIT_MEETING_OK");
        response.addProperty("message", "Meeting edited successfully");

        return response;
    }


//    //view history  of past meeting.
//    public ViewHistoryScheduleResponse viewHistorySchedule(ViewHistoryScheduleRequest request) {
//        String session = request.getSession();
//        String meetingType = request.getMeetingType();
//        int page = request.getPage();
//        int size = request.getSize();
//        String sort = request.getSort();
//
//        // query database to get history
//        Page<Meeting> meetingsPage = meetingRepository.findHistorySchedule(session, meetingType, (Pageable) PageRequest.of(page - 1, size, Sort.Direction.fromString(sort)));
//
//        ViewHistoryScheduleResponse response = new ViewHistoryScheduleResponse();
//        response.setCode("VIEW_HISTORY_SCHEDULE_OK");
//        response.setLists(mapMeetingInfoList(meetingsPage.getContent()));
//        response.setMetadata(mapMetadata(meetingsPage));
//
//        return response;
//    }
public JsonObject ViewHistorySchedu(ViewHistoryScheduleRequest request) {
        JsonObject response = new JsonObject();
    Long teacherId = userSessionsMapper.getTeacherIdBySession(request.getSession());
    String meetingType = request.getMeetingType();
    int page = request.getPage();
    int size = request.getSize();
    String sort = request.getSort().toUpperCase();
    int offset = (page - 1) * size;

    List<Meeting> pastMeetings = meetingMapper.findPastMeetings(teacherId, meetingType, offset, size, sort);

    // Giả sử bạn đã có logic để tính totalRows
    int totalRows = pastMeetings.size();
    int totalPage = (int) Math.ceil((double) totalRows / size);
    boolean hasNextPage = page < totalPage;
    boolean hasPreviousPage = page > 1;

    return buildHistoryResponse(pastMeetings, hasNextPage, hasPreviousPage, totalPage, totalRows);
}

    private JsonObject buildHistoryResponse(List<Meeting> meetings, boolean hasNextPage, boolean hasPreviousPage, int totalPage, int totalRows) {
        JsonObject response = new JsonObject();
        response.addProperty("code", "VIEW_HISTORY_SCHEDULE_OK");

        JsonArray listArray = new JsonArray();
        for (Meeting meeting : meetings) {
            JsonObject meetingJson = new JsonObject();
            meetingJson.addProperty("meeting_id", meeting.getId());
            meetingJson.addProperty("start_time", meeting.getStartTime().toString());
            meetingJson.addProperty("end_time", meeting.getEndTime().toString());
            // Add list of students and meeting minutes content
            // ...

            listArray.add(meetingJson);
        }

        response.add("lists", listArray);

        JsonObject metadata = new JsonObject();
        metadata.addProperty("hasNextPage", hasNextPage);
        metadata.addProperty("hasPreviousPage", hasPreviousPage);
        metadata.addProperty("totalPage", totalPage);
        metadata.addProperty("totalRow", totalRows);
        response.add("metadata", metadata);

        return response;
    }







}
