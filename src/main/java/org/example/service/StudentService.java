package org.example.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.mappers.MeetingMapper;
import org.example.mappers.UserMapper;
import org.example.models.Meeting;
import org.example.request.BookMeetingRequest;
import org.example.request.CancelMeetingRequest;
import org.example.request.ViewAvailableTimeSlotsRequest;
import org.example.response.BookMeetingResponse;
import org.example.response.CancelMeetingResponse;
import org.example.response.Metadata;
import org.example.response.ViewAvailableTimeSlotsResponse;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class StudentService {

    private UserMapper userMapper;
    private MeetingMapper meetingMapper;

    public ViewAvailableTimeSlotsResponse viewAvailableTimeSlots(ViewAvailableTimeSlotsRequest request) {
        int page = (request.getPage()) - 1; // Giảm đi 1 vì PageRequest bắt đầu từ 0
        int size = (request.getSize());

        Page<Meeting> meetingPage = meetingRepository.findAvailableTimeSlots(
                request.getSearch(), (Pageable) PageRequest.of(page, size));

        List<TimeSlot> timeSlots = meetingPage.getContent().stream()
                .map(this::mapToTimeSlot)
                .collect(Collectors.toList());

        ViewAvailableTimeSlotsResponse response = new ViewAvailableTimeSlotsResponse();
        response.setCode("VIEW_AVAILABLE_TIME_SLOTS_OK");
        response.setLists(timeSlots);
        response.setMetadata(createMetadata(meetingPage));

        return response;
    }

    public ViewWeeklyAppointmentsResponse viewWeeklyAppointments(ViewWeeklyAppointmentsRequest request) {
        int page = (request.getPage()) - 1; // Giảm đi 1 vì PageRequest bắt đầu từ 0
        int size = (request.getSize());

        Page<Meeting> meetingPage = meetingRepository.findWeeklyAppointments(
                request.getStartTime(), request.getEndTime(), (Pageable) PageRequest.of(page, size));

        List<TimeSlot> timeSlots = meetingPage.getContent().stream()
                .map(this::mapToTimeSlot)
                .collect(Collectors.toList());

        ViewWeeklyAppointmentsResponse response = new ViewWeeklyAppointmentsResponse();
        response.setCode("VIEW_WEEKLY_APPOINTMENTS_OK");
        response.setLists(timeSlots);
        response.setMetadata(createMetadata(meetingPage));

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

    public JsonObject bookMeeting(BookMeetingRequest request) {
        // (logic xử lý đặt cuộc hẹn)

        BookMeetingResponse response = new BookMeetingResponse();
        response.setCode("BOOK_MEETING_CREATED");

        // Create a Gson instance
        Gson gson = new Gson();

        // Convert BookMeetingResponse to JsonObject

        return gson.toJsonTree(response).getAsJsonObject();
    }

    public JsonObject cancelMeeting(CancelMeetingRequest request) {
        // (Thêm logic xử lý hủy cuộc hẹn ở đây)

        CancelMeetingResponse response = new CancelMeetingResponse();
        response.setCode("CANCEL_MEETING_OK");
        Gson gson = new Gson();

        return gson.toJsonTree(response).getAsJsonObject();
    }

}
