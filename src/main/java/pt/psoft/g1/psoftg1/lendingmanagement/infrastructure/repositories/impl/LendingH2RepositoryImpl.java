package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LendingH2RepositoryImpl implements LendingRepository {

    @Autowired
    private EntityManager entityManager;


    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        Root<Lending> root = cq.from(Lending.class);
        cq.select(root).where(cb.equal(root.get("lendingNumber").get("lendingNumber"), lendingNumber));

        TypedQuery<Lending> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst();
    }


    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        Root<Lending> root = cq.from(Lending.class);
        Join<Lending, Book> bookJoin = root.join("book");
        Join<Lending, ReaderDetails> readerJoin = root.join("readerDetails");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(bookJoin.get("isbn"), isbn));
        predicates.add(cb.equal(readerJoin.get("readerNumber"), readerNumber));

        cq.select(root).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }


    @Override
    public int getCountFromCurrentYear() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Lending> root = cq.from(Lending.class);

        cq.select(cb.count(root)).where(cb.equal(cb.function("YEAR", Integer.class, root.get("startDate")), LocalDate.now().getYear()));

        return entityManager.createQuery(cq).getSingleResult().intValue();
    }

    @Override
    public List<Lending> listOutstandingByReaderNumber(String readerNumber) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        Root<Lending> root = cq.from(Lending.class);
        Join<Lending, ReaderDetails> readerJoin = root.join("readerDetails");

        cq.select(root).where(
                cb.equal(readerJoin.get("readerNumber"), readerNumber),
                cb.isNull(root.get("returnedDate"))
        );

        return entityManager.createQuery(cq).getResultList();
    }


    @Override
    public Double getAverageDuration() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<Lending> root = cq.from(Lending.class);

        Expression<Double> duration = cb.avg(cb.diff(root.get("returnedDate"), root.get("startDate")));
        cq.select(duration);

        return entityManager.createQuery(cq).getSingleResult();
    }


    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<Lending> root = cq.from(Lending.class);
        Join<Lending, Book> bookJoin = root.join("book");

        Expression<Double> avgDuration = cb.avg(cb.diff(root.get("returnedDate"), root.get("startDate")));
        cq.select(avgDuration).where(cb.equal(bookJoin.get("isbn"), isbn));

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        Root<Lending> root = cq.from(Lending.class);

        cq.select(root).where(
                cb.isNull(root.get("returnedDate")),
                cb.lessThan(root.get("limitDate"), LocalDate.now())
        ).orderBy(cb.asc(root.get("limitDate")));

        TypedQuery<Lending> query = entityManager.createQuery(cq);
        query.setFirstResult((page.getNumber() - 1) * page.getLimit());
        query.setMaxResults(page.getLimit());

        return query.getResultList();
    }


    @Override
    public List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        Root<Lending> root = cq.from(Lending.class);
        Join<Lending, Book> bookJoin = root.join("book", JoinType.LEFT);
        Join<Lending, ReaderDetails> readerJoin = root.join("readerDetails", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        if (readerNumber != null) predicates.add(cb.equal(readerJoin.get("readerNumber"), readerNumber));
        if (isbn != null) predicates.add(cb.equal(bookJoin.get("isbn"), isbn));
        if (returned != null) {
            if (returned) predicates.add(cb.isNotNull(root.get("returnedDate")));
            else predicates.add(cb.isNull(root.get("returnedDate")));
        }
        if (startDate != null) predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
        if (endDate != null) predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), endDate));

        cq.select(root).where(predicates.toArray(new Predicate[0]));

        TypedQuery<Lending> query = entityManager.createQuery(cq);
        query.setFirstResult((page.getNumber() - 1) * page.getLimit());
        query.setMaxResults(page.getLimit());

        return query.getResultList();
    }


    @Override
    public Lending save(Lending lending) {
        if (lending.getPk() == null) {
            entityManager.persist(lending);
            return lending;
        } else {
            return entityManager.merge(lending);
        }
    }

    @Override
    public void delete(Lending lending) {
        entityManager.remove(entityManager.contains(lending) ? lending : entityManager.merge(lending));
    }


}
