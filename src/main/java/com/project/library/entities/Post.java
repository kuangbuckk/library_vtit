package com.project.library.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "content", length = 255)
    private String content;

    @ManyToOne
    @JoinColumn(name = "book_code", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_code", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}
