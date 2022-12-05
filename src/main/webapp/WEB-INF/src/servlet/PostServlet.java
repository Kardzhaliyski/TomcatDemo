package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.Dao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Comment;
import model.Post;
import utils.Security;

import java.io.IOException;
import java.util.Arrays;

public class PostServlet extends HttpServlet {

    Dao dao;
    Gson gson;

    @Override
    public void init() throws ServletException {
        dao = new Dao();
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Security.isLoggedIn(req)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            Post[] posts = dao.getAllPosts();
            writeAsJson(resp, posts);
            return;
        }

        String[] pathParts = getPathParts(pathInfo);
        if (pathParts.length == 0 || !isPositiveNumber(pathParts[0])) {
            writeAsJson(resp, null);
            return;
        }

        String postIdString = pathParts[0];
        int id = Integer.parseInt(postIdString);

        if (pathParts.length == 1) {
            Post post = dao.getPostById(id);
            writeAsJson(resp, post);
        } else if (pathParts.length == 2) {
            if (!pathParts[1].equalsIgnoreCase("comments")) {
                writeAsJson(resp, null);
            }

            RequestDispatcher dispatcher = req.getRequestDispatcher("/comments?postId=" + id);
            dispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Security.isLoggedIn(req)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            writeAsJson(resp, null);
            return;
        }

        String[] pathParts = getPathParts(pathInfo);
        if (pathParts.length == 0 || !isPositiveNumber(pathParts[0])) {
            writeAsJson(resp, null);
            return;
        }

        if (pathParts.length != 1) {
            writeAsJson(resp, null);
        }

        int id = Integer.parseInt(pathParts[0]);
        int i = dao.deletePostById(id);
        writeAsJson(resp, i);
    }

    private void writeAsJson(HttpServletResponse resp, Object obj) throws IOException {
        String json = gson.toJson(obj);
        resp.getWriter().println(json);
    }

    private boolean isPositiveNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private String[] getPathParts(String pathInfo) {
        return Arrays.stream(pathInfo.split("/"))
                .filter(p -> !p.isEmpty())
                .toArray(String[]::new); // "" posts 1 ""
    }
}
