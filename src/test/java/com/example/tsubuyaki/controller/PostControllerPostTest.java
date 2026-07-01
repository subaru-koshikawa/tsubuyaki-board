package com.example.tsubuyaki.controller;

import com.example.tsubuyaki.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PostController.class)
class PostControllerPostTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    @DisplayName("投稿登録_入力正常_POST_posts_保存して一覧へリダイレクトする")
    void createPost_validInput_redirectsToList() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("author", "alice")
                        .param("body", "hello"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts"));

        then(postService).should().create("alice", "hello");
    }

    @Test
    @DisplayName("投稿登録_投稿者が空白のみ_入力画面をエラー付きで再表示する")
    void createPost_blankAuthor_rendersFormWithErrors() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("author", "   ")
                        .param("body", "hello"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/form"))
                .andExpect(model().attributeHasFieldErrors("postForm", "author"));

        then(postService).should(never()).create(anyString(), anyString());
    }

    @Test
    @DisplayName("投稿登録_本文が空白のみ_入力画面をエラー付きで再表示する")
    void createPost_blankBody_rendersFormWithErrors() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("author", "alice")
                        .param("body", "   "))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/form"))
                .andExpect(model().attributeHasFieldErrors("postForm", "body"));

        then(postService).should(never()).create(anyString(), anyString());
    }

    @Test
    @DisplayName("投稿登録_投稿者30文字かつ本文280文字_保存して一覧へリダイレクトする")
    void createPost_maxLengthInput_redirectsToList() throws Exception {
        String author = "a".repeat(30);
        String body = "b".repeat(280);

        mockMvc.perform(post("/posts")
                        .param("author", author)
                        .param("body", body))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts"));

        then(postService).should().create(author, body);
    }

    @Test
    @DisplayName("投稿登録_投稿者31文字_入力画面をエラー付きで再表示する")
    void createPost_tooLongAuthor_rendersFormWithErrors() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("author", "a".repeat(31))
                        .param("body", "hello"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/form"))
                .andExpect(model().attributeHasFieldErrors("postForm", "author"));

        then(postService).should(never()).create(anyString(), anyString());
    }

    @Test
    @DisplayName("投稿登録_本文281文字_入力画面をエラー付きで再表示する")
    void createPost_tooLongBody_rendersFormWithErrors() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("author", "alice")
                        .param("body", "b".repeat(281)))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/form"))
                .andExpect(model().attributeHasFieldErrors("postForm", "body"));

        then(postService).should(never()).create(anyString(), anyString());
    }
}
