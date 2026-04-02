package com.example.library.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;
    public String author;
    public int magicLevel;

    @OneToMany
    public List<Chapter> chapters = new ArrayList<>();
}
