package com.example.tsubuyaki.repository;

import com.example.tsubuyaki.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findTop50ByOrderByCreatedAtDesc();

    List<Post> findTop50ByBodyContainingOrderByCreatedAtDesc(String keyword);
}
