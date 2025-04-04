package com.project.library.repositories;

import com.project.library.dtos.search.BookSearchDTO;
import com.project.library.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN FETCH b.categories c WHERE " +
            "b.id = :#{#bookSearchDTO.id} OR " +
            "b.title LIKE %:#{#bookSearchDTO.title}% OR " +
            "b.author LIKE %:#{#bookSearchDTO.author}% OR " +
            "b.createdAt >= :#{#bookSearchDTO.publishedAt} OR " +
            "c.categoryName IN :#{#bookSearchDTO.categoryNames}")
    Page<Book> findAll(Pageable pageable, @Param("bookSearchDTO") BookSearchDTO bookSearchDTO);
    boolean existsByTitle(String title);
}
