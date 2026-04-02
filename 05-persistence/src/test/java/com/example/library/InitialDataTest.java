package com.example.library;

import com.example.library.repository.TomeRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class InitialDataTest {

    @Inject
    TomeRepository tomeRepository;

    @Test
    void libraryHasFiveTomesOnStartup() {
        assertThat(tomeRepository.listAll()).hasSize(5);
    }
}
