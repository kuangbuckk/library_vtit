package com.project.library.repositories;

import com.project.library.entities.RoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleGroupRepository extends JpaRepository<RoleGroup, UUID> {
}
