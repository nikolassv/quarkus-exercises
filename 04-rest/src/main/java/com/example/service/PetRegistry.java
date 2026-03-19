package com.example.service;

import com.example.model.Pet;
import com.example.model.Species;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class PetRegistry {

    private final Map<String, Pet> pets = new ConcurrentHashMap<>();
    private final AtomicInteger idSequence = new AtomicInteger(100);

    @PostConstruct
    void init() {
        seed("biscuit", "Biscuit", "@sarahk", Species.DOG,
                "Professional stick fetcher and mud enthusiast. Open to fetch opportunities.");
        seed("mittens", "Mittens", "@tomr", Species.CAT,
                "Knocking things off tables since 2008. Serial disruptor.");
        seed("mr-fluffington", "Mr. Fluffington", "@jessica_h", Species.HAMSTER,
                "Running champion, wheel division. Bootstrapped. No VC money.");
        seed("captain-waddles", "Captain Waddles", "@devops_dan", Species.RABBIT,
                "Agile practitioner. Hops-driven development. Snack-first culture.");
        seed("professor-whiskers", "Professor Whiskers", "@vc_larry", Species.CAT,
                "Disrupting the napping industry. Advisor at three kibble startups.");
    }

    private void seed(String id, String name, String ownerHandle, Species species, String bio) {
        Pet pet = new Pet();
        pet.id = id;
        pet.name = name;
        pet.ownerHandle = ownerHandle;
        pet.species = species;
        pet.bio = bio;
        pets.put(id, pet);
    }

    public Pet register(Pet pet) {
        pet.id = "pet-" + idSequence.getAndIncrement();
        pets.put(pet.id, pet);
        return pet;
    }

    public Optional<Pet> findById(String id) {
        return Optional.ofNullable(id != null ? pets.get(id) : null);
    }

    public List<Pet> findBySpecies(Species species) {
        return pets.values().stream()
                .filter(p -> species == p.species)
                .toList();
    }

    public List<Pet> all() {
        return List.copyOf(pets.values());
    }
}
