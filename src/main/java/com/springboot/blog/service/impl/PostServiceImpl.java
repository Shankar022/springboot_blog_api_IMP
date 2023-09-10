package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // convert DTO o Entity
        Post post = mapDtoToEntity(postRequestDto);
        // save the post in DB
        Post newPost = postRepository.save(post);
        // convert Entity to DTO
        return mapEntityToDto(newPost);
    }

    @Override
    public List<PostDto> createPosts(List<PostDto> postRequestDtoList) {
        // convert DTO o Entity
        List<Post> posts = postRequestDtoList.stream().map(this::mapDtoToEntity).toList();
        List<Post> newPosts = postRepository.saveAll(posts);
        return newPosts.stream().map(this::mapEntityToDto).toList();
    }


    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        // Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
        //        : Sort.by(sortBy).descending();
        // create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);
        Page<Post> posts = postRepository.findAll(pageable);
        // get content for page object
        List<Post> allPosts = posts.getContent();
        List<PostDto> content =  allPosts.stream().map(this::mapEntityToDto).toList();
        return prepareResponseForGetAll(content,posts);
    }


    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));
        return mapEntityToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postRequestDto, long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));

        // convert DTO to Entity
        Post newPost = mapDtoToEntity(postRequestDto);
        // update the post
        post.setTitle(newPost.getTitle());
        post.setDescription(newPost.getDescription());
        post.setContent(newPost.getContent());

        Post updatedPost = postRepository.save(post);
        return mapEntityToDto(updatedPost);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));
        postRepository.delete(post);
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
    private PostDto mapEntityToDto(Post newPost) {
        return new PostDto(
                newPost.getId(),
                newPost.getTitle(),
                newPost.getDescription(),
                newPost.getContent()
        );
    }

    // create a PostResponse object and return it back
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
