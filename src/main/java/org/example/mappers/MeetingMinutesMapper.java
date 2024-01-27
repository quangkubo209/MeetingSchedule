package org.example.mappers;

import org.apache.ibatis.annotations.*;
import org.example.models.Meeting;
import org.example.models.MeetingMinutes;

import java.util.List;

@Mapper
public interface MeetingMinutesMapper {

    @Insert("INSERT INTO meeting_minutes (meeting_id, content) VALUES (#{meetingId}, #{content})")
    void insertMeetingMinutes(MeetingMinutes meetingMinutes);


    @Update("UPDATE meeting_minutes SET content = #{meetingMinutes.content} " +
            "WHERE meeting_id = #{meetingMinutes.meetingId}")
    void updateMeetingMinutes(@Param("meetingMinutes") MeetingMinutes meetingMinutes);
}
