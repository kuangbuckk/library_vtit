package com.project.library.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_type", length = 50)
    private String tokenType;

    @Column(name = "refresh_token", length = 255, nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    private boolean revoked;
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static final int MAX_TOKEN_AMOUNT = 3;
}
