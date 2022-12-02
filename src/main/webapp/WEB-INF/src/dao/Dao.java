package dao;

import model.Comment;
import model.CommentMapper;
import model.Post;
import model.PostMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Dao {
    public static void main(String[] args) {
        Dao dao = new Dao();
        Comment c1 = new Comment(1, "id labore ex et quam laborum",
                "Eliseo@gardner.biz",
                "laudantium enim quasi est quidem magnam voluptate ipsam eos\\ntempora quo necessitatibus\\ndolor quam autem quasi\\nreiciendis et nam sapiente accusantium");


        dao.addComment(c1);
    }

    private SqlSessionFactory sessionFactory;

    public Dao() {
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("M:\\Archive\\Git\\TomcatDemo\\MavenTomcatDemo\\src\\main\\webapp\\WEB-INF\\src\\application.properties"));
        InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
        sessionFactory = new SqlSessionFactoryBuilder().build(in, properties);
//        SqlSession sqlSession = sqlSessionFactory.openSession();
    }

    public Post getPostById(int id) {
        try (SqlSession sqlSession = sessionFactory.openSession()) {
            PostMapper mapper = sqlSession.getMapper(PostMapper.class);
            Post post = mapper.getPostById(id);
            sqlSession.commit();
            return post;
        }
    }

    public void addPost(Post post) {
        try (SqlSession sqlSession = sessionFactory.openSession()) {
            PostMapper mapper = sqlSession.getMapper(PostMapper.class);
            mapper.addPost(post);
            sqlSession.commit();
        }
    }

    public Post[] getAllPosts() {
        try (SqlSession sqlSession = sessionFactory.openSession()) {
            PostMapper mapper = sqlSession.getMapper(PostMapper.class);
            Post[] posts = mapper.getAllPosts();
            sqlSession.commit();
            return posts;
        }
    }

    public Comment[] getAllCommentsForPost(int id) {
        try (SqlSession sqlSession = sessionFactory.openSession()) {
            CommentMapper mapper = sqlSession.getMapper(CommentMapper.class);
            Comment[] comments = mapper.getAllCommentsForPost(id);
            sqlSession.commit();
            return comments;
        }
    }

    public void addComment(Comment comment) {
        try (SqlSession sqlSession = sessionFactory.openSession()) {
            CommentMapper mapper = sqlSession.getMapper(CommentMapper.class);
            mapper.addComment(comment);
            sqlSession.commit();
        }
    }
}
