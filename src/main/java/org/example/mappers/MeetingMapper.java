package org.example.mappers;

import org.apache.ibatis.annotations.*;
import org.example.models.Meeting;
import org.example.models.MeetingMinutes;
import org.example.models.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MeetingMapper {

    @Select("SELECT * FROM meeting")
    List<Meeting> getAllMeetings();

    @Select("SELECT * FROM meeting WHERE id = #{id}")
    Optional<Meeting> getMeetingById(long id);

    @Insert("INSERT INTO meeting (teacher_id, start_time, end_time, slot_type, created_at, updated_at, slot_available, date) " +
            "VALUES (#{teacherId}, #{startTime}, #{endTime}, #{slotType}, #{createdAt}, #{updatedAt}, #{slotAvailable}, #{date})")
    void insertMeeting(Meeting meeting);

    @Update("UPDATE meeting " +
            "SET teacher_id = #{teacherId}, start_time = #{startTime}, end_time = #{endTime}, slot_type = #{slotType}, " +
            "updated_at = #{updatedAt}, slot_available = #{slotAvailable}, date = #{date} " +
            "WHERE id = #{id}")
    void updateMeeting(Meeting meeting);

    @Delete("DELETE FROM meeting WHERE id = #{id}")
    void deleteMeeting(long id);

    @Select("SELECT u.* FROM users u " +
            "JOIN meeting_participant mp ON u.id = mp.student_id " +
            "WHERE mp.meeting_id = #{meetingId}")
    List<User> getMeetingParticipants(long meetingId);

    @Select("SELECT * FROM meeting_minutes WHERE meeting_id = #{meetingId}")
    MeetingMinutes getMeetingMinutes(long meetingId);


}
