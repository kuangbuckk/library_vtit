package com.project.library.services;

import com.project.library.dtos.BorrowDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Borrow;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.BorrowRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.services.interfaces.IBorrowService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BorrowServiceImpl implements IBorrowService {
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public List<Borrow> getAllBorrows() {
        List<Borrow> borrows = borrowRepository.findAll();
        return borrows;
    }

    @Override
    public Borrow getBorrowByCode(UUID code) {
        Borrow existingBorrow = borrowRepository
                .findById(code)
                .orElseThrow(() -> new DataNotFoundException("Borrow with code " + code + " not found"));
        return existingBorrow;
    }

    @Override
    public Borrow addBorrow(BorrowDTO borrowDTO) {
        Book existingBook = bookRepository.findById(UUID.fromString(borrowDTO.getBookCode()))
                .orElseThrow(() ->
                        new DataNotFoundException("Book with code " + borrowDTO.getBookCode() + " not found")
                );

        User existingUser = userRepository.findById(UUID.fromString(borrowDTO.getUserCode()))
                .orElseThrow(() ->
                        new DataNotFoundException("User with code " + borrowDTO.getUserCode() + " not found")
                );

        Borrow borrow = Borrow.builder()
                .book(existingBook)
                .user(existingUser)
                .borrowAt(LocalDateTime.now())
                .returnAt(null)
                .status(null)
                .build();
        return borrowRepository.save(borrow);
    }

    @Override
    public Borrow updateBorrow(BorrowDTO borrowDTO, UUID code) {
        return null;
    }

    @Override
    public void deleteBorrow(UUID code) {
        borrowRepository.deleteById(code);
    }
}
