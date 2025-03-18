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
@EntityListeners(AuditingEntityListener.class)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;

    @Column(name ="title", nullable = false)
    private String title;

    @Column(name ="author", nullable = false)
    private String author;

    @Column(name ="page_count", nullable = false)
    @Min(value = 0)
    private Integer pageCount;

    @Column(name ="amount", nullable = false)
    @Min(value = 0)
    private Integer amount;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "book_code"),
            inverseJoinColumns = @JoinColumn(name = "category_code")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;

}
