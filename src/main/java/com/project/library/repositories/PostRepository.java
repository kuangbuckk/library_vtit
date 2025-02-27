package com.project.library.repositories;

import com.project.library.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query("SELECT p FROM Post p WHERE " +
            ":keyword IS NULL OR :keyword = '' OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    Page<Post> findAll(Pageable pageable, @Param("keyword") String keyword);
}
