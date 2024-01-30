package  org.example.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.models.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingMapper {
    List<Meeting> findAvailableTimeSlotsByTeacher(
            @Param("teacherName") String teacherName,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sort") String sort
    );

    List<Meeting> findMeetingsForWeek(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sort") String sort
    );

    void bookMeeting(Long meetingId);

    List<Meeting> findMeetingsForTeacher(
            @Param("teacherId") Long teacherId,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sort") String sort
    );

    @Insert("INSERT INTO meetings (teacher_id, start_time, end_time, slot_type, slot_available, content) " +
            "VALUES (#{meeting.teacherId}, #{meeting.startTime}, #{meeting.endTime}, #{meeting.meetingType}, #{meeting.slotAvailable}, #{meeting.content})")
    void declareTimeSlot(@Param("meeting") Meeting meeting);

    @Update("UPDATE meetings SET start_time = #{meeting.startTime}, end_time = #{meeting.endTime}, " +
            "slot_type = #{meeting.slotType}, slot_available = #{meeting.slotAvailable} " +
            "WHERE id = #{meeting.id} AND teacher_id = #{meeting.teacherId}")
    void updateMeeting(@Param("meeting") Meeting meeting);

    List<Meeting> findPastMeetings(
            @Param("teacherId") Long teacherId,
            @Param("meetingType") String meetingType,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sort") String sort
    );
    @Select("SELECT fullname FROM users u JOIN meeting_participant mp ON u.id = mp.student_id WHERE mp.meeting_id = #{meetingId}")
    List<String> findParticipantsByMeetingId(@Param("meetingId") Long meetingId);

    @Select("SELECT content FROM meeting_minutes WHERE meeting_id = #{meetingId}")
    String findMeetingMinutesByMeetingId(@Param("meetingId") Long meetingId);

    boolean areSlotsAvailable(Long meetingId);

    void addMeetingParticipant(Long meetingId, Long studentId);
    void increaseMeetingSlot(Long meetingId);
    void removeMeetingParticipant(Long meetingId, Long studentId);



}
