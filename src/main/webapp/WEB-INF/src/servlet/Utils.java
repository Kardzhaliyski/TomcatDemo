package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Utils {
    public static Gson gson = gson = new GsonBuilder().setPrettyPrinting().create();
    public static void writeErrorAsJson(HttpServletResponse resp, int statusCode, String msg) throws IOException {
        String json = gson.toJson(msg);
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        resp.getWriter().println(json);
    }

    public static void writeAsJson(HttpServletResponse resp, int status, Object obj) throws IOException {
        resp.setStatus(status);
        writeAsJson(resp, obj);
    }

    public static void writeAsJson(HttpServletResponse resp, Object obj) throws IOException {
        String json = gson.toJson(obj);
        resp.setContentType("application/json");
        resp.getWriter().println(json);
    }
}
