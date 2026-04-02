package com.example.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    public Tome tome;

    @ManyToOne
    public Apprentice apprentice;

    public LocalDate borrowedOn;

    public LocalDate returnedOn; // null when still on loan
}
