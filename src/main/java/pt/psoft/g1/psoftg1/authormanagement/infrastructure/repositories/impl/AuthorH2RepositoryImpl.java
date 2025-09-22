package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorH2RepositoryImpl implements AuthorRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        String authorQuery = "FROM Author a WHERE a.authorNumber = :authorNumber";
        TypedQuery<Author> query = entityManager.createQuery(authorQuery, Author.class);
        query.setParameter("authorNumber", authorNumber);
        return query.getResultStream().findFirst();
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        String authorQuery = "FROM Author a WHERE a.name.name LIKE :namePrefix";
        TypedQuery<Author> query = entityManager.createQuery(authorQuery, Author.class);
        query.setParameter("namePrefix", name + "%");
        return query.getResultList();
    }

    @Override
    public List<Author> searchByNameName(String name) {
        String authorQuery = "SELECT a FROM Author a WHERE a.name.name = :name";
        TypedQuery<Author> query = entityManager.createQuery(authorQuery, Author.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Author save(Author author) {
        if (author.getAuthorNumber() == null) {
            entityManager.persist(author);
        } else {
            entityManager.merge(author);
        }
        return author;
    }

    @Override
    public Iterable<Author> findAll() {
        String authorQuery = "FROM Author";
        TypedQuery<Author> query = entityManager.createQuery(authorQuery, Author.class);
        return query.getResultList();
        }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {
        String authorQuery = "SELECT new pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView(a.name.name, COUNT(l.pk)) " +
                "FROM Book b " +
                "JOIN b.authors a " +
                "JOIN Lending l ON l.book.pk = b.pk " +
                "GROUP BY a.name " +
                "ORDER BY COUNT(l) DESC";

        TypedQuery<AuthorLendingView> query = entityManager.createQuery(authorQuery, AuthorLendingView.class);
        query.setFirstResult((int) pageableRules.getOffset());
        query.setMaxResults(pageableRules.getPageSize());
        List<AuthorLendingView> results = query.getResultList();
        return new PageImpl<>(results, pageableRules, results.size());
    }

    @Override
    public void delete(Author author) {
        entityManager.remove(entityManager.contains(author) ? author : entityManager.merge(author));
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        String hql = "SELECT DISTINCT coAuthor FROM Book b " +
                "JOIN b.authors coAuthor " +
                "WHERE b IN (SELECT b FROM Book b JOIN b.authors a WHERE a.authorNumber = :authorNumber) " +
                "AND coAuthor.authorNumber <> :authorNumber";
        TypedQuery<Author> query = entityManager.createQuery(hql, Author.class);
        query.setParameter("authorNumber", authorNumber);
        return query.getResultList();
    }
}
