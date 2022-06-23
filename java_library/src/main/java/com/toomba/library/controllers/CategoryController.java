package com.toomba.library.controllers;

import java.util.List;
import java.util.Optional;

import com.toomba.library.models.Category;
import com.toomba.library.repositories.BookRepository;
import com.toomba.library.repositories.CategoryRepository;

import lombok.NonNull;
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
 * REST Controller of the Categories.
 */
@RequiredArgsConstructor
@RequestMapping(path = "api/category")
@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public static final String NO_DATA = "No data";

    /**
     * Get all categories.
     *
     * @return all categories
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getCategories() {
        List<Category> categoriesFound = categoryRepository.findAll();

        if (!categoriesFound.isEmpty()) {
            return new ResponseEntity<>(categoriesFound, HttpStatus.OK);
        } else {
            return ResponseHandler.generateResponse(NO_DATA, HttpStatus.OK);
        }
    }

    /**
     * Find category by id.
     *
     * @param id to search for
     * @return info message or category
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategory(@PathVariable Long id) {
        Optional<Category> categoryFound = categoryRepository.findById(id);
        if (categoryFound.isPresent()) {
            return new ResponseEntity<>(categoryFound.get(), HttpStatus.OK);
        } else {
            return ResponseHandler.generateResponse(NO_DATA, HttpStatus.OK);
        }
    }

    /**
     * Category to delete.
     *
     * @param id to search for
     * @return info message or category
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        Optional<Category> categoryFound = categoryRepository.findById(id);
        if (categoryFound.isPresent()) {
            if(categoryFound.get().getBook() != null){
                categoryFound.get().getBook().forEach(book -> {
                    book.getCategories().removeIf(category -> category.getId().equals(categoryFound.get().getId()));
                    bookRepository.save(book);
                });
            }
            categoryRepository.delete(categoryFound.get());
            return new ResponseEntity<>(categoryFound.get(), HttpStatus.OK);
        }
        return ResponseHandler.generateResponse(NO_DATA, HttpStatus.OK);
    }

    /**
     * Post category.
     *
     * @param category to post
     * @return info message or category
     */
    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody @NonNull Category category) {
        if (category.getBook().size() > 0 || category.getTitle() == null) {
            return ResponseHandler.generateResponse("Dont add books or empty titles to the category", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.OK);
        }
    }

    /**
     * Put category.
     *
     * @param category to update
     * @return info message or category
     */
    @PutMapping
    public ResponseEntity<Object> updateCategory(@RequestBody Category category) {
        Optional<Category> categoryFound = categoryRepository.findById(category.getId());
        if (!categoryFound.isPresent()){
            return ResponseHandler.generateResponse(NO_DATA, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.OK);
        }
    }
}
