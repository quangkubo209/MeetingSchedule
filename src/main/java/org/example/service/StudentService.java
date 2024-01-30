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

    private  final UserMapper userMapper;
    private final MeetingMapper meetingMapper;
    public StudentService(MeetingMapper meetingMapper, UserMapper userMapper) {
        this.meetingMapper = meetingMapper;
        this.userMapper = userMapper;
    }

    public JsonObject ViewAvailableTimeSlots(ViewAvailableTimeSlotsRequest request) {
        try {
            String teacherName = "%" + request.getSearch() + "%";
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
        } catch (Exception e) {
            // Handle the database error and return an error response
            return buildErrorResponse("VIEW_AVAILABLE_TIME_SLOTS_ERROR", "Error fetching available time slots from the database.");
        }
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
        try {
            LocalDateTime startTime = request.getStartTime();
            LocalDateTime endTime = request.getEndTime();
            int page = request.getPage();
            int size = request.getSize();
            String sort = request.getSort().toUpperCase();
            int offset = (page - 1) * size;
            // Existing code for fetching weekly appointments from the database
            List<Meeting> meetings = meetingMapper.findMeetingsForWeek(startTime, endTime, offset, size, sort);

            int totalRows = meetings.size();
            int totalPage = (int) Math.ceil((double) totalRows / size);
            boolean hasNextPage = page < totalPage;
            boolean hasPreviousPage = page > 1;

            return buildResponseM(meetings, hasNextPage, hasPreviousPage, totalPage, totalRows);
        } catch (Exception e) {
            // Handle the database error and return an error response
            return buildErrorResponse("VIEW_WEEKLY_APPOINTMENTS_ERROR", "Error fetching weekly appointments from the database.");
        }
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
        Long studentId = request.getStudentId();
        if (meetingId == null || studentId == null) {
            return buildErrorResponse("INVALID_REQUEST", "Meeting ID or Student ID is null.");
        }

        try {
            // Check if there are available slots
            if (!meetingMapper.areSlotsAvailable(meetingId)) {
                return buildErrorResponse("NO_SLOTS_AVAILABLE", "No slots available for this meeting.");
            }

            // Book the meeting
            meetingMapper.bookMeeting(meetingId);

            // Add student as a participant
            meetingMapper.addMeetingParticipant(meetingId, studentId);


            response.addProperty("code", "BOOK_MEETING_SUCCESS");
            response.addProperty("message", "Meeting successfully booked.");
        } catch (Exception e) {
            // Rollback the transaction here if started earlier

            return buildErrorResponse("BOOK_MEETING_ERROR", "Error booking the meeting: " + e.getMessage());
        }
        return response;
    }




    public JsonObject cancelMeeting(CancelMeetingRequest request) {
        JsonObject response = new JsonObject();
        Long studentId = request.getStudentId();
        Long meetingId = request.getMeetingId();
        if (meetingId == null || studentId == null) {
            return buildErrorResponse("INVALID_REQUEST", "Meeting ID or Student ID is null.");
        }

        try {
            // Remove student as a participant
            meetingMapper.removeMeetingParticipant(meetingId, studentId);

            // Increase the meeting slot
            meetingMapper.increaseMeetingSlot(meetingId);

            response.addProperty("code", "CANCEL_MEETING_SUCCESS");
            response.addProperty("message", "Meeting successfully canceled.");
        } catch (Exception e) {
            // Rollback the transaction here if started earlier

            return buildErrorResponse("CANCEL_MEETING_ERROR", "Error canceling the meeting: " + e.getMessage());
        }
        return response;
    }
    private JsonObject buildErrorResponse(String errorCode, String errorMessage) {
        JsonObject response = new JsonObject();
        response.addProperty("code", errorCode);
        response.addProperty("error", errorMessage);
        return response;
    }

}
