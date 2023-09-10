package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment newComment = mapDtoToEntity(commentDto);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", postId));

        newComment.setPost(post);
        Comment comment = commentRepository.save(newComment);
        return mapEntityToDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> commentList = commentRepository.findCommentsByPostId(postId);
        return commentList.stream().map(this::mapEntityToDto).toList();
    }


    // convert DTO to Entity function
    private Comment mapDtoToEntity(CommentDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setId(commentRequestDto.getId());
        comment.setName(commentRequestDto.getName());
        comment.setEmail(commentRequestDto.getEmail());
        comment.setBody(commentRequestDto.getBody());
        return comment;
    }

    // convert Entity to DTO function
    private CommentDto mapEntityToDto(Comment newComment) {
        return new CommentDto(
                newComment.getId(),
                newComment.getName(),
                newComment.getEmail(),
                newComment.getBody()
        );
    }
}
