package com.example.library.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Apprentice extends PanacheEntity {

    public String name;
    public String house;
    public int enrollmentYear;

    // Task 5
    public static List<Apprentice> findByHouse(String house) {
        return list("houseName", house);
    }
}
