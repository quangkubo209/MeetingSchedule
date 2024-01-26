package org.example.mappers;

import org.apache.ibatis.annotations.*;
import org.example.models.Meeting;
import org.example.models.MeetingMinutes;

import java.util.List;

@Mapper
public interface MeetingMinutesMapper {

    @Select("SELECT * FROM meeting_minutes")
    List<MeetingMinutes> getAllMeetingMinutes();

    @Select("SELECT * FROM meeting_minutes WHERE id = #{id}")
    MeetingMinutes getMeetingMinutesById(long id);

    @Insert("INSERT INTO meeting_minutes (meeting_id, content, created_at, updated_at) " +
            "VALUES (#{meetingId}, #{content}, #{createdAt}, #{updatedAt})")
    void insertMeetingMinutes(MeetingMinutes meetingMinutes);

    @Update("UPDATE meeting_minutes " +
            "SET meeting_id = #{meetingId}, content = #{content}, " +
            "updated_at = #{updatedAt} " +
            "WHERE id = #{id}")
    void updateMeetingMinutes(MeetingMinutes meetingMinutes);

    @Delete("DELETE FROM meeting_minutes WHERE id = #{id}")
    void deleteMeetingMinutes(long id);

    @Select("SELECT * FROM meeting WHERE id = (SELECT meeting_id FROM meeting_minutes WHERE id = #{minutesId})")
    Meeting getMinutesMeeting(long minutesId);
}
