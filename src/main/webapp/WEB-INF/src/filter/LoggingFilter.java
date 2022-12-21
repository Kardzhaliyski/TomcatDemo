package filter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AuthToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.AuthenticationService;

import java.io.IOException;

public class LoggingFilter extends HttpFilter {

    Logger log;

    @Override
    public void init(FilterConfig config) throws ServletException {
        String logPath = config.getServletContext().getRealPath("WEB-INF/logs");
        System.setProperty("fileDir", logPath);
        log = LogManager.getLogger("FileLogger");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        long before = System.currentTimeMillis();
        try {
            chain.doFilter(req, res);
        } catch (Exception e) {
            log.error(e);
        }


        long after = System.currentTimeMillis();
        long timeTaken = after - before;

        String method = req.getMethod();
        String uname = "notLoggedIn";
        String authHeader = req.getHeader("Authorization");
        if(authHeader != null) {
            AuthToken authToken = AuthenticationService.getInstance().getAuthToken(authHeader);
            if(authToken != null) {
                uname = authToken.uname;
            }
        }

        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        String path = servletPath + (pathInfo == null ? "" : pathInfo);
        String msg = String.format("%s %s %s took %d ms",
                method, path, uname, timeTaken);
        log.info(msg);
    }
}
