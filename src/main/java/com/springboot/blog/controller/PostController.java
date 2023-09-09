package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog/api/v1/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // create post endpoint
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postRequestDto) {
        return new ResponseEntity<>(postService.createPost(postRequestDto), HttpStatus.CREATED);
    }

    // get all posts
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    // get post by id
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }
}
