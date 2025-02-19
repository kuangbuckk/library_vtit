package com.project.library.services.interfaces;

import com.project.library.dtos.BorrowDTO;
import com.project.library.entities.Borrow;

import java.util.List;
import java.util.UUID;

public interface IBorrowService {
    List<Borrow> getAllBorrows();
    Borrow getBorrowByCode(UUID code);
    Borrow addBorrow(BorrowDTO borrowDTO);
    Borrow updateBorrow(BorrowDTO borrowDTO, UUID code);
    void deleteBorrow(UUID code);
}
