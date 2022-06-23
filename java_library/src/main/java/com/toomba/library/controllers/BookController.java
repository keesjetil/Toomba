package com.toomba.library.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.toomba.library.models.Book;
import com.toomba.library.repositories.BookRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.ResponseHandler;

/**
 * REST Controller of the Books.
 */
@RequiredArgsConstructor
@RequestMapping(path = "api/book")
@RestController
public class BookController {

    private final BookRepository bookRepository;

    /**
     * Get all books.
     *
     * @return all books
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getBooks() {
        List<Book> booksFound = bookRepository.findAll();

        if (!booksFound.isEmpty()) {
            return new ResponseEntity<>(booksFound, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    /**
     * Find book by id.
     *
     * @param id to search for
     * @return book with specified id or nothing
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getBook(@PathVariable Long id) {
        Optional<Book> bookFound = bookRepository.findById(id);

        if (bookFound.isPresent()) {
            return new ResponseEntity<>(bookFound.get(), HttpStatus.OK);
        } else {
            return ResponseHandler.generateResponse("No data", HttpStatus.OK);
        }
    }

    /**
     * Delete book by id
     *
     * @param id to search for
     * @return succes or fail message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        Optional<Book> bookFound = bookRepository.findById(id);
        if (bookFound.isPresent()) {
            bookRepository.delete(bookFound.get());
            return ResponseHandler.generateResponse("Deleted book with id: " + id, HttpStatus.OK);
        }
        return ResponseHandler.generateResponse("Unable to Deleted book with id: " + id, HttpStatus.OK);

    }

    /**
     * Post book.
     *
     * @param book to post
     * @return fail message or saved book
     */
    @PostMapping
    public ResponseEntity<Object> createBook(@RequestBody Book book) {
        if (book == null
                || book.getCategories() == null
                || book.getAuthor() == null
                || book.getDescription() == null
                || book.getTitle() == null) {
            return ResponseHandler.generateResponse("Dont fill in null values", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(bookRepository.save(book), HttpStatus.OK);
        }
    }

    /**
     * Update book.
     *
     * @param book to update
     * @return info message or updated book
     */
    @PutMapping
    public ResponseEntity<Object> updateBook(@RequestBody Book book) {
        if (book.getCategories() == null
                || book.getAuthor() == null
                || book.getDescription() == null
                || book.getTitle() == null) {
            return ResponseHandler.generateResponse("Dont fill in null values", HttpStatus.BAD_REQUEST);
        } else {
            Optional<Book> bookFound = bookRepository.findById(book.getId());
            if (bookFound.isPresent()) {
                return new ResponseEntity<>(bookRepository.save(book), HttpStatus.OK);
            } else {
                return ResponseHandler.generateResponse("No data", HttpStatus.OK);
            }
        }
    }
}
