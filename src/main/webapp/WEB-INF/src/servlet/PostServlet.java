package servlet;

import static javax.servlet.http.HttpServletResponse.*;
import static servlet.Utils.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.Dao;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import model.Post;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostServlet extends HttpServlet {

    Pattern GET_PATH_PATTERN = Pattern.compile("/([0-9]+)(?:(/comments)|/)?");
    Pattern DELETE_PUT_PATH_PATTERN = Pattern.compile("/([0-9]+)/?");
    Dao dao;
    Gson gson;

    @Override
    public void init() throws ServletException {
        dao = new Dao();
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String t = req.getServletPath();
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            Post[] posts = dao.getAllPosts();
            writeAsJson(resp, posts);
            return;
        }

        Matcher matcher = GET_PATH_PATTERN.matcher(pathInfo);
        if (!matcher.matches()) {
            writeErrorAsJson(resp, SC_NOT_FOUND, "");
            return;
        }

        int id = Integer.parseInt(matcher.group(1));
        if (matcher.groupCount() == 1) {
            Post post = dao.getPostById(id);
            if (post == null) {
                writeErrorAsJson(resp, SC_NOT_FOUND, "");
                return;
            }

            writeAsJson(resp, post);
            return;
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/comments?postId=" + id);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Matcher matcher = DELETE_PUT_PATH_PATTERN.matcher(pathInfo);
        if (!matcher.matches()) {
            writeErrorAsJson(resp, SC_NOT_FOUND, "");
        }

        int id = Integer.parseInt(matcher.group(1));
        dao.deletePostById(id);
        writeAsJson(resp, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (!(pathInfo == null || pathInfo.equals("/"))) {
            writeErrorAsJson(resp, SC_NOT_FOUND, "");
        }

        Post post = null;
        try {
            post = gson.fromJson(req.getReader(), Post.class);
        } catch (NumberFormatException e) {
            writeErrorAsJson(resp, SC_BAD_REQUEST, "Invalid userId!");
            return;
        }

        if (post.title == null || post.body == null || post.userId == null) {
            writeErrorAsJson(resp, SC_BAD_REQUEST, "Invalid data!");
            return;
        }

        dao.addPost(post);
        writeAsJson(resp, SC_CREATED, post);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Matcher matcher = DELETE_PUT_PATH_PATTERN.matcher(pathInfo);
        if (!matcher.matches()) {
            writeErrorAsJson(resp, SC_NOT_FOUND, "");
        }

        Post post = null;
        try {
            post = gson.fromJson(req.getReader(), Post.class);
        } catch (NumberFormatException e) {
            writeErrorAsJson(resp, SC_BAD_REQUEST, "Invalid userId!");
            return;
        }

        int id = Integer.parseInt(matcher.group(1));
        if(!dao.containsPost(id)) {
            writeErrorAsJson(resp, SC_NOT_FOUND, "");
        }

        post.id = id;
        dao.updatePost(post);
        writeAsJson(resp, post);
    }
}
