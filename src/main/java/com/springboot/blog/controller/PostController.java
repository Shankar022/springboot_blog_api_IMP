package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
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

    // Create a post
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postRequestDto) {
        PostDto postResponseDto = postService.createPost(postRequestDto);
        return new ResponseEntity<>(postResponseDto, HttpStatus.CREATED);
    }

    // Create multiple posts
    @PostMapping("/create_posts")
    public ResponseEntity<List<PostDto>> createPosts(@RequestBody List<PostDto> postRequestDtoList) {
        List<PostDto> postResponseDtoList = postService.createPosts(postRequestDtoList);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.CREATED);
    }

    // Get all posts with pagination and sorting
    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        PostResponse postResponseDtoList = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    // Get a post by its ID
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(value = "id") Long id) {
        PostDto postResponseDto = postService.getPostById(id);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    // Update a post by its ID
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postRequestDto, @PathVariable(value = "id") Long id) {
        PostDto postResponseDto = postService.updatePost(postRequestDto, id);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    // Delete a post by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(value = "id") Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(String.format("Post deleted successfully, ID: %s ", id), HttpStatus.OK);
    }
}
