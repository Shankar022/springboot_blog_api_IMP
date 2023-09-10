package com.springboot.blog.service;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {

    // Create a post
    PostDto createPost(PostDto postDto);

    // Create multiple posts
    List<PostDto> createPosts(List<PostDto> postDtoList);

    // Get all posts with pagination and sorting
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    // Get a post by its ID
    PostDto getPostById(long id);

    // Update a post by its ID
    PostDto updatePost(PostDto postDto, long id);

    // Delete a post by its ID
    void deletePost(long id);
}
