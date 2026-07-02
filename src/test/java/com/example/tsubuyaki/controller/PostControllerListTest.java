package com.example.tsubuyaki.controller;

import com.example.tsubuyaki.domain.Post;
import com.example.tsubuyaki.service.PostService;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PostController.class)
class PostControllerListTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    @DisplayName("投稿一覧_投稿があるとき_詳細画面へのリンクを表示する")
    void list_existingPosts_rendersDetailLink() throws Exception {
        Post post = new Post("alice", "hello", Instant.parse("2026-07-02T01:00:00Z"), "blue");
        ReflectionTestUtils.setField(post, "id", 1L);
        given(postService.latest()).willReturn(List.of(post));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/list"))
                .andExpect(content().string(containsString("href=\"/posts/1\"")))
                .andExpect(content().string(containsString("class=\"post__avatar\"")))
                .andExpect(content().string(containsString("background-color: blue")))
                .andExpect(content().string(containsString("詳細")));
    }

    @Test
    @DisplayName("投稿一覧_q指定あり_本文にキーワードを含む投稿だけを表示する")
    void list_withQuery_rendersOnlyMatchingPosts() throws Exception {
        Post hit = new Post("alice", "Spring Boot の話", Instant.parse("2026-07-02T01:00:00Z"));
        ReflectionTestUtils.setField(hit, "id", 1L);
        given(postService.searchByBody("Spring")).willReturn(List.of(hit));

        mockMvc.perform(get("/posts").param("q", "Spring"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/list"))
                .andExpect(content().string(containsString("Spring Boot の話")))
                .andExpect(content().string(not(containsString("関係ない投稿"))))
                .andExpect(content().string(containsString("name=\"q\"")))
                .andExpect(content().string(containsString("value=\"Spring\"")));
        then(postService).should().searchByBody("Spring");
        then(postService).should(never()).latest();
    }

    @Test
    @DisplayName("投稿一覧_q空文字_最新50件の既存挙動を維持する")
    void list_withBlankQuery_usesLatestPosts() throws Exception {
        Post post = new Post("bob", "通常一覧の投稿", Instant.parse("2026-07-02T02:00:00Z"));
        ReflectionTestUtils.setField(post, "id", 2L);
        given(postService.latest()).willReturn(List.of(post));

        mockMvc.perform(get("/posts").param("q", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/list"))
                .andExpect(content().string(containsString("通常一覧の投稿")))
                .andExpect(content().string(containsString("name=\"q\"")))
                .andExpect(content().string(containsString("value=\"\"")));
        then(postService).should().latest();
        then(postService).should(never()).searchByBody("");
    }
}
