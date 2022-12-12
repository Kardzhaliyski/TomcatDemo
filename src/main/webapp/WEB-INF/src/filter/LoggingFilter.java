package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LoggingFilter extends HttpFilter {

    private static Logger log;

    @Override
    public void init() throws ServletException {
        log = LogManager.getLogger(LoggingFilter.class);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("Start Logging");
        long before = System.currentTimeMillis();
        chain.doFilter(req, res);

        long after = System.currentTimeMillis();
        long timeTaken = after - before;

        String method = req.getMethod();
        String path = req.getPathInfo();
        String uname = "notLoggedIn";
        HttpSession session = req.getSession(false);
        if(session != null) {
            uname = (String) session.getAttribute("uname");
        }

        log.info(String.format("%s %s %s took %sms"),
                method, path, uname, timeTaken);

        int resStatus = res.getStatus();
        if(resStatus >= 300) {
            log.warn(String.format("[%s] - %s %s %s took %sms"),
                    resStatus, method, path, uname, timeTaken);
        }

        System.out.println("Finished  Logging");
    }
}
