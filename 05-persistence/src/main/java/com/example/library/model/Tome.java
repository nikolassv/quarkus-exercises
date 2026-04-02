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
    @Column(name = "magic_level")
    public int magicLevel;

    @OneToMany
    public List<Chapter> chapters = new ArrayList<>();
}
