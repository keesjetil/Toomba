package com.toomba.library.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import com.toomba.library.models.Book;
import com.toomba.library.models.Category;
import com.toomba.library.repositories.BookRepository;
import com.toomba.library.repositories.CategoryRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookController bookController;

    @Autowired
    CategoryRepository categoryRepository;

    Book testBook;

    Book resultBook;
    HttpStatus resultStatus;
    Set<Category> categories;

    @BeforeAll
    @Transactional
    public void setup() {
        categories = new HashSet();
        categoryRepository.save(new Category("Horror"));
        categoryRepository.save(new Category("Comedy"));
        categoryRepository.findAll().forEach(category -> categories.add(category));

        testBook  = bookRepository.save(new Book("title", "description", "author", categories));
    }

    @Test
    @Transactional
    void getBook() {
        givenBookExists();
        whenGetBook();
        thenBookRetrieved();
    }

    @Test
    void createBook() {
        givenCreatedBook();
        whenCreateBook();
        thenBookRetrieved();
        thenBookInDatabase();
    }

    @Test
    void updateBook() {
        givenUpdatedBook();
        whenUpdateBook();
        thenBookRetrieved();
        thenBookUpdatedInDatabase();
    }

    @Test
    @Transactional
    void deleteBook() {
        givenBookExists();
        whenDeleteBook();
        thenBookIsRemoved();
    }

    //Givens
    private void givenBookExists() {
        testBook = bookRepository.findAll().iterator().next();
    }
    private void givenCreatedBook(){
        testBook = new Book("post", "post", "post", categories);
    }

    private void givenUpdatedBook(){
        testBook = bookRepository.findById(testBook.getId()).orElseThrow();
        testBook.setAuthor("UPDATE");
        categories.clear();
        testBook.setCategories(categories);
        testBook.setTitle("UPDATE");
        testBook.setDescription("UPDATE");
    }

    // Whens
    private void whenGetBook() {
        ResponseEntity result = bookController.getBook(testBook.getId());
        resultStatus = result.getStatusCode();
        resultBook = (Book) result.getBody();
    }

    private void whenCreateBook(){
        ResponseEntity result = bookController.createBook(testBook);
        resultStatus = result.getStatusCode();
        resultBook = (Book) result.getBody();
    }

    private void whenDeleteBook() {
        ResponseEntity result = bookController.deleteBook(testBook.getId());
        resultStatus = result.getStatusCode();
        resultBook = (Book) result.getBody();
    }

    private void whenUpdateBook(){
        ResponseEntity result = bookController.updateBook(testBook);
        resultStatus = result.getStatusCode();
        resultBook = (Book) result.getBody();
    }

    // thens
    private void thenBookRetrieved() {
        assertEquals(resultBook.getId(), testBook.getId());
        assertEquals(resultBook.getDescription(), testBook.getDescription());
        assertEquals(resultBook.getTitle(), testBook.getTitle());
        assertEquals(resultBook.getAuthor(), testBook.getAuthor());
        assertEquals(resultBook.getCategories(), testBook.getCategories());
        assertEquals(resultStatus, HttpStatus.OK);
    }

    private void thenBookIsRemoved(){
        assertEquals(resultStatus, HttpStatus.OK);
        assertFalse(bookRepository.findById(testBook.getId()).isPresent());
    }

    private void thenBookInDatabase(){
        assertTrue(bookRepository.findById(testBook.getId()).isPresent());
    }

    private void thenBookUpdatedInDatabase(){
        assertEquals(resultBook.getId(), testBook.getId());
        assertEquals(resultBook.getDescription(), testBook.getDescription());
        assertEquals(resultBook.getTitle(), testBook.getTitle());
        assertEquals(resultBook.getAuthor(), testBook.getAuthor());
        assertEquals(resultBook.getCategories(), testBook.getCategories());
        assertEquals(resultStatus, HttpStatus.OK);
        thenBookInDatabase();
    }
}