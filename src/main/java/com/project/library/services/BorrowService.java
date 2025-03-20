package com.project.library.services;

import com.project.library.dtos.BorrowDTO;
import com.project.library.dtos.search.BorrowSearchDTO;
import com.project.library.responses.BorrowPageResponse;
import com.project.library.responses.BorrowResponse;

public interface BorrowService {
    BorrowPageResponse getAllBorrows(int pageNumber, int size, BorrowSearchDTO borrowSearchDTO);
    BorrowResponse getBorrowByCode(Long id);
    BorrowResponse addBorrow(BorrowDTO borrowDTO);
    BorrowResponse updateBorrow(BorrowDTO borrowDTO, Long id);
    BorrowResponse deleteBorrow(Long id);
    void destroyBorrow(Long id);
}
