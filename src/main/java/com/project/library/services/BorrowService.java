package com.project.library.services;

import com.project.library.dtos.BorrowDTO;
import com.project.library.dtos.search.BorrowSearchDTO;
import com.project.library.responses.BorrowPageResponse;
import com.project.library.responses.BorrowResponse;

import java.util.UUID;

public interface BorrowService {
    BorrowPageResponse getAllBorrows(int pageNumber, int size, BorrowSearchDTO borrowSearchDTO);
    BorrowResponse getBorrowByCode(UUID code);
    BorrowResponse addBorrow(BorrowDTO borrowDTO);
    BorrowResponse updateBorrow(BorrowDTO borrowDTO, UUID code);
    BorrowResponse deleteBorrow(UUID code);
    void destroyBorrow(UUID code);
}
