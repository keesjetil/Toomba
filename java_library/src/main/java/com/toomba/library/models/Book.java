package com.toomba.library.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    private Long id;

    private String title;
    private String description;

//    @ManyToMany
//    @JoinTable(
//            name="Book_Category",
//            joinColumns = @JoinColumn(name = "book_id"),
//            inverseJoinColumns = @JoinColumn(name = "category_id"))
//    private Set<Category> categories;

    public Book(String title, double price) {
        this.title = title;
    }
}
