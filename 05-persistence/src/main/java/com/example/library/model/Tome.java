package com.example.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    public int magicLevel;

    @JsonIgnore
    @OneToMany
    public List<Chapter> chapters = new ArrayList<>();
}
