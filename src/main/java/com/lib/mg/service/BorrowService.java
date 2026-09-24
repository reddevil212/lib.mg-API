package com.lib.mg.service;

import com.lib.mg.entity.Book;
import com.lib.mg.entity.Borrow;
import com.lib.mg.entity.UserInfo;
import com.lib.mg.enums.Status;
import com.lib.mg.repository.BookRepository;
import com.lib.mg.repository.BorrowRepository;
import com.lib.mg.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowService(BorrowRepository borrowRepository,
                         BookRepository bookRepository,
                         UserRepository userRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Borrow borrowBook(Long userId, Long bookId) {

        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        // Check if there are available copies for borrowing
        if (book.getNumberOfCopies() == null || book.getNumberOfCopies() <= 0) {
            throw new RuntimeException("No available copies for this book!");
        }

        // Decrement the number of copies
        book.setNumberOfCopies(book.getNumberOfCopies() - 1);
        bookRepository.save(book);

        // Create the borrow record
        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(LocalDate.now());

        // Set auto-due date to 30 days from now
        borrow.setDueDate(LocalDate.now().plusDays(30));

        borrow.setStatus(Status.BORROWED);

        return borrowRepository.save(borrow);
    }
    @Transactional
    public Borrow returnBook(Long borrowId) {
        // Find the borrow record
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + borrowId));

        if (Status.RETURNED.equals(borrow.getStatus())) {
            throw new RuntimeException("This book has already been returned.");
        }

        // Update borrow record status and return date
        borrow.setReturnDate(LocalDate.now());
        borrow.setStatus(Status.RETURNED);

        // Increment the available copies for the book
        Book book = borrow.getBook();
        if (book != null) {
            int currentCopies = (book.getNumberOfCopies() != null) ? book.getNumberOfCopies() : 0;
            book.setNumberOfCopies(currentCopies + 1);
            bookRepository.save(book);
        }

        return borrowRepository.save(borrow);
    }

    @Transactional(readOnly = true)
    public List<Borrow> getBorrowedBooksByUser(Long userId) {
        return borrowRepository.findByUserIdAndStatus(userId, Status.BORROWED);
    }
}
