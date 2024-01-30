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


            List<String> participants = meetingMapper.findParticipantsByMeetingId(meeting.getId());
            JsonArray participantArray = new JsonArray();
            for (String studentName : participants) {
                JsonObject studentJson = new JsonObject();
                studentJson.addProperty("student_name", studentName);
                participantArray.add(studentJson);
            }
            meetingJson.add("list_student", participantArray);

            // Thêm nội dung meeting minutes
            String meetingContent = meetingMapper.findMeetingMinutesByMeetingId(meeting.getId());
            meetingJson.addProperty("meeting_minutes", meetingContent);

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
