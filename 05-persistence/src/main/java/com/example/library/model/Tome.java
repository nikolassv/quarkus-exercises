package com.example.library.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tome {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tome_seq")
    @SequenceGenerator(name = "tome_seq", sequenceName = "tome_seq", allocationSize = 1)
    public Long id;

    public String title;
    public String author;
    @Column(name = "magic_level")
    public int magicLevel;

    @OneToMany(mappedBy = "tome")
    public List<Chapter> chapters = new ArrayList<>();
}
