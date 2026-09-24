package com.lib.mg.service;

import com.lib.mg.entity.Book;
import com.lib.mg.entity.UserInfo;
import com.lib.mg.entity.Wishlist;
import com.lib.mg.repository.BookRepository;
import com.lib.mg.repository.UserRepository;
import com.lib.mg.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public WishlistService(WishlistRepository wishlistRepository,
                           UserRepository userRepository,
                           BookRepository bookRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Wishlist addToWishlist(Long userId, Long bookId) {
        if (wishlistRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new RuntimeException("Book is already in wishlist for user id: " + userId);
        }

        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .book(book)
                .addedAt(LocalDateTime.now())
                .build();

        return wishlistRepository.save(wishlist);
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long bookId) {
        if (!wishlistRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new RuntimeException("Book not found in wishlist for user id: " + userId + " and book id: " + bookId);
        }
        wishlistRepository.deleteByUserIdAndBookId(userId, bookId);
    }

    @Transactional(readOnly = true)
    public List<Wishlist> getWishlistByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return wishlistRepository.findByUserId(userId);
    }
}
