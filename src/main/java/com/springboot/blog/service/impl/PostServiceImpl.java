package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    // if a class has only one member then it is required to @Autowired annotation
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public PostDto createPost(PostDto postRequestDto) {

        // convert DTO to Entity
        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setDescription(postRequestDto.getDescription());
        post.setContent(postRequestDto.getContent());

        // save the post in DB
        Post newPost = postRepository.save(post);

        // convert Entity to DTO
        PostDto postResponseDto = new PostDto(
                newPost.getId(),
                newPost.getTitle(),
                newPost.getDescription(),
                newPost.getContent()
        );
        return postResponseDto;
    }
}
