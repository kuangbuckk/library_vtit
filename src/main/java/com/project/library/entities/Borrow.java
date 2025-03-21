package com.project.library.entities;

import com.project.library.constants.BorrowStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "borrows")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Borrow extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
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
}

