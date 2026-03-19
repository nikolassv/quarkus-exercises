package com.example.exception;

public class PetNotFoundException extends RuntimeException {

    public final String petId;

    public PetNotFoundException(String petId) {
        super("No pet found with id: " + petId);
        this.petId = petId;
    }
}
