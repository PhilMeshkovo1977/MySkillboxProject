package main.mapper;


import java.util.ArrayList;
import java.util.List;
import main.api.response.PostByIdApi;
import main.api.response.ResponsePostApi;
import main.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

  @Autowired
  UserMapper userMapper;

  public ResponsePostApi postToResponsePostApi(Post post) {
    ResponsePostApi responsePostApi = new ResponsePostApi();
    responsePostApi.setId(post.getId());
    responsePostApi.setTime(post.getTime());
    responsePostApi.setUser(userMapper.userToUserApi(post.getUser()));
    responsePostApi.setTitle(post.getTitle());
    responsePostApi.setAnnounce(post.getText());
    responsePostApi.setViewCount(post.getView_count());
    return responsePostApi;
  }

  public List<ResponsePostApi> postToResponsePostApi(List<Post> posts) {
    List<ResponsePostApi> postApiList = new ArrayList<>();
    for (Post post : posts) {
      postApiList.add(postToResponsePostApi(post));
    }
    return postApiList;
  }

  public PostByIdApi postToPostById(Post post) {
    PostByIdApi postByIdApi = new PostByIdApi();
    postByIdApi.setId(post.getId());
    postByIdApi.setTime(post.getTime());
    postByIdApi.setUser(userMapper.userToUserApi(post.getUser()));
    postByIdApi.setTitle(post.getTitle());
    postByIdApi.setAnnounce(post.getText());
    postByIdApi.setViewCount(post.getView_count());

    return postByIdApi;
  }

}
