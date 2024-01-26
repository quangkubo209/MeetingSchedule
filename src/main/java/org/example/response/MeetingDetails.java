package org.example.response;

import com.example.demotest.model.MeetingParticipant;
import com.example.demotest.model.User;
import com.example.demotest.repository.UserRepository;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MeetingDetails {
    private Long meetingId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> listStudent;
    private String meetingMinutes;

    // Getters and setters

    public String getMeetingMinutes() {
        return meetingMinutes;
    }

    public void setMeetingMinutes(String meetingMinutes) {
        this.meetingMinutes = meetingMinutes;
    }

    public void setListStudent(List<MeetingParticipant> meetingParticipants, UserRepository studentRepository) {
        this.listStudent = mapStudentList(meetingParticipants, studentRepository);
    }

    public List<String> getListStudent() {
        return listStudent;
    }

    public void setListStudent(List<String> listStudent) {
        this.listStudent = listStudent;
    }

    private List<String> mapStudentList(List<MeetingParticipant> meetingParticipants, UserRepository studentRepository) {
        return meetingParticipants.stream()
                .map(participant -> getStudentNameById(participant.getStudent_id(), studentRepository))
                .collect(Collectors.toList());
    }

    private String getStudentNameById(Long studentId, UserRepository studentRepository) {
        Optional<User> studentOptional = studentRepository.findById(studentId);
        return studentOptional.map(User::getFullname).orElse(null);
    }



    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }
}
