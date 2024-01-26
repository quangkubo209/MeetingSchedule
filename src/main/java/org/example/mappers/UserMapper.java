package org.example.mappers;


import org.apache.ibatis.annotations.*;
import org.example.models.Meeting;
import org.example.models.MeetingParticipant;
import org.example.models.User;
import org.example.models.UserSessions;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users")
    List<User> getAllUsers();

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(long id);

    @Insert("INSERT INTO users (username, password, fullname, role, created_at) " +
            "VALUES (#{username}, #{password}, #{fullname}, #{role}, #{createdAt})")
    void insertUser(User user);

    @Update("UPDATE users " +
            "SET username = #{username}, password = #{password}, fullname = #{fullname}, role = #{role} " +
            "WHERE id = #{id}")
    void updateUser(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteUser(long id);

    @Select("SELECT m.* FROM meeting m " +
            "JOIN meeting_participant mp ON m.id = mp.meeting_id " +
            "WHERE mp.student_id = #{userId}")
    List<Meeting> getUserMeetings(long userId);

    @Select("SELECT * FROM meeting_participant WHERE student_id = #{userId}")
    List<MeetingParticipant> getUserMeetingParticipants(long userId);

    @Select("SELECT * FROM user_sessions WHERE user_id = #{userId}")
    List<UserSessions> getUserSessions(long userId);

    @Select("SELECT EXISTS (SELECT 1 FROM your_user_table WHERE username = #{username})")
    boolean existsByUsername(@Param("username") String username);

    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    @Results(id = "userResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "fullname", column = "fullname"),
            @Result(property = "role", column = "role"),
            @Result(property = "createdAt", column = "created_at")
    })
    Optional<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
