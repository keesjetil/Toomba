package com.toomba.library.controllers;

import static com.toomba.library.controllers.CategoryController.NO_DATA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.toomba.library.models.Book;
import com.toomba.library.models.Category;
import com.toomba.library.repositories.BookRepository;
import com.toomba.library.repositories.CategoryRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryControllerTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    CategoryController categoryController;

    @Autowired
    CategoryRepository categoryRepository;

    private Book testBook;
    private Category testCategory;
    private Object resultCategory;
    private Map<String, Object> resultJson;


    private List<Category> resultCategories;
    private HttpStatus resultStatus;
    private Set<Category> categories;

    @BeforeEach
    public void setup() {
        categoryRepository.deleteAll();
        categories = new HashSet<>();
        categoryRepository.save(new Category("Horror"));
        categoryRepository.save(new Category("Comedy"));
        categoryRepository.findAll().forEach(category -> categories.add(category));
    }

    @Test
    @Transactional
    void getCategories() {
        whenGetCategories();
        assertTrue(categories.size() == 2);

        whenGetCategoriesIsEmpty();
        assertTrue(resultJson.get("message").equals(NO_DATA));
    }

    @Test
    @Transactional
    void getCategory() {
        givenCategoryExists();
        whenGetCategory();
        thenResultEqualToTest();

        whenGetCategoriesIsEmpty();
        whenGetCategory();
        thenResultEqualToMessage(NO_DATA, HttpStatus.OK);
    }

    @Test
    @Transactional
    void createCategory() {
        givenNewCategory();
        whenPostCategory();
        thenResultEqualToTest();

        testCategory.setTitle(null);
        whenPostCategory();
        thenResultEqualToMessage("Dont add books or empty titles to the category", HttpStatus.BAD_REQUEST);
    }

    @Test
    @Transactional
    void updateCategory() {
        givenCategoryExists();
        testCategory.setTitle("TestKees");
        whenPutCategory();
        thenResultEqualToTest();

        whenGetCategoriesIsEmpty();
        whenPutCategory();
        thenResultEqualToMessage(NO_DATA, HttpStatus.OK);

    }

    @Test
    @Transactional
    void deleteCategoryTest1() {
        givenCategoryExists();
        testCategory.setTitle("TestKees");
        whenDeleteCategory();
        thenResultEqualToTest();

        categoryRepository.deleteAll();
        whenDeleteCategory();
        thenResultEqualToMessage(NO_DATA, HttpStatus.OK);

    }

    @Test
    @Transactional
    void deleteCategoryTest2() {
        givenCategoryExists();
        givenCategoryAndBook();
        whenDeleteCategory();
        thenResultEqualToTest();
        thenTestBookHasNoCategories();
    }

    private void givenCategoryAndBook(){
        categories.clear();
        testBook = new Book("title", "description", "author", categories);
        testBook = bookRepository.save(testBook);
        testCategory.getBook().add(testBook);
        categoryRepository.save(testCategory);
    }


    private void givenCategoryExists() {
        testCategory = categoryRepository.findAll().iterator().next();
    }

    private void givenNewCategory() {
        testCategory = new Category("HAI");
    }

    private void whenGetCategory() {
        ResponseEntity result = categoryController.getCategory(testCategory.getId());
        resultStatus = result.getStatusCode();
        resultCategory = result.getBody();
    }

    private void whenGetCategories() {
        ResponseEntity result = categoryController.getCategories();
        resultStatus = result.getStatusCode();
        resultCategories = (List<Category>) result.getBody();
    }

    private void whenGetCategoriesIsEmpty() {
        categoryRepository.deleteAll();
        ResponseEntity result = categoryController.getCategories();
        resultStatus = result.getStatusCode();
        resultJson = (Map<String, Object>) result.getBody();
    }

    private void whenDeleteCategory() {
        ResponseEntity result = categoryController.deleteCategory(testCategory.getId());
        resultStatus = result.getStatusCode();
        resultCategory = result.getBody();
    }

    private void whenPostCategory() {
        ResponseEntity result = categoryController.createCategory(testCategory);
        resultStatus = result.getStatusCode();
        resultCategory = result.getBody();
    }

    private void whenPutCategory() {
        ResponseEntity result = categoryController.updateCategory(testCategory);
        resultStatus = result.getStatusCode();
        resultCategory = result.getBody();
    }

    private void thenResultEqualToTest() {
        Category categoryResult = (Category) resultCategory;
        assertEquals(categoryResult.getBook(), testCategory.getBook());
        assertEquals(categoryResult.getTitle(), testCategory.getTitle());
    }

    ;

    private void thenResultEqualToMessage(String message, HttpStatus status) {
        Map<String, Object> categoryResult = (Map<String, Object>) resultCategory;
        assertTrue(categoryResult.get("message").equals(message));
        assertTrue(categoryResult.get("status").equals(status.value()));
    }

    private void thenTestBookHasNoCategories(){
        bookRepository.findById(testBook.getId()).get().getCategories().forEach(category -> {
            assertTrue(category.getId() != testCategory.getId());
        });
        assertFalse(bookRepository.findById(testBook.getId()).get().getCategories().contains(testCategory));
    }

}