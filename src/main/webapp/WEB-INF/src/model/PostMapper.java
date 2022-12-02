package model;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface PostMapper {

    @Select("SELECT * FROM posts WHERE id = #{id}")
    public Post getPostById(int id);

    @Select("SELECT * FROM posts")
    public Post[] getAllPosts();

    @Insert("INSERT INTO posts(user_id, title, body) VALUES (#{userId}, #{title}, #{body})")
    public void addPost(Post post);
}
