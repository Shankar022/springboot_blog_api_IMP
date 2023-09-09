package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Post post = mapDtoToEntity(postRequestDto);
        // save the post in DB
        Post newPost = postRepository.save(post);
        // convert Entity to DTO
        PostDto postResponseDto = mapEntityToDto(newPost);
        return postResponseDto;
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> allPosts = postRepository.findAll();
        return allPosts.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","Id",id));
        return mapEntityToDto(post);
    }


    // convert DTO to Entity function
    private Post mapDtoToEntity(PostDto postRequestDto) {
        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setDescription(postRequestDto.getDescription());
        post.setContent(postRequestDto.getContent());
        return post;
    }

    // convert Entity to DTO function
    private PostDto mapEntityToDto(Post newPost){
        return new PostDto(
                newPost.getId(),
                newPost.getTitle(),
                newPost.getDescription(),
                newPost.getContent()
        );
    }
}
