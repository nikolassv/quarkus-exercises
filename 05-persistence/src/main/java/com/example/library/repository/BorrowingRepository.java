package com.example.library.repository;

import com.example.library.model.Apprentice;
import com.example.library.model.Borrowing;
import com.example.library.model.Tome;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class BorrowingRepository implements PanacheRepository<Borrowing> {

    // Task 6
    public List<Borrowing> findActiveLoans() {
        return list("returnedOn IS NOT NULL");
    }

    @Transactional
    public Borrowing borrow(Tome tome, Apprentice apprentice) {
        Borrowing borrowing = new Borrowing();
        borrowing.tome = tome;
        borrowing.apprentice = apprentice;
        borrowing.borrowedOn = LocalDate.now();
        persist(borrowing);
        return borrowing;
    }

    @Transactional
    public void returnTome(Long borrowingId) {
        findByIdOptional(borrowingId).ifPresent(b -> b.returnedOn = LocalDate.now());
    }
}
