package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {

    // Create a comment for a post
    CommentDto createComment(long postId, CommentDto commentDto);

    // Get all comments for a post by post ID
    List<CommentDto> getCommentsByPostId(long postId);

    // Get a comment by its ID
    CommentDto getCommentById(long postId, long commentId);

    // Update a comment by its ID
    CommentDto updateComment(long postId, long commentId, CommentDto commentDto);

    // Delete a comment by its ID
    void deleteComment(long postId, long commentId);
}
