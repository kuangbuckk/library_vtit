package com.project.library.repositories;

import com.project.library.dtos.UserSearchDTO;
import com.project.library.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE " +
            "u.code = :#{#userSearchDTO.code} OR " +
            "u.username LIKE %:#{#userSearchDTO.username}% OR " +
            "u.email LIKE %:#{#userSearchDTO.email}% OR " +
            "u.fullName LIKE %:#{#userSearchDTO.fullName}% OR " +
            "u.phoneNumber LIKE %:#{#userSearchDTO.phoneNumber}% OR " +
            "u.address LIKE %:#{#userSearchDTO.address}% OR " +
            "u.dateOfBirth = :#{#userSearchDTO.dateOfBirth} OR " +
            "u.isActive = :#{#userSearchDTO.isActive}")
    Page<User> findAll(Pageable pageable, @Param("userSearchDTO") UserSearchDTO userSearchDTO);
    Optional<User> findByUsername(String username);
}
