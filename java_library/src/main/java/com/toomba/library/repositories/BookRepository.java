package com.toomba.library.repositories;

import com.toomba.library.models.Book;

import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
