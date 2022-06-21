package com.toomba.library.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
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

import utils.EmptyJsonResponse;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookController bookController;

    @Autowired
    CategoryRepository categoryRepository;

    private Book testBook;
    private Book resultBook;
    private EmptyJsonResponse resultJson;
    private String resultString;


    private List<Book> resultBooks;
    private HttpStatus resultStatus;
    private Set<Category> categories;

    @BeforeAll
    public void setup() {
        categories = new HashSet<>();
        categoryRepository.save(new Category("Horror"));
        categoryRepository.save(new Category("Comedy"));
        categoryRepository.findAll().forEach(category -> categories.add(category));

        testBook = bookRepository.save(new Book("title", "description", "author", categories));
    }

    @Test
    @Transactional
    void getBooks() {
        givenBookExists();
        whenGetBooks();
        thenBooksRetrieved();
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
    void deleteBook() {
        givenBookExists();
        whenDeleteBook();
        thenBookIsRemoved();
    }

    @Test
    @Transactional
    void getEmptyBook() {
        givenBooksDontExist();
        whenGetEmptyBook();
        thenReturnEmptyJson();
    }

    @Test
    @Transactional
    void getEmptyBooks() {
        givenBooksDontExist();
        whenGetBooks();
        thenReturnEmptyBooks();
    }

    @Test
    @Transactional
    void creatBookWithEmptyValue() {
        givenBookWithoutAuthor();
        whenCreateAuthorlessBook();
        thenReturnErrorString();
    }

    @Test
    @Transactional
    void updateBookWithEmptyValue() {
        givenBookExistsAndSetNullAuthor();
        whenUpdateAuthorlessBook();
        thenReturnErrorString();
    }

    @Test
    @Transactional
    void updateBookWithNonExistantBook() {
        givenBookDoesNotExist();
        whenUpdateEmptyBook();
        thenReturnEmptyJson();
    }

    //Givens
    private void givenBookExists() {
        testBook = bookRepository.findAll().iterator().next();
    }

    private void givenBookDoesNotExist() {

        testBook = bookRepository.findAll().iterator().next();
        bookRepository.delete(testBook);
    }

    private void givenBookExistsAndSetNullAuthor() {
        testBook = bookRepository.findAll().iterator().next();
        testBook.setAuthor(null);
    }

    private void givenBookWithoutAuthor() {
        testBook = new Book("post", "post", "post", categories);
        testBook.setAuthor(null);
    }


    private void givenBooksDontExist() {
        bookRepository.deleteAll();
    }

    private void givenCreatedBook() {
        testBook = new Book("post", "post", "post", categories);
    }

    private void givenUpdatedBook() {
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

    private void whenGetEmptyBook() {
        ResponseEntity result = bookController.getBook(testBook.getId());
        resultStatus = result.getStatusCode();
        resultJson = (EmptyJsonResponse) result.getBody();
    }

    private void whenGetBooks() {
        ResponseEntity result = bookController.getBooks();
        resultStatus = result.getStatusCode();
        resultBooks = (List<Book>) result.getBody();
    }

    private void whenCreateBook() {
        ResponseEntity result = bookController.createBook(testBook);
        resultStatus = result.getStatusCode();
        resultBook = (Book) result.getBody();
    }

    private void whenCreateAuthorlessBook() {
        ResponseEntity result = bookController.createBook(testBook);
        resultStatus = result.getStatusCode();
        resultString = (String) result.getBody();
    }

    private void whenUpdateAuthorlessBook() {
        ResponseEntity result = bookController.updateBook(testBook);
        resultStatus = result.getStatusCode();
        resultString = (String) result.getBody();
    }

    private void whenUpdateEmptyBook() {
        ResponseEntity result = bookController.updateBook(testBook);
        resultStatus = result.getStatusCode();
        resultJson = (EmptyJsonResponse) result.getBody();
    }


    private void whenDeleteBook() {
        ResponseEntity result = bookController.deleteBook(testBook.getId());
        resultStatus = result.getStatusCode();
        resultBook = (Book) result.getBody();
    }

    private void whenUpdateBook() {
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

    private void thenBooksRetrieved() {
        Book resultBook = resultBooks.stream().findFirst().get();
        assertEquals(resultBook.getId(), testBook.getId());
        assertEquals(resultBook.getDescription(), testBook.getDescription());
        assertEquals(resultBook.getTitle(), testBook.getTitle());
        assertEquals(resultBook.getAuthor(), testBook.getAuthor());
        assertEquals(resultBook.getCategories(), testBook.getCategories());
        assertEquals(resultStatus, HttpStatus.OK);
    }

    private void thenBookIsRemoved() {
        assertEquals(resultStatus, HttpStatus.OK);
        assertFalse(bookRepository.findById(testBook.getId()).isPresent());
    }

    private void thenBookInDatabase() {
        assertTrue(bookRepository.findById(testBook.getId()).isPresent());
    }

    private void thenBookUpdatedInDatabase() {
        assertEquals(resultBook.getId(), testBook.getId());
        assertEquals(resultBook.getDescription(), testBook.getDescription());
        assertEquals(resultBook.getTitle(), testBook.getTitle());
        assertEquals(resultBook.getAuthor(), testBook.getAuthor());
        assertEquals(resultBook.getCategories(), testBook.getCategories());
        assertEquals(resultStatus, HttpStatus.OK);
        thenBookInDatabase();
    }

    private void thenReturnEmptyJson() {
        assertEquals(resultJson.getClass(), new EmptyJsonResponse().getClass());
        assertEquals(resultStatus, HttpStatus.OK);
    }

    private void thenReturnErrorString() {
        assertTrue(resultString.equals("Dont fill in null values"));
        assertEquals(resultStatus, HttpStatus.BAD_REQUEST);
    }

    private void thenReturnEmptyBooks() {
        assertTrue(resultBooks.isEmpty());
        assertEquals(resultStatus, HttpStatus.OK);
    }
}