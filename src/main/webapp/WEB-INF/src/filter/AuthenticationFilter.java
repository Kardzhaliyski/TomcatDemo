package filter;

import static jakarta.servlet.http.HttpServletResponse.*;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.AuthenticationService;
import servlet.Utils;

import java.io.IOException;

public class AuthenticationFilter extends HttpFilter {

    private static AuthenticationService authService;

    @Override
    public void init() throws ServletException {
        authService = AuthenticationService.getInstance();
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        String authHeader = req.getHeader("Authorization");
        if(authHeader == null) {
            Utils.writeErrorAsJson(res, SC_UNAUTHORIZED, "User not logged in.");
            return;
        }

        boolean correctSchema = authHeader.startsWith("Bearer ");
        if (!correctSchema) {
            Utils.writeErrorAsJson(res, SC_UNAUTHORIZED, "User not logged in.");
            return;
        }

        String token = authHeader.substring(7);
        boolean valid = authService.isValid(token);
        if (!valid) {
            Utils.writeErrorAsJson(res, SC_UNAUTHORIZED, "User not logged in.");
            return;
        }

        chain.doFilter(req, res);
    }
}
