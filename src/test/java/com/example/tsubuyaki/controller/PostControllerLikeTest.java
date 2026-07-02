package com.example.tsubuyaki.controller;

import com.example.tsubuyaki.service.PostService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerLikeTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    @DisplayName("いいね登録_POST_posts_id_likes_clientHashでトグルして詳細へ戻る")
    void like_firstRequest_togglesLikeAndRedirectsToDetail() throws Exception {
        String remoteAddress = "203.0.113.10";
        String userAgent = "MockMvc Browser";
        String clientHash = sha256First8(remoteAddress + userAgent);

        mockMvc.perform(post("/posts/{id}/likes", 1L)
                        .with(request -> {
                            request.setRemoteAddr(remoteAddress);
                            return request;
                        })
                        .header("User-Agent", userAgent))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts/1"));

        then(postService).should().toggleLike(1L, clientHash);
    }

    @Test
    @DisplayName("いいね解除_同一clientHashで二回POST_同じキーでトグルする")
    void like_secondRequestFromSameClient_togglesOffWithSameClientHash() throws Exception {
        String remoteAddress = "203.0.113.10";
        String userAgent = "MockMvc Browser";
        String clientHash = sha256First8(remoteAddress + userAgent);

        mockMvc.perform(post("/posts/{id}/likes", 1L)
                        .with(request -> {
                            request.setRemoteAddr(remoteAddress);
                            return request;
                        })
                        .header("User-Agent", userAgent))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts/1"));
        mockMvc.perform(post("/posts/{id}/likes", 1L)
                        .with(request -> {
                            request.setRemoteAddr(remoteAddress);
                            return request;
                        })
                        .header("User-Agent", userAgent))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/posts/1"));

        then(postService).should(times(2)).toggleLike(1L, clientHash);
    }

    private String sha256First8(String source) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(source.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest).substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
