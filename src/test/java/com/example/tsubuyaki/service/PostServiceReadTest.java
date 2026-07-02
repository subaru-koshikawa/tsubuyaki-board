package com.example.tsubuyaki.service;

import com.example.tsubuyaki.domain.Post;
import com.example.tsubuyaki.repository.PostLikeRepository;
import com.example.tsubuyaki.repository.PostRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PostServiceReadTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("Service_latest_Repositoryの最新50件を返す")
    void latest_returnsRepositoryResult() {
        List<Post> posts = List.of(new Post("alice", "hello", Instant.parse("2026-07-02T01:00:00Z")));
        given(postRepository.findTop50ByOrderByCreatedAtDesc()).willReturn(posts);

        assertThat(postService.latest()).isSameAs(posts);
    }

    @Test
    @DisplayName("Service_searchByBody_本文検索結果を返す")
    void searchByBody_returnsRepositoryResult() {
        List<Post> posts = List.of(new Post("alice", "Spring Boot", Instant.parse("2026-07-02T01:00:00Z")));
        given(postRepository.findTop50ByBodyContainingOrderByCreatedAtDesc("Spring")).willReturn(posts);

        assertThat(postService.searchByBody("Spring")).isSameAs(posts);
    }

    @Test
    @DisplayName("Service_findById_Repositoryの検索結果を返す")
    void findById_returnsRepositoryResult() {
        Post post = new Post("alice", "hello", Instant.parse("2026-07-02T01:00:00Z"));
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        assertThat(postService.findById(1L)).containsSame(post);
    }

    @Test
    @DisplayName("Service_likeCount_Repositoryのいいね数を返す")
    void likeCount_returnsRepositoryCount() {
        given(postLikeRepository.countByPostId(1L)).willReturn(3L);

        assertThat(postService.likeCount(1L)).isEqualTo(3L);
    }

    @Test
    @DisplayName("Service_toggleLike_投稿が存在しない_例外を返し保存しない")
    void toggleLike_missingPost_throwsException() {
        given(postLikeRepository.findByPostIdAndClientHash(99L, "12345678")).willReturn(Optional.empty());
        given(postRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postService.toggleLike(99L, "12345678"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Post not found: 99");
        then(postLikeRepository).shouldHaveNoMoreInteractions();
    }
}
