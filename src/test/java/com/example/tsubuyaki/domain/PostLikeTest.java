package com.example.tsubuyaki.domain;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostLikeTest {

    @Test
    @DisplayName("PostLike_コンストラクタ_投稿とclientHashを保持する")
    void constructor_setsPostAndClientHash() {
        Post post = new Post("alice", "hello", Instant.parse("2026-07-02T01:00:00Z"));

        PostLike postLike = new PostLike(post, "12345678");

        assertThat(postLike.getId()).isNull();
        assertThat(postLike.getPost()).isSameAs(post);
        assertThat(postLike.getClientHash()).isEqualTo("12345678");
    }

    @Test
    @DisplayName("PostLike_JPA用コンストラクタ_空のインスタンスを作成できる")
    void protectedConstructor_createsEmptyInstance() {
        PostLike postLike = new PostLike();

        assertThat(postLike.getId()).isNull();
        assertThat(postLike.getPost()).isNull();
        assertThat(postLike.getClientHash()).isNull();
    }
}
