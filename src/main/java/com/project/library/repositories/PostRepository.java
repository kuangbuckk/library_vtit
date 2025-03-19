package com.project.library.repositories;

import com.project.library.dtos.search.PostSearchDTO;
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
    @Query("SELECT p FROM Post p JOIN FETCH p.book b JOIN FETCH p.comments c JOIN FETCH p.user u WHERE " +
            "p.title LIKE %:#{#postSearchDTO.title}% OR " +
            "p.content LIKE %:#{#postSearchDTO.content}% OR " +
            "u.code = :#{#postSearchDTO.userCode} OR " +
            "b.code = :#{#postSearchDTO.bookCode}")
    Page<Post> findAll(Pageable pageable, @Param("postSearchDTO") PostSearchDTO postSearchDTO);
}
