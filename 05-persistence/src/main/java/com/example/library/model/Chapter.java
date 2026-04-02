package com.example.library.model;

import jakarta.persistence.*;

@Entity
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;
    public int pageCount;

    @ManyToOne
    public Tome tome;
}
