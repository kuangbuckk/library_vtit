package com.project.library.services;

import com.project.library.dtos.BorrowDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Borrow;
import com.project.library.entities.BorrowStatus;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.BorrowRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.BorrowPageResponse;
import com.project.library.responses.BorrowResponse;
import com.project.library.services.interfaces.IBorrowService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class BorrowServiceImpl implements IBorrowService {
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public BorrowPageResponse getAllBorrows(Pageable pageable) {
        Page<Borrow> borrows = borrowRepository.findAll(pageable);
        int totalPages = borrows.getTotalPages();
        List<BorrowResponse> borrowResponseList = borrows.getContent()
                .stream()
                .map(borrow -> BorrowResponse.fromBorrow(borrow))
                .toList();
        return BorrowPageResponse.builder()
                .borrowResponseList(borrowResponseList)
                .totalPages(totalPages)
                .build();
    }

    @Override
    public BorrowResponse getBorrowByCode(UUID code) {
        Borrow existingBorrow = borrowRepository
                .findById(code)
                .orElseThrow(() -> new DataNotFoundException("Borrow with code " + code + " not found"));
        return BorrowResponse.fromBorrow(existingBorrow);
    }

    @Override
    public BorrowResponse addBorrow(BorrowDTO borrowDTO) {
        Book existingBook = bookRepository.findById(UUID.fromString(borrowDTO.getBookCode()))
                .orElseThrow(() ->
                        new DataNotFoundException("Book with code " + borrowDTO.getBookCode() + " not found")
                );
        int remainBookCount = existingBook.getAmount();
        if (!isAvailableToBorrow(remainBookCount)) {
            throw new DataNotFoundException("This book is not available to borrow!");
        } else if (borrowDTO.getBorrowAmount() > existingBook.getAmount()) {
            throw new DataNotFoundException("There is not enough borrow amount!");
        }
        existingBook.setAmount(existingBook.getAmount() - borrowDTO.getBorrowAmount());
        bookRepository.saveAndFlush(existingBook);

        User existingUser = userRepository.findById(UUID.fromString(borrowDTO.getUserCode()))
                .orElseThrow(() ->
                        new DataNotFoundException("User with code " + borrowDTO.getUserCode() + " not found")
                );

        Borrow newBorrow = Borrow.builder()
                .book(existingBook)
                .user(existingUser)
                .borrowAmount(borrowDTO.getBorrowAmount())
                .borrowAt(LocalDateTime.now())
                .returnAt(null)
                .status(null)
                .build();
        borrowRepository.save(newBorrow);
        return BorrowResponse.fromBorrow(newBorrow);
    }

    @Override
    public BorrowResponse updateBorrow(BorrowDTO borrowDTO, UUID code) {
        Borrow existingBorrow = borrowRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException("Borrow with code " + code + " not found"));
        Book existingBook = existingBorrow.getBook();

        if (borrowDTO.getStatus().equals(String.valueOf(BorrowStatus.RETURNED))) {
            existingBorrow.setReturnAt(LocalDateTime.now());
            existingBorrow.setStatus(BorrowStatus.RETURNED);

            existingBook.setAmount(existingBook.getAmount() + existingBorrow.getBorrowAmount());
        }

        if (Objects.equals(borrowDTO.getStatus(), String.valueOf(BorrowStatus.BORROWED))) {
            existingBook.setAmount(existingBook.getAmount() - borrowDTO.getBorrowAmount());
        }

        bookRepository.saveAndFlush(existingBook);
        borrowRepository.save(existingBorrow);
        return BorrowResponse.fromBorrow(existingBorrow);
    }

    @Override
    public void deleteBorrow(UUID code) {
        borrowRepository.deleteById(code);
    }

    private boolean isAvailableToBorrow(int amount) {
        return amount > 0;
    }
}
