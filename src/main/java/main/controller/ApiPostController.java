package main.controller;

import com.fasterxml.jackson.databind.JsonNode;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import main.api.response.PostByIdApi;
import main.api.response.PostListApi;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

  @Autowired
  PostService postService;

  @Autowired
  private HttpServletRequest request;

  @GetMapping
  public PostListApi getAllPosts(
      @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
      @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
      @RequestParam(value = "mode", defaultValue = "recent", required = false) String mode) {
    return postService.getAllPosts(PageRequest.of(offset, limit), mode);
  }

  @GetMapping("/search")
  public ResponseEntity<?> getAllPostsByTagAndTitle(
      @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
      @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
      @RequestParam(value = "query", defaultValue = "tagName", required = false) String query) {
    try {
      PostListApi postListApi = postService.getAllPostsByTagAndTitle(offset, limit, query);
      return new ResponseEntity<>(postListApi, HttpStatus.OK);
    } catch (EntityNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> getPostById(@PathVariable int id) {
    try {
      PostByIdApi postByIdApi = postService.findPostById(id);
      return new ResponseEntity<>(postByIdApi, HttpStatus.OK);
    } catch (EntityNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

  }

  @GetMapping("/byDate")
  public ResponseEntity<?> getAllPostsByDate(
      @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
      @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
      @RequestParam(value = "date", defaultValue = "2007-01-01", required = false) String date) {
    try {
      PostListApi postListApi = postService.getAllPostsByDate(offset, limit, date);
      return new ResponseEntity<>(postListApi, HttpStatus.OK);
    } catch (EntityNotFoundException e){
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

  }

  @GetMapping("/byTag")
  public ResponseEntity<?> getAllPostsByTag(
      @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
      @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
      @RequestParam(value = "tag", defaultValue = "tagName", required = false) String tag) {
    try {
      PostListApi postListApi = postService.getAllPostsByTag(offset, limit, tag);
      return new ResponseEntity<>(postListApi, HttpStatus.OK);
    } catch (EntityNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/my")
  public PostListApi getAllMyPosts(
      @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
      @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
      @RequestParam(value = "status", defaultValue = "inactive", required = false) String status)
      throws Exception {
    return postService.getAllMyPosts(PageRequest.of(offset, limit), status);
  }

  @GetMapping("/moderation")
  public PostListApi getAllPostsToModeration(
      @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
      @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
      @RequestParam(value = "status", defaultValue = "new", required = false) String status) {
    return postService.getAllPostsToModeration(PageRequest.of(offset, limit), status);
  }

  @PostMapping("/addPost")
  public JsonNode addPost(
      @RequestParam(value = "time", required = false) String time,
      @RequestParam(value = "active", required = false) Integer active,
      @RequestParam(value = "title", required = false) String title,
      @RequestParam(value = "text", required = false) String text,
      @RequestParam(value = "tags", required = false) String tags) throws Exception {
    JsonNode object = postService.addPost(time, active, title, text, tags);
    return object;
  }
}
