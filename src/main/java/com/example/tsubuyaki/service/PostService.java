package com.example.tsubuyaki.service;

import com.example.tsubuyaki.domain.Post;
import com.example.tsubuyaki.domain.PostLike;
import com.example.tsubuyaki.repository.PostLikeRepository;
import com.example.tsubuyaki.repository.PostRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository repository;
    private final PostLikeRepository postLikeRepository;

    public PostService(PostRepository repository, PostLikeRepository postLikeRepository) {
        this.repository = repository;
        this.postLikeRepository = postLikeRepository;
    }

    public List<Post> latest() {
        return repository.findTop50ByOrderByCreatedAtDesc();
    }

    public List<Post> searchByBody(String keyword) {
        return repository.findTop50ByBodyContainingOrderByCreatedAtDesc(keyword);
    }

    public Optional<Post> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Post create(String author, String body) {
        return repository.save(new Post(author, body, Instant.now()));
    }

    public long likeCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }

    @Transactional
    public long toggleLike(Long postId, String clientHash) {
        return postLikeRepository.findByPostIdAndClientHash(postId, clientHash)
                .map(existing -> {
                    postLikeRepository.delete(existing);
                    return postLikeRepository.countByPostId(postId);
                })
                .orElseGet(() -> {
                    Post post = repository.findById(postId)
                            .orElseThrow(() -> new IllegalArgumentException("Post not found: " + postId));
                    postLikeRepository.save(new PostLike(post, clientHash));
                    return postLikeRepository.countByPostId(postId);
                });
    }
}
