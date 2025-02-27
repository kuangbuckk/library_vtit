package com.project.library.repositories;

import com.project.library.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query("SELECT b FROM Book b WHERE " +
            ":keyword IS NULL OR :keyword = '' OR b.title LIKE %:keyword% OR b.author LIKE %:keyword%")
    Page<Book> findAll(Pageable pageable, @Param("keyword") String keyword);
}
