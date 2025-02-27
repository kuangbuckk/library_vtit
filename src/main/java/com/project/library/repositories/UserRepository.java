package com.project.library.repositories;

import com.project.library.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE " +
            ":keyword IS NULL OR :keyword = '' OR u.username LIKE %:keyword% OR u.email LIKE %:keyword% " +
            "OR u.fullName LIKE %:keyword% OR u.phoneNumber LIKE %:keyword% OR u.address LIKE %:keyword%")
    Page<User> findAll(Pageable pageable, String keyword);
    Optional<User> findByUsername(String username);
}
