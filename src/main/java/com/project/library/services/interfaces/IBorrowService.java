package com.project.library.services.interfaces;

import com.project.library.dtos.BorrowDTO;
import com.project.library.dtos.BorrowSearchDTO;
import com.project.library.entities.Borrow;
import com.project.library.responses.BorrowPageResponse;
import com.project.library.responses.BorrowResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IBorrowService {
    BorrowPageResponse getAllBorrows(int pageNumber, int size, BorrowSearchDTO borrowSearchDTO);
    BorrowResponse getBorrowByCode(UUID code);
    BorrowResponse addBorrow(BorrowDTO borrowDTO);
    BorrowResponse updateBorrow(BorrowDTO borrowDTO, UUID code);
    BorrowResponse deleteBorrow(UUID code);
    void destroyBorrow(UUID code);
}
