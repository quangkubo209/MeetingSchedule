package org.example.mappers;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.models.User;
import org.example.models.UserSessions;

public interface UserSessionsMapper {
    void insertUserSession(UserSessions newUserSession);

    UserSessions findBySessionKey(String sessionKey);
    @Select("SELECT u.id FROM users u " +
            "JOIN user_sessions us ON u.id = us.user_id " +
            "WHERE us.session_key = #{sessionKey} AND u.role = 'teacher'")
    Long getTeacherIdBySession(@Param("sessionKey") String sessionKey);

}
