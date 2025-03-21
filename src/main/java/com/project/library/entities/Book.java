package com.project.library.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {
    @Column(name ="title", nullable = false)
    private String title;

    @Column(name ="author", nullable = false)
    private String author;

    @Column(name ="page_count", nullable = false)
    @Min(value = 0)
    private Integer pageCount;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "description", length = 2500)
    private String description;

    @Column(name ="amount", nullable = false)
    @Min(value = 0)
    private Integer amount;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;

    public String getCategoryNames() {
        return this.categories.stream()
                .map(Category::getCategoryName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No Category");
    }

}
