package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.List;
import java.util.Optional;

public class FineH2RepositoryImpl implements FineRepository {


    @Autowired
    private EntityManager entityManager;


    @Override
    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        try {
            // Assuming Lending has a relationship with Fine and we use a join to find the Fine by lendingNumber
            Fine fine = entityManager.createQuery(
                            "SELECT f FROM Fine f " +
                                    "JOIN f.lending l " +
                                    "WHERE l.lendingNumber.lendingNumber = :lendingNumber", Fine.class)
                    .setParameter("lendingNumber", lendingNumber)
                    .getSingleResult();
            return Optional.of(fine);
        } catch (Exception e) {
            // If no result is found, return an empty Optional
            return Optional.empty();
        }
    }

    @Override
    public Iterable<Fine> findAll() {
        List<Fine> fines = entityManager.createQuery("SELECT f FROM Fine f", Fine.class).getResultList();
        return fines;
    }

    @Override
    @Transactional
    public Fine save(Fine fine) {
        if (fine.getPk() == null) {
            // If pk is null, persist a new entity
            entityManager.persist(fine);
        } else {
            // Otherwise, merge the existing entity
            fine = entityManager.merge(fine);
        }
        return fine;
    }
}
