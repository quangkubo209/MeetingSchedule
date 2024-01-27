package org.example.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.mappers.MeetingMapper;
import org.example.mappers.UserMapper;
import org.example.models.Meeting;
import org.example.request.BookMeetingRequest;
import org.example.request.CancelMeetingRequest;
import org.example.request.ViewAvailableTimeSlotsRequest;
import org.example.request.ViewWeeklyAppointmentsRequest;
import org.example.response.*;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class StudentService {

    private UserMapper userMapper;
    private MeetingMapper meetingMapper;
    public StudentService(MeetingMapper meetingMapper) {
        this.meetingMapper = meetingMapper;
    }

    public  JsonObject ViewAvailableTimeSlots(ViewAvailableTimeSlotsRequest request) {
        JsonObject response = new JsonObject();
        String teacherName = "%" + request.getSearch() + "%"; // Cho phép tìm kiếm linh hoạt
        int page = request.getPage();
        int size = request.getSize();
        String sort = request.getSort().toUpperCase();
        int offset = (page - 1) * size;

        List<Meeting> slots = meetingMapper.findAvailableTimeSlotsByTeacher(teacherName, offset, size, sort);


        int totalRows = slots.size();
        int totalPage = (int) Math.ceil((double) totalRows / size);
        boolean hasNextPage = page < totalPage;
        boolean hasPreviousPage = page > 1;

        return buildResponse(slots, hasNextPage, hasPreviousPage, totalPage, totalRows);
    }

    private JsonObject buildResponse(List<Meeting> slots, boolean hasNextPage, boolean hasPreviousPage, int totalPage, int totalRows) {
        JsonObject response = new JsonObject();
        response.addProperty("code", "VIEW_AVAILABLE_TIME_SLOTS_OK");

        JsonArray listArray = getJsonElements(slots);
        response.add("lists", listArray);

        JsonObject metadata = new JsonObject();
        metadata.addProperty("hasNextPage", hasNextPage);
        metadata.addProperty("hasPreviousPage", hasPreviousPage);
        metadata.addProperty("totalPage", totalPage);
        metadata.addProperty("totalRow", totalRows);
        response.add("metadata", metadata);

        return response;
    }

    public JsonObject viewWeeklyAppointments(ViewWeeklyAppointmentsRequest request) {
        JsonObject response = new JsonObject();
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();
        int page = request.getPage();
        int size = request.getSize();
        String sort = request.getSort().toUpperCase();
        int offset = (page - 1) * size;

        List<Meeting> meetings = meetingMapper.findMeetingsForWeek(startTime, endTime, offset, size, sort);

        // Giả sử bạn đã có logic để tính totalRows
        int totalRows = meetings.size();
        int totalPage = (int) Math.ceil((double) totalRows / size);
        boolean hasNextPage = page < totalPage;
        boolean hasPreviousPage = page > 1;

        return buildResponseM(meetings, hasNextPage, hasPreviousPage, totalPage, totalRows);
    }

    private JsonObject buildResponseM(List<Meeting> meetings, boolean hasNextPage, boolean hasPreviousPage, int totalPage, int totalRows) {
        JsonObject response = new JsonObject();
        response.addProperty("code", "VIEW_WEEKLY_APPOINTMENTS_OK");

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


    public JsonObject bookMeeting(BookMeetingRequest request) {
        JsonObject response = new JsonObject();
        Long meetingId = request.getMeetingId();

        meetingMapper.bookMeeting(meetingId);
        response.addProperty("code", "BOOK_MEETING_CREATED");

        return response;
    }

    public JsonObject cancelMeeting(CancelMeetingRequest request) {
        JsonObject response = new JsonObject();
        Long meetingId = request.getMeetingId();

        meetingMapper.cancelMeeting(meetingId);

        response.addProperty("code", "CANCEL_MEETING_OK");


        return response;
    }

}
