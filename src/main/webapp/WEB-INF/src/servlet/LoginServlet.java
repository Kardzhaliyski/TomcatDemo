package servlet;

import static javax.servlet.http.HttpServletResponse.*;
import static servlet.Utils.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.Dao;
import dao.UsersDao;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import model.dto.LoginUserDTO;
import org.apache.commons.codec.digest.DigestUtils;
import service.AuthenticationService;

import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    UsersDao dao;
    Gson gson;
    AuthenticationService authService;

    @Override
    public void init() throws ServletException {
        dao = new UsersDao();
        gson = new GsonBuilder().setPrettyPrinting().create();
        authService = AuthenticationService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/login/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginUserDTO dto = gson.fromJson(req.getReader(), LoginUserDTO.class);

        if (dto == null || dto.uname == null || dto.psw == null) {
            writeErrorAsJson(resp, SC_BAD_REQUEST, "Invalid credentials");
            return;
        }

        User user = dao.getUserByUsername(dto.uname);
        String salt = user.salt;
        String hashedPassword = DigestUtils.sha1Hex(salt + dto.psw);
        if(user.password.equals(hashedPassword)) {
            String token = authService.createNewToken(dto.uname);

            resp.addHeader("Authorization", "Bearer " + token);
            writeAsJson(resp, "User logged in");
        } else {
            writeErrorAsJson(resp, SC_UNAUTHORIZED, "Bad credentials");
        }
    }
}
