package com.toomba.library.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.toomba.library.models.Book;
import com.toomba.library.repositories.BookRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.EmptyJsonResponse;

@RequiredArgsConstructor
@RequestMapping(path = "api/book")
@RestController
public class BookController {

    private final BookRepository bookRepository;

    @GetMapping("/all")
    public ResponseEntity getBooks() {
        List<Book> booksFound = bookRepository.findAll();

        if (booksFound != null && !booksFound.isEmpty()) {
            return new ResponseEntity(booksFound, HttpStatus.OK);
        } else {
            return new ResponseEntity(new ArrayList(), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getBook(@PathVariable Long id) {
        Optional<Book> bookFound = bookRepository.findById(id);

        if (bookFound.isPresent()) {
            return new ResponseEntity(bookFound.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable Long id) {
        Optional<Book> bookFound = bookRepository.findById(id);
        if (bookFound.isPresent()) {
            bookRepository.delete(bookFound.get());
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Transactional
    @PostMapping
    public ResponseEntity createBook(@RequestBody Book book) {
        if (book == null
                || book.getCategories() == null
                || book.getAuthor() == null
                || book.getDescription() == null
                || book.getCategories() == null
                || book.getTitle() == null) {
            return new ResponseEntity("Dont fill in null values", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(bookRepository.save(book), HttpStatus.OK);
        }
    }

    @PutMapping
    public ResponseEntity updateBook(@RequestBody Book book) {
        if (book.getCategories() == null
                || book.getAuthor() == null
                || book.getDescription() == null
                || book.getCategories() == null
                || book.getTitle() == null) {
            return new ResponseEntity("Dont fill in null values", HttpStatus.BAD_REQUEST);
        } else {
            Optional<Book> bookFound = bookRepository.findById(book.getId());
            if (bookFound.isPresent()) {
                return new ResponseEntity(bookRepository.save(book), HttpStatus.OK);
            } else {
                return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
            }
        }
    }
}
