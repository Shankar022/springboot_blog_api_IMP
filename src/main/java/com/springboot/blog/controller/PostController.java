package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        PostDto postResponseDto = postService.createPost(postRequestDto);
        return new ResponseEntity<>(postResponseDto, HttpStatus.CREATED);
    }

    // create multiple posts
    @PostMapping("/create_posts")
    public ResponseEntity<List<PostDto>> createPosts(@RequestBody List<PostDto> postRequestDtoList) {
        List<PostDto> postResponseDtoList = postService.createPosts(postRequestDtoList);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.CREATED);
    }


    // get all posts
    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String  sortDir
    ) {
        PostResponse postResponseDtoList = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    // get post by id
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long id) {
        PostDto postResponseDto = postService.getPostById(id);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    // update a post by id
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postRequestDto, @PathVariable(name = "id") Long id) {
        PostDto postResponseDto = postService.updatePost(postRequestDto, id);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    //delete a post by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(String.format("Post deleted successfully, ID : %s ", id), HttpStatus.OK);
    }
}
