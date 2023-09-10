package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Autowired
    // If a class has only one member, then it is not required to use the @Autowired annotation
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    // Create a new post
    @Override
    public PostDto createPost(PostDto postRequestDto) {
        // Convert DTO to Entity
        Post post = mapDtoToEntity(postRequestDto);
        // Save the post in the database
        Post newPost = postRepository.save(post);
        // Convert Entity to DTO
        return mapEntityToDto(newPost);
    }

    // Create multiple posts
    @Override
    public List<PostDto> createPosts(List<PostDto> postRequestDtoList) {
        // Convert DTOs to Entities
        List<Post> posts = postRequestDtoList.stream().map(this::mapDtoToEntity).toList();
        List<Post> newPosts = postRepository.saveAll(posts);
        // Convert Entities to DTOs
        return newPosts.stream().map(this::mapEntityToDto).toList();
    }

    // Get all posts with pagination and sorting
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
        //        : Sort.by(sortBy).descending();

        // Create a pageable instance with pagination and sorting parameters
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);
        Page<Post> posts = postRepository.findAll(pageable);
        // Get content for the page object and prepare the response
        List<Post> allPosts = posts.getContent();
        List<PostDto> content = allPosts.stream().map(this::mapEntityToDto).toList();
        return prepareResponseForGetAll(content, posts);
    }

    // Get a specific post by its ID
    @Override
    public PostDto getPostById(long id) {
        // Find the post by its ID or throw a ResourceNotFoundException
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));
        // Convert Entity to DTO
        return mapEntityToDto(post);
    }

    // Update a specific post by its ID
    @Override
    public PostDto updatePost(PostDto postRequestDto, long id) {
        // Find the post by its ID or throw a ResourceNotFoundException
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));
        // Convert DTO to Entity
        Post newPost = mapDtoToEntity(postRequestDto);
        // Update the post
        post.setTitle(newPost.getTitle());
        post.setDescription(newPost.getDescription());
        post.setContent(newPost.getContent());
        // Save the updated post
        Post updatedPost = postRepository.save(post);
        // Convert Entity to DTO for the response
        return mapEntityToDto(updatedPost);
    }

    // Delete a specific post by its ID
    @Override
    public void deletePost(long id) {
        // Find the post by its ID or throw a ResourceNotFoundException
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id));
        // Delete the post
        postRepository.delete(post);
    }

    // Convert DTO to Entity function
    private Post mapDtoToEntity(PostDto postRequestDto) {
        return modelMapper.map(postRequestDto,Post.class);
//        Post post = new Post();
//        post.setTitle(postRequestDto.getTitle());
//        post.setDescription(postRequestDto.getDescription());
//        post.setContent(postRequestDto.getContent());
//        return post;
    }

    // Convert Entity to DTO function
    private PostDto mapEntityToDto(Post newPost) {
        return modelMapper.map(newPost, PostDto.class);
//        return new PostDto(
//                newPost.getId(),
//                newPost.getTitle(),
//                newPost.getDescription(),
//                newPost.getContent()
//        );
    }

    // Create a PostResponse object and return it
    private PostResponse prepareResponseForGetAll(List<PostDto> content, Page<Post> posts) {
        return new PostResponse(
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isFirst(),
                posts.isLast(),
                content
        );
    }
}
