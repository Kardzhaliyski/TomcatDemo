package dao.mapper;

import model.AuthToken;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface AuthTokenMapper {
    @Select("SELECT * FROM tokens WHERE token = #{token}")
    public AuthToken getBy(String token);

    @Insert("INSERT INTO tokens(token, username, created_date, expiration_date) VALUES (#{token}, #{uname}, #{createdDate}, #{expirationDate})")
    public void addToken(AuthToken token);
}
