package com.toomba.library.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    private Long id;

    private String title;

//    @ManyToMany(mappedBy="categories")
//    private Set<Book> books;

    public Category(String title) {
        this.title = title;
    }
}
