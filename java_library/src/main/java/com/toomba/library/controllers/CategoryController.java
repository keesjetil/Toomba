package com.toomba.library.controllers;

import java.util.Optional;

import com.toomba.library.models.Category;
import com.toomba.library.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.EmptyJsonResponse;

@RequiredArgsConstructor
@RequestMapping(path = "api/category")
@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    public ResponseEntity getCategory(@PathVariable Long id) {
        Optional<Category> categoryFound = categoryRepository.findById(id);
        if (categoryFound.isPresent()) {
            return new ResponseEntity(categoryFound.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        Optional<Category> categoryFound = categoryRepository.findById(id);
        if (categoryFound.isPresent()) {
            categoryRepository.delete(categoryFound.get());
            return new ResponseEntity(categoryFound.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity createCategory(Category category) {
        if (category != null) {
            return new ResponseEntity(categoryRepository.save(category), HttpStatus.OK);
        } else {
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
        }
    }

    @PutMapping
    public ResponseEntity updateCategory(Category category) {
        Optional<Category> categoryFound = categoryRepository.findById(category.getId());
        if (category != null) {
            return new ResponseEntity(categoryRepository.save(category), HttpStatus.OK);
        } else {
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
        }
    }
}
