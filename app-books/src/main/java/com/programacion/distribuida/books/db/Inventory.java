package com.programacion.distribuida.books.db;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String book_isbn;
    private Integer sold;
    private Integer supplied;
    private Integer version;

    @OneToOne
    @MapsId
    @JoinColumn(name = "book_isbn")
    private Book book;

}
