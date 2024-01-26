package org.example.mappers;

import org.apache.ibatis.annotations.*;
import org.example.models.Meeting;
import org.example.models.MeetingParticipant;
import org.example.models.User;

import java.util.List;

@Mapper
public interface MeetingParticipantMapper {

    @Select("SELECT * FROM meeting_participant")
    List<MeetingParticipant> getAllMeetingParticipants();

    @Select("SELECT * FROM meeting_participant WHERE id = #{id}")
    MeetingParticipant getMeetingParticipantById(long id);

    @Insert("INSERT INTO meeting_participant (meeting_id, student_id) " +
            "VALUES (#{meetingId}, #{studentId})")
    void insertMeetingParticipant(MeetingParticipant meetingParticipant);

    @Update("UPDATE meeting_participant " +
            "SET meeting_id = #{meetingId}, student_id = #{studentId} " +
            "WHERE id = #{id}")
    void updateMeetingParticipant(MeetingParticipant meetingParticipant);

    @Delete("DELETE FROM meeting_participant WHERE id = #{id}")
    void deleteMeetingParticipant(long id);

    @Select("SELECT * FROM users WHERE id = (SELECT student_id FROM meeting_participant WHERE id = #{participantId})")
    User getParticipantUser(long participantId);

    @Select("SELECT * FROM meeting WHERE id = (SELECT meeting_id FROM meeting_participant WHERE id = #{participantId})")
    Meeting getParticipantMeeting(long participantId);
}
