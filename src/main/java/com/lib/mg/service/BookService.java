package com.lib.mg.service;

import com.lib.mg.entity.Book;
import com.lib.mg.repository.BookRepository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // --- Book Operations ---

    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks(int limit) {
        return bookRepository.findAllBy(Limit.of(limit));
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    @Transactional
    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = getBookById(id);

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setPublisher(updatedBook.getPublisher());
        existingBook.setPublishYear(updatedBook.getPublishYear());
        existingBook.setPublishDate(updatedBook.getPublishDate());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setPages(updatedBook.getPages());
        existingBook.setGenres(updatedBook.getGenres());
        existingBook.setDescription(updatedBook.getDescription());
        existingBook.setBookFormat(updatedBook.getBookFormat());
        existingBook.setCoverImg(updatedBook.getCoverImg());
        existingBook.setEdition(updatedBook.getEdition());
        existingBook.setLanguage(updatedBook.getLanguage());
        existingBook.setLikedPercent(updatedBook.getLikedPercent());
        existingBook.setNumRatings(updatedBook.getNumRatings());
        existingBook.setNumberOfCopies(updatedBook.getNumberOfCopies());
        existingBook.setPrimaryAuthor(updatedBook.getPrimaryAuthor());
        existingBook.setRating(updatedBook.getRating());
        existingBook.setSeries(updatedBook.getSeries());

        return bookRepository.save(existingBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Transactional(readOnly = true)
    public Page<Book> searchBooks(String queryTerm, String title, String author, String genre, String isbn, Pageable pageable) {
        Specification<Book> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Global search query matching title, author, primaryAuthor, isbn, or genres
            if (queryTerm != null && !queryTerm.trim().isEmpty()) {
                String pattern = "%" + queryTerm.trim().toLowerCase() + "%";
                Predicate titleMatch = cb.like(cb.lower(root.get("title")), pattern);
                Predicate authorMatch = cb.like(cb.lower(root.get("author")), pattern);
                Predicate primaryAuthorMatch = cb.like(cb.lower(root.get("primaryAuthor")), pattern);
                Predicate isbnMatch = cb.like(cb.lower(root.get("isbn")), pattern);
                Predicate genreMatch = cb.like(cb.lower(root.get("genres")), pattern);

                predicates.add(cb.or(titleMatch, authorMatch, primaryAuthorMatch, isbnMatch, genreMatch));
            }

            // Specific field filters (case-insensitive partial matching)
            if (title != null && !title.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.trim().toLowerCase() + "%"));
            }

            if (author != null && !author.trim().isEmpty()) {
                String authorPattern = "%" + author.trim().toLowerCase() + "%";
                Predicate a1 = cb.like(cb.lower(root.get("author")), authorPattern);
                Predicate a2 = cb.like(cb.lower(root.get("primaryAuthor")), authorPattern);
                predicates.add(cb.or(a1, a2));
            }

            if (genre != null && !genre.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("genres")), "%" + genre.trim().toLowerCase() + "%"));
            }

            if (isbn != null && !isbn.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("isbn")), "%" + isbn.trim().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return bookRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooks(String title, String author, String genre) {
        return searchBooks(null, title, author, genre, null, Pageable.unpaged()).getContent();
    }
}
