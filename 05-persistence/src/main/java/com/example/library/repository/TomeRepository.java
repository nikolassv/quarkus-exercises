package com.example.library.repository;

import com.example.library.model.Tome;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TomeRepository {

    @Inject
    EntityManager em;

    public List<Tome> listAll() {
        return em.createQuery("SELECT t FROM Tome t", Tome.class).getResultList();
    }

    @Transactional
    public Tome save(Tome tome) {
        em.persist(tome);
        return tome;
    }

    public Optional<Tome> findById(Long id) {
        return Optional.ofNullable(em.find(Tome.class, id));
    }

    @Transactional
    public void delete(Long id) {
        Tome tome = em.find(Tome.class, id);
        if (tome != null) {
            em.remove(tome);
        }
    }

    // Task 3
    public List<Tome> findByAuthor(String author) {
        return em.createQuery(
                "SELECT t FROM tome t WHERE t.author = :author", Tome.class)
                .setParameter("author", author)
                .getResultList();
    }
}
