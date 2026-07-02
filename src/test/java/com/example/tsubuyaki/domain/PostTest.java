package com.example.tsubuyaki.domain;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    @DisplayName("Post_equals_同一インスタンス_trueを返す")
    void equals_sameInstance_returnsTrue() {
        Post post = new Post("alice", "hello", Instant.parse("2026-07-02T01:00:00Z"));

        assertThat(post).isEqualTo(post);
    }

    @Test
    @DisplayName("Post_equals_別クラス_falseを返す")
    void equals_otherType_returnsFalse() {
        Post post = new Post("alice", "hello", Instant.parse("2026-07-02T01:00:00Z"));

        assertThat(post).isNotEqualTo("not a post");
    }

    @Test
    @DisplayName("Post_equals_idが同じ投稿_trueを返す")
    void equals_sameId_returnsTrue() {
        Post left = new Post("alice", "hello", Instant.parse("2026-07-02T01:00:00Z"));
        Post right = new Post("bob", "different", Instant.parse("2026-07-02T02:00:00Z"));
        ReflectionTestUtils.setField(left, "id", 1L);
        ReflectionTestUtils.setField(right, "id", 1L);

        assertThat(left).isEqualTo(right);
        assertThat(left).hasSameHashCodeAs(right);
    }

    @Test
    @DisplayName("Post_equals_idが異なる投稿_falseを返す")
    void equals_differentId_returnsFalse() {
        Post left = new Post("alice", "hello", Instant.parse("2026-07-02T01:00:00Z"));
        Post right = new Post("alice", "hello", Instant.parse("2026-07-02T01:00:00Z"));
        ReflectionTestUtils.setField(left, "id", 1L);
        ReflectionTestUtils.setField(right, "id", 2L);

        assertThat(left).isNotEqualTo(right);
    }
}
