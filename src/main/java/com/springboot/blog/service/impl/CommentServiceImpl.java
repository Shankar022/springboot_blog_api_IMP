package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    // Create a new comment for a specific post
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment newComment = mapDtoToEntity(commentDto);

        // Find the associated post or throw a ResourceNotFoundException
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", postId));

        // Set the post for the new comment and save it
        newComment.setPost(post);
        Comment comment = commentRepository.save(newComment);

        // Map Entity to DTO and return it
        return mapEntityToDto(comment);
    }

    // Get all comments for a specific post
    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // Find all comments associated with the post
        List<Comment> commentList = commentRepository.findCommentsByPostId(postId);

        // Map each comment Entity to DTO and return as a list
        return commentList.stream().map(this::mapEntityToDto).toList();
    }

    // Get a specific comment by its ID, ensuring it belongs to the specified post
    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Comment comment = validateCommentBelongsToPost(postId, commentId);

        // Map Entity to DTO and return it
        return mapEntityToDto(comment);
    }

    // Update a specific comment by its ID, ensuring it belongs to the specified post
    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        Comment comment = validateCommentBelongsToPost(postId, commentId);

        // Map DTO to Entity for the updated comment properties
        Comment requestComment = mapDtoToEntity(commentDto);
        comment.setName(requestComment.getName());
        comment.setEmail(requestComment.getEmail());
        comment.setBody(requestComment.getBody());

        // Save the updated comment and return it
        Comment updatedComment = commentRepository.save(comment);
        return mapEntityToDto(updatedComment);
    }

    // Delete a specific comment by its ID, ensuring it belongs to the specified post
    @Override
    public void deleteComment(long postId, long commentId) {
        Comment comment = validateCommentBelongsToPost(postId, commentId);

        // Delete the comment
        commentRepository.delete(comment);
    }

    // Define a method to validate if a comment belongs to a post
    private Comment validateCommentBelongsToPost(long postId, long commentId) {
        // Find the associated post or throw a ResourceNotFoundException
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", postId));

        // Find the comment by its ID or throw a ResourceNotFoundException
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "Id", commentId));

        // Check if the comment belongs to the specified post, otherwise throw a BlogAPIException
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment doesn't belong to the post !");
        }

        return comment;
    }

    // Convert DTO to Entity function
    private Comment mapDtoToEntity(CommentDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setId(commentRequestDto.getId());
        comment.setName(commentRequestDto.getName());
        comment.setEmail(commentRequestDto.getEmail());
        comment.setBody(commentRequestDto.getBody());
        return comment;
    }

    // Convert Entity to DTO function
    private CommentDto mapEntityToDto(Comment newComment) {
        return new CommentDto(
                newComment.getId(),
                newComment.getName(),
                newComment.getEmail(),
                newComment.getBody()
        );
    }
}
