package com.project.library.repositories;

import com.project.library.dtos.BorrowSearchDTO;
import com.project.library.entities.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, UUID> {
    @Query("SELECT br FROM Borrow br WHERE " +
            "br.code = :#{#borrowSearchDTO.code} OR " +
            "br.user.code = :#{#borrowSearchDTO.userCode} OR " +
            "br.book.code = :#{#borrowSearchDTO.bookCode} OR " +
            "br.borrowAt = :#{#borrowSearchDTO.borrowAt} OR " +
            "br.returnAt = :#{#borrowSearchDTO.returnAt} OR " +
            "br.status = :#{#borrowSearchDTO.status}")
    Page<Borrow> findAll(Pageable pageable, @Param("borrowSearchDTO") BorrowSearchDTO borrowSearchDTO);
}
