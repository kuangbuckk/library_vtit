package com.project.library.repositories;

import com.project.library.entities.Function;
import com.project.library.entities.RoleGroup;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleGroupRepository extends JpaRepository<RoleGroup, UUID> {
//    @EntityGraph(attributePaths = {"functions"})
    @Query("SELECT rg.functions FROM RoleGroup rg WHERE rg.roleGroupName = :roleGroupName")
    List<Function> findFunctionsByRoleGroupName(String roleGroupName);
}
