package servlet;

import static jakarta.servlet.http.HttpServletResponse.*;
import static servlet.Utils.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.Dao;
import dao.UsersDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.dto.LoginUserDTO;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    UsersDao dao;
    Gson gson;

    @Override
    public void init() throws ServletException {
        dao = new UsersDao();
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/login/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            writeAsJson(resp,"Already Logged in!");
            return;
        }

        LoginUserDTO dto = gson.fromJson(req.getReader(), LoginUserDTO.class);

        if (dto.uname == null || dto.psw == null) {
            writeErrorAsJson(resp, SC_BAD_REQUEST, "Invalid credentials");
            return;
        }

        User user = dao.getUserByUsername(dto.uname);
        String salt = user.salt;
        String hashedPassword = DigestUtils.sha1Hex(salt + dto.psw);
        if(user.password.equals(hashedPassword)) {
            req.getSession(true);
            writeAsJson(resp, "User logged in");
        } else {
            writeErrorAsJson(resp, SC_UNAUTHORIZED, "Bad credentials");
        }
    }
}
