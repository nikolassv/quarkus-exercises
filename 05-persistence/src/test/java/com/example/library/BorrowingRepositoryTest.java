package com.example.library;

import com.example.library.model.Apprentice;
import com.example.library.model.Borrowing;
import com.example.library.model.Tome;
import com.example.library.repository.BorrowingRepository;
import com.example.library.repository.TomeRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestTransaction
class BorrowingRepositoryTest {

    @Inject
    BorrowingRepository borrowingRepository;

    @Inject
    TomeRepository tomeRepository;

    @Test
    void activeLoansOnlyIncludesUnreturnedBooks() {
        Tome tome = new Tome();
        tome.title = "The Warding Grimoire";
        tome.author = "Orin Blackthorn";
        tome.magicLevel = 5;
        tomeRepository.save(tome);

        Apprentice apprentice = new Apprentice();
        apprentice.name = "Felix Ashgrove";
        apprentice.house = "Stormveil";
        apprentice.enrollmentYear = 1419;
        apprentice.persist();

        Borrowing activeLoan = borrowingRepository.borrow(tome, apprentice);
        Borrowing returnedLoan = borrowingRepository.borrow(tome, apprentice);
        borrowingRepository.returnTome(returnedLoan.id);

        List<Borrowing> active = borrowingRepository.findActiveLoans();

        assertThat(active).hasSize(1);
        assertThat(active.get(0).id).isEqualTo(activeLoan.id);
    }
}
