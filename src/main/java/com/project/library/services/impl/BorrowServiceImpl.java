package com.project.library.services.impl;

import com.project.library.dtos.BorrowDTO;
import com.project.library.dtos.search.BorrowSearchDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Borrow;
import com.project.library.constants.BorrowStatus;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.exceptions.DataOutOfBoundException;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.BorrowRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.BorrowPageResponse;
import com.project.library.responses.BorrowResponse;
import com.project.library.services.BorrowService;
import com.project.library.utils.MessageKeys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class BorrowServiceImpl implements BorrowService {
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public BorrowPageResponse getAllBorrows(int pageNumber, int size, BorrowSearchDTO borrowSearchDTO) {
        Page<Borrow> borrows = borrowRepository.findAll(PageRequest.of(pageNumber, size), borrowSearchDTO);
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
    public BorrowResponse getBorrowByCode(Long id) {
        Borrow existingBorrow = borrowRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, id));
        return BorrowResponse.fromBorrow(existingBorrow);
    }

    @Override
    public BorrowResponse addBorrow(BorrowDTO borrowDTO) {
        Book existingBook = bookRepository.findById(borrowDTO.getBookId())
                .orElseThrow(() ->
                        new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, borrowDTO.getBookId())
                );
        int remainBookCount = existingBook.getAmount();
        if (!isAvailableToBorrow(remainBookCount)) {
            throw new DataOutOfBoundException(MessageKeys.BOOK_OUT_OF_STOCK);
        } else if (borrowDTO.getBorrowAmount() > existingBook.getAmount()) {
            throw new DataOutOfBoundException(MessageKeys.BOOK_OUT_OF_STOCK);
        }
        existingBook.setAmount(existingBook.getAmount() - borrowDTO.getBorrowAmount());
        bookRepository.saveAndFlush(existingBook);

        User existingUser = userRepository.findById(borrowDTO.getUserId())
                .orElseThrow(() ->
                        new DataNotFoundException(MessageKeys.USER_NOT_FOUND, borrowDTO.getUserId())
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
    public BorrowResponse updateBorrow(BorrowDTO borrowDTO, Long id) {
        Borrow existingBorrow = borrowRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, id));
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
    public BorrowResponse deleteBorrow(Long id) {
        Borrow existingBorrow = borrowRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BORROW_NOT_FOUND, id));
        existingBorrow.setIsDeleted(true);
        borrowRepository.save(existingBorrow);
        return BorrowResponse.fromBorrow(existingBorrow);
    }

    @Override
    public void destroyBorrow(Long id) {
        Borrow existingBorrow = borrowRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.BORROW_NOT_FOUND, id));
        borrowRepository.delete(existingBorrow);
    }

    private boolean isAvailableToBorrow(int amount) {
        return amount > 0;
    }
}
