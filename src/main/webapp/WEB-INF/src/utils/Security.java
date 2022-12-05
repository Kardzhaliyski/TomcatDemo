package utils;

import dao.UsersDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.User;

public class Security {
    private static final UsersDao dao = new UsersDao();

    public static boolean verifyUser(User user) {
        if (user.username == null || user.password == null) {
            return false;
        }

        User persistedUser = dao.getUserByUsername(user.username);
        if (persistedUser == null) {
            return false;
        }

        if (persistedUser.password.equals(user.password)) {
            return true;
        }

        return false;
    }

    public static boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return false;
        }

        return true;
    }

    public static boolean exist(String username) {
        User persistedUser = dao.getUserByUsername(username);
        if (persistedUser == null) {
            return false;
        }

        return true;
    }
}
