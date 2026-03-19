package com.example.service;

import com.example.model.Squeak;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class SqueakBoard {

    private final List<Squeak> squeaks = new CopyOnWriteArrayList<>();
    private final AtomicInteger idSequence = new AtomicInteger(1);

    @PostConstruct
    void init() {
        post("biscuit", "Just fetched 47 sticks. New personal record. #hustle #grinding");
        post("mittens", "Knocked three things off the counter before breakfast. Disrupting the table economy.");
        post("mr-fluffington", "2am wheel run: 14 miles. Sleep is for Series B companies.");
        post("captain-waddles", "Stand-up complete. Snack acquired. Pivoting to nap. #lean #agile");
        post("biscuit", "The mud patch behind the oak tree is now fully operational. Tours available.");
        post("professor-whiskers", "Took a meeting. Did not find it valuable. Did not attend the follow-up.");
    }

    public Squeak post(String petId, String content) {
        Squeak squeak = new Squeak();
        squeak.id = "squeak-" + idSequence.getAndIncrement();
        squeak.petId = petId;
        squeak.content = content;
        squeak.postedAt = Instant.now().toString();
        squeaks.add(squeak);
        return squeak;
    }

    public List<Squeak> all() {
        return List.copyOf(squeaks);
    }

    public List<Squeak> findByPetId(String petId) {
        return squeaks.stream()
                .filter(s -> petId.equals(s.petId))
                .toList();
    }
}
