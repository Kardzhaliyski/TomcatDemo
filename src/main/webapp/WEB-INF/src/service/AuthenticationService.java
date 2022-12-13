package service;

import dao.TokenDao;
import model.AuthToken;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AuthenticationService {

    public static AuthenticationService instance = null;
    private final TokenDao dao;
    private final Random random;
    private Map<String, AuthToken> tokenMap = new HashMap<>();

    public static AuthenticationService getInstance() {
        if(instance == null) {
            instance = new AuthenticationService();
        }

        return instance;
    }

    private AuthenticationService() {
        this.tokenMap = new HashMap<>();
        this.dao = new TokenDao();
        this.random = new Random();
    }

    public boolean isValid(String token) {
        if (tokenMap.containsKey(token)) {
            return true;
        }

        AuthToken authToken = dao.getToken(token);
        if (authToken == null) {
            return false;
        }

        tokenMap.put(token, authToken);
        return true;
    }

    public String createNewToken(String username) {
        int salt = random.nextInt();
        String token = DigestUtils.sha1Hex(username + "$" + salt);
        AuthToken authToken = new AuthToken(username, token);
        dao.addToken(authToken);
        tokenMap.put(token, authToken);
        return token;
    }

    public AuthToken getToken(String token) {
        AuthToken authToken = tokenMap.get(token);
        if(authToken == null) {
            throw new IllegalStateException("Token hasn't been cached. Try login again.");
        }

        return authToken;
    }
}
