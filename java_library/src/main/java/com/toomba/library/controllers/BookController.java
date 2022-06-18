package com.toomba.library.controllers;

import java.util.Optional;

import com.toomba.library.models.Book;
import com.toomba.library.repositories.BookRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.EmptyJsonResponse;

@RequiredArgsConstructor
@RequestMapping(path = "api/book")
@RestController
public class BookController {

    private final BookRepository bookRepository;

    @GetMapping("/{id}")
    public ResponseEntity getBook(@PathVariable Long id) {
        Optional<Book> bookFound = bookRepository.findById(id);
        if(bookFound.isPresent()){
            return new ResponseEntity(bookFound.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
        }
    }

}
