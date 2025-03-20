package com.project.library.repositories;

import com.project.library.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByIdIn(List<Long> codes);
    List<Category> findByCategoryNameIn(List<String> categoryNames);
}
