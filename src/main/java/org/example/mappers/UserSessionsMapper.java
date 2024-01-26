package org.example.mappers;

import org.apache.ibatis.annotations.*;
import org.example.models.User;
import org.example.models.UserSessions;

import java.util.List;

@Mapper
public interface UserSessionsMapper {

    @Select("SELECT * FROM user_sessions")
    List<UserSessions> getAllUserSessions();

    @Select("SELECT * FROM user_sessions WHERE id = #{id}")
    UserSessions getUserSessionsById(long id);

    @Insert("INSERT INTO user_sessions (user_id, session_key) " +
            "VALUES (#{userId}, #{sessionKey}) " +
            "ON DUPLICATE KEY UPDATE user_id = VALUES(user_id), session_key = VALUES(session_key)")
    void save(UserSessions userSessions);

    @Update("UPDATE user_sessions " +
            "SET user_id = #{userId}, session_key = #{sessionKey} " +
            "WHERE id = #{id}")
    void updateUserSessions(UserSessions userSessions);

    @Delete("DELETE FROM user_sessions WHERE id = #{id}")
    void deleteUserSessions(long id);

    @Select("SELECT * FROM users WHERE id = (SELECT user_id FROM user_sessions WHERE id = #{userSessionId})")
    User getUserSessionUser(long userSessionId);

    @Select("SELECT * FROM user_sessions WHERE session_key = #{sessionKey}")
    UserSessions findBySessionKey(@Param("sessionKey") String sessionKey);
}
