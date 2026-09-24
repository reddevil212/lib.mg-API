package com.lib.mg.controller;

import com.lib.mg.entity.Borrow;
import com.lib.mg.service.BorrowService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    // POST /borrows/user/1/book/5
    @PostMapping("/user/{userId}/book/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Borrow borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return borrowService.borrowBook(userId, bookId);
    }

    // POST /borrows/1/return
    @PostMapping("/{borrowId}/return")
    public Borrow returnBook(@PathVariable Long borrowId) {
        return borrowService.returnBook(borrowId);
    }

    @GetMapping("/user/{userId}")
    public List<Borrow> getBorrowedBooksByUser(@PathVariable Long userId) {
        return borrowService.getBorrowedBooksByUser(userId);
    }
}
