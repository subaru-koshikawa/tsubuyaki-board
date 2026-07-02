package com.example.tsubuyaki.repository;

import com.example.tsubuyaki.domain.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    long countByPostId(Long postId);

    Optional<PostLike> findByPostIdAndClientHash(Long postId, String clientHash);
}
