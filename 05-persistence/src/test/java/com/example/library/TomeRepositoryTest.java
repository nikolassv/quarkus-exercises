package com.example.library;

import com.example.library.model.Chapter;
import com.example.library.model.Tome;
import com.example.library.repository.TomeRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestTransaction
class TomeRepositoryTest {

    @Inject
    TomeRepository tomeRepository;

    @Inject
    EntityManager em;

    @Test
    void chaptersAreRetrievableViaTome() {
        Tome tome = new Tome();
        tome.title = "Spells of the Ancient Order";
        tome.author = "Erasmus Cray";
        tome.magicLevel = 6;
        tomeRepository.save(tome);

        Chapter chapter = new Chapter();
        chapter.title = "Foundations";
        chapter.pageCount = 40;
        chapter.tome = tome;
        em.persist(chapter);

        em.flush();
        em.clear();

        Tome loaded = tomeRepository.findById(tome.id).orElseThrow();
        assertThat(loaded.chapters).hasSize(1);
    }

    @Test
    void searchingByAuthorReturnsMatchingTomes() {
        Tome tome = new Tome();
        tome.title = "Ember Scrolls";
        tome.author = "Pyra Ashborne";
        tome.magicLevel = 4;
        tomeRepository.save(tome);

        List<Tome> results = tomeRepository.findByAuthor("Pyra Ashborne");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).title).isEqualTo("Ember Scrolls");
    }
}
