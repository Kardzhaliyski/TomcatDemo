package dao.mapper;

import model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User getByUsername(String username);

    @Insert("INSERT INTO users(username, password) VALUES (#{username}, #{password})")
    void insert(User user);
}
