package dao;

import dao.mapper.AuthTokenMapper;
import model.AuthToken;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;

public class TokenDao {

    private SqlSessionFactory sessionFactory;

    public TokenDao() {
        try {
            sessionFactory = SessionFactoryInstance.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthToken getToken(String token) {
        try (SqlSession session = sessionFactory.openSession(true)){
            AuthTokenMapper mapper = session.getMapper(AuthTokenMapper.class);
            return mapper.getBy(token);
        }
    }

    public void addToken(AuthToken token) {
        try (SqlSession session = sessionFactory.openSession(true)){
            AuthTokenMapper mapper = session.getMapper(AuthTokenMapper.class);
            mapper.addToken(token);
        }
    }
}
