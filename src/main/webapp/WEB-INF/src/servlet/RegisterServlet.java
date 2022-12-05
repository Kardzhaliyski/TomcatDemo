package servlet;

import dao.UsersDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import utils.Security;

import java.io.BufferedReader;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    private final UsersDao dao = new UsersDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/register/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        String username = null;
        String password = null;
        String confirmPassword = null;
        String line = reader.readLine();
        for (String kvp : line.split("&")) {
            String[] split = kvp.split("=");
            String key = split[0];
            String value = split[1];
            if(key.equalsIgnoreCase("uname")) {
                username = value;
            } else if (key.equalsIgnoreCase("psw")) {
                password = value;
            } else if (key.equalsIgnoreCase("psw-repeat")) {
                confirmPassword = value;
            }
        }

        if(username == null || password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (dao.getUserByUsername(username) != null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User user = new User(username, password);
        dao.insert(user);

        resp.sendRedirect("/login/");
    }
}
