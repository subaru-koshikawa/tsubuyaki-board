package com.example.tsubuyaki.service;

import com.example.tsubuyaki.domain.Post;
import com.example.tsubuyaki.repository.PostLikeRepository;
import com.example.tsubuyaki.repository.PostRepository;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PostService.class)
class PostServiceLikeTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Test
    @DisplayName("Service_toggleLike_同一clientHash_登録して二回目で解除する")
    void toggleLike_sameClientHash_registersThenRemoves() {
        Post post = postRepository.save(new Post("alice", "hello", Instant.parse("2026-07-02T01:00:00Z")));

        long firstCount = postService.toggleLike(post.getId(), "12345678");
        long secondCount = postService.toggleLike(post.getId(), "12345678");

        assertThat(firstCount).isEqualTo(1);
        assertThat(secondCount).isZero();
        assertThat(postLikeRepository.countByPostId(post.getId())).isZero();
    }
}
