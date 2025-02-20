package com.project.library.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "borrows")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;

    @ManyToOne
    @JoinColumn(name = "user_code", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_code", nullable = false)
    private Book book;

    @Column(name = "borrow_amount", nullable = false)
    private int borrowAmount;

    @Column(name = "borrow_at", nullable = false)
    private LocalDateTime borrowAt;

    @Column(name = "return_at")
    private LocalDateTime returnAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BorrowStatus status;

    @PrePersist
    protected void setDefaultStatus() {
        if (this.status == null) {
            this.status = BorrowStatus.BORROWED;
        }
    }
}

