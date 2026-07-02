package com.example.tsubuyaki.service;

import com.example.tsubuyaki.domain.Post;
import com.example.tsubuyaki.repository.PostLikeRepository;
import com.example.tsubuyaki.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PostServiceCreateTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("Service_create_投稿者と本文とアバター色_現在時刻付きの投稿を保存する")
    void create_savesPostWithAvatarColorAndCurrentTimestamp() {
        postService.create("alice", "hello", "blue");

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        then(postRepository).should().save(captor.capture());
        assertThat(captor.getValue().getAuthor()).isEqualTo("alice");
        assertThat(captor.getValue().getBody()).isEqualTo("hello");
        assertThat(captor.getValue().getAvatarColor()).isEqualTo("blue");
        assertThat(captor.getValue().getCreatedAt()).isNotNull();
    }
}
