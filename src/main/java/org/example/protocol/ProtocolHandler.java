package org.example.protocol;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.request.*;
import org.example.service.StudentService;
import org.example.service.TeacherService;
import org.example.service.UserService;

import javax.swing.text.View;
import java.util.Map;

public class ProtocolHandler {
    private UserService userService;
    private StudentService studentService;

    private TeacherService teacherService;
    private Gson gson = new Gson(); // Instantiate Gson here

    public String processRequest(String request) {
        JsonObject jsonResponse = new JsonObject();
        JsonObject jsonRequest = JsonParser.parseString(request).getAsJsonObject(); // Use parseString() here
        String action = jsonRequest.get("action").getAsString();

        switch (action) {
            case "REGISTER":
                RegisterRequest registerRequest = gson.fromJson(jsonRequest, RegisterRequest.class);
                jsonResponse= userService.register(registerRequest);
                break;

            case "LOGIN":
                // Implement login logic
                LoginRequest loginRequest = gson.fromJson(jsonRequest, LoginRequest.class);
                jsonResponse = userService.login(loginRequest);
                break;

            case "LOGOUT":
                // Implement logout logic
                LogoutRequest logoutRequest = gson.fromJson(jsonRequest, LogoutRequest.class);
                jsonResponse = userService.logout(logoutRequest);
                break;

            case "VIEW_AVAILABLE_TIME_SLOTS":
                // Implement view available time slots logic
                ViewAvailableTimeSlotsRequest viewAvailableTimeSlotsRequest = gson.fromJson(jsonRequest, ViewAvailableTimeSlotsRequest.class);
                jsonResponse = studentService.viewAvailableTimeSlots(viewAvailableTimeSlotsRequest);
                break;
            case "VIEW_WEEKLY_APPOINTMENTS_OK":
                ViewWeeklyAppointmentsRequest viewWeeklyAppointmentsRequest = gson.fromJson(jsonRequest, ViewWeeklyAppointmentsRequest.class);
                jsonResponse = studentService.viewWeeklyAppointments(viewWeeklyAppointmentsRequest);
                break;
            case "BOOK_MEETING":
                // Implement view available time slots logic
                BookMeetingRequest bookMeetingRequest = gson.fromJson(jsonRequest, BookMeetingRequest.class);
                jsonResponse = studentService.bookMeeting(bookMeetingRequest);
                break;
            case "CANCEL_MEETING":
                // Implement view available time slots logic
                CancelMeetingRequest cancelMeetingRequest = gson.fromJson(jsonRequest, CancelMeetingRequest.class);
                jsonResponse = studentService.bookMeeting(cancelMeetingRequest);
                break;
            case "VIEW_SCHEDULE":
                // Implement view available time slots logic
                ViewScheduleRequest viewScheduleRequest = gson.fromJson(jsonRequest, ViewScheduleRequest.class);
                jsonResponse = studentService.bookMeeting(viewScheduleRequest);                break;
            case "DECLARE_SLOTS":
                // Implement view available time slots logic
                DeclareSlotsRequest declareSlotsRequest = gson.fromJson(jsonRequest, DeclareSlotsRequest.class);
                jsonResponse = studentService.bookMeeting(declareSlotsRequest);                break;
            case "EDIT_MEETING":
                // Implement view available time slots logic
                EditMeetingRequest editMeetingRequest  = gson.fromJson(jsonRequest, EditMeetingRequest.class);
                jsonResponse = studentService.bookMeeting(editMeetingRequest);                break;
            case "VIEW_HISTORY_SCHEDULE":
                // Implement view available time slots logic
                ViewHistoryScheduleRequest viewHistoryScheduleRequest = gson.fromJson(jsonRequest, ViewHistoryScheduleRequest.class);
                jsonResponse = studentService.bookMeeting(viewHistoryScheduleRequest);                break;

            default:
                // Unknown action
                jsonResponse.addProperty("code", "UNKNOWN_ACTION");
                jsonResponse.addProperty("error_message", "Unknown action");
                break;
        }

        return jsonResponse.toString();
    }
}
