package servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import utils.Security;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/login/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if(session != null) {
            RequestDispatcher disp = req.getRequestDispatcher("/");
            disp.forward(req, resp);
            return;
        }

        BufferedReader reader = req.getReader();
        String username = null;
        String password = null;
        String line = reader.readLine();
        for (String kvp : line.split("&")) {
            String[] split = kvp.split("=");
            String key = split[0];
            String value = split[1];
            if(key.equalsIgnoreCase("uname")) {
                username = value;
            } else if (key.equalsIgnoreCase("psw")) {
                password = value;
            }
        }

        if(username == null || password == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User user = new User(username, password);
        boolean isValid = Security.verifyUser(user);
        if(!isValid) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        req.getSession(true);
        req.getRequestDispatcher("/").forward(req, resp);
    }
}
