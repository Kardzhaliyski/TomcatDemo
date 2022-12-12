package filter;

import static jakarta.servlet.http.HttpServletResponse.*;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.Utils;

import java.io.IOException;

public class AuthenticationFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            Utils.writeErrorAsJson(res, SC_UNAUTHORIZED, "User not logged in.");
            return;
        }

        chain.doFilter(req, res);
    }
}
