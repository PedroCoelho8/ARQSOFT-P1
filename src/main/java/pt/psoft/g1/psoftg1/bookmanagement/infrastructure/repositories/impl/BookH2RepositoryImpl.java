package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BookH2RepositoryImpl implements BookRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Book> findByGenre(String genre) {
        String bookQuery = "FROM Book b WHERE b.genre.genre LIKE :genre";
        TypedQuery<Book> query = entityManager.createQuery(bookQuery, Book.class);
        query.setParameter("genre", "%" + genre + "%");
        return query.getResultList();
    }


    @Override
    public List<Book> findByTitle(String title) {
        String bookQuery = "FROM Book b WHERE b.title.title LIKE :title";
        TypedQuery<Book> query = entityManager.createQuery(bookQuery, Book.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        String bookQuery = "SELECT b FROM Book b JOIN b.authors a WHERE a.name.name LIKE :authorName";
        TypedQuery<Book> query = entityManager.createQuery(bookQuery, Book.class);
        query.setParameter("authorName", "%" + authorName + "%");
        return query.getResultList();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        String bookQuery = "FROM Book b WHERE b.isbn.isbn = :isbn";
        TypedQuery<Book> query = entityManager.createQuery(bookQuery, Book.class);
        query.setParameter("isbn", isbn);
        return query.getResultList().stream().findFirst();
    }

    //Por implementar
    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        String bookCountQuery = "SELECT new pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO(b, COUNT(l)) " +
                "FROM Book b " +
                "JOIN Lending l ON l.book = b " +
                "WHERE l.startDate > :oneYearAgo " +
                "GROUP BY b " +
                "ORDER BY COUNT(l) DESC";

        TypedQuery<BookCountDTO> query = entityManager.createQuery(bookCountQuery, BookCountDTO.class);
        query.setParameter("oneYearAgo", oneYearAgo);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<BookCountDTO> resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, resultList.size());
    }


    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        String bookQuery = "SELECT b FROM Book b JOIN b.authors a WHERE a.authorNumber = :authorNumber";
        TypedQuery<Book> query = entityManager.createQuery(bookQuery, Book.class);
        query.setParameter("authorNumber", authorNumber);
        return query.getResultList();
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        String title = query.getTitle();
        String genre = query.getGenre();
        String authorName = query.getAuthorName();

        // Initialize CriteriaBuilder and CriteriaQuery
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);

        // Joins for Genre and Author
        Join<Book, Genre> genreJoin = root.join("genre");
        Join<Book, Author> authorJoin = root.join("authors");

        // Select the root (Book) as the result type
        cq.select(root);

        // Create conditions (Predicates) based on the query parameters
        List<Predicate> where = new ArrayList<>();
        if (title != null && !title.isEmpty()) {
            where.add(cb.like(root.get("title"), title + "%"));
        }
        if (genre != null && !genre.isEmpty()) {
            where.add(cb.like(genreJoin.get("genre"), genre + "%"));
        }
        if (authorName != null && !authorName.isEmpty()) {
            where.add(cb.like(authorJoin.get("name"), authorName + "%"));
        }

        // Apply where conditions and order by title
        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("title")));

        // Create query and set pagination
        TypedQuery<Book> typedQuery = entityManager.createQuery(cq);
        typedQuery.setFirstResult((page.getNumber() - 1) * page.getLimit());
        typedQuery.setMaxResults(page.getLimit());

        return typedQuery.getResultList();
    }



    @Override
    @Transactional
    public Book save(Book book) {
        if (book.getIsbn() == null) {
            entityManager.persist(book);
        } else {
            entityManager.merge(book);
        }
        return book;
    }

    @Override
    public void delete(Book book) {
        if (entityManager.contains(book)) {
            entityManager.remove(book);
        } else {
            entityManager.remove(entityManager.merge(book));
        }
    }

    @Override
    public List<Book> findTopLentBooksByGenre(String genre, Pageable pageable) {
        String sql = "SELECT b FROM Book b JOIN Lending l ON b.pk = l.book.pk " +
                "WHERE b.genre.genre = :genre " +
                "GROUP BY b.pk " +
                "ORDER BY COUNT(l) DESC";
        TypedQuery<Book> query = entityManager.createQuery(sql, Book.class);
        query.setParameter("genre", genre);
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

}
