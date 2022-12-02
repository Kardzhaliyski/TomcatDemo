
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.Dao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Comment;
import model.Post;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

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
        String pathInfo = req.getPathInfo();
//        System.out.println(req.getServletPath());
//        System.out.println(req.getContextPath());
//        System.out.println(pathInfo);
//        System.out.println(req.getPathTranslated());

        if (pathInfo == null || pathInfo.equals("/")) {
            Post[] posts = dao.getAllPosts();
            String json = gson.toJson(posts);
            resp.getWriter().println(json);
            return;
        }

        String[] pathParts = getPathParts(pathInfo);
        if (pathParts.length == 0 || !isNumber(pathParts[0])) {
            String json = gson.toJson(null);
            resp.getWriter().println(json);
            return;
        }

        String postIdString = pathParts[0];
        int id = Integer.parseInt(postIdString);

        if (pathParts.length == 1) {
            Post post = dao.getPostById(id);
            String json = gson.toJson(post);
            resp.getWriter().println(json);
        } else if (pathParts.length == 2) {
            if(!pathParts[1].equalsIgnoreCase("comments")) {
                String json = gson.toJson(null);
                resp.getWriter().println(json);
            }

            Comment[] comments = dao.getAllCommentsForPost(id);
            String json = gson.toJson(comments);
            resp.getWriter().println(json);
        }

    }

    private boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private String[] getPathParts(String pathInfo) {
        return Arrays.stream(pathInfo.split("/")).filter(p -> !p.isEmpty()).toArray(String[]::new);
    }
}
