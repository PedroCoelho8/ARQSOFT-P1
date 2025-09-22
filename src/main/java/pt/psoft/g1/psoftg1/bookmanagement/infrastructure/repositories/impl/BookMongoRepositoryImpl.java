package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookMongo;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookDBMapper;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class BookMongoRepositoryImpl implements BookRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Book> findByGenre(String genre) {
        Query query = new Query();
        query.addCriteria(Criteria.where("genre").regex(".*" + genre + ".*", "i"));
        List<BookMongo> books = mongoTemplate.find(query, BookMongo.class);
        return books.stream().map(BookDBMapper::toEntity).toList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        Query query = new Query(Criteria.where("title.title").regex(title, "i"));
        List<BookMongo> books = mongoTemplate.find(query, BookMongo.class);
        return books.stream().map(BookDBMapper::toEntity).toList();
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        Query query = new Query(Criteria.where("authors.name").regex(authorName, "i"));
        List<BookMongo> books = mongoTemplate.find(query, BookMongo.class);
        return books.stream().map(BookDBMapper::toEntity).toList();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        Query query = new Query(Criteria.where("isbn.isbn").is(isbn));
        BookMongo bookMongo = mongoTemplate.findOne(query, BookMongo.class);
        return Optional.ofNullable(BookDBMapper.toEntity(bookMongo));
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {

        return Page.empty();
    }

    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        Query query = new Query(Criteria.where("authors.authorNumber").is(authorNumber));
        List<BookMongo> books = mongoTemplate.find(query, BookMongo.class);
        return books.stream().map(BookDBMapper::toEntity).toList();
    }



    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        Query mongoQuery = new Query();

        if (query.getTitle() != null) {
            mongoQuery.addCriteria(Criteria.where("title.title").regex(query.getTitle(), "i"));
        }
        if (query.getGenre() != null) {
            mongoQuery.addCriteria(Criteria.where("genre").regex(query.getGenre(), "i"));
        }
        if (query.getAuthorName() != null) {
            mongoQuery.addCriteria(Criteria.where("authors.name").regex(query.getAuthorName(), "i"));
        }

        mongoQuery.skip((page.getNumber() - 1) * page.getLimit());
        mongoQuery.limit(page.getLimit());

        List<BookMongo> bookMongos = mongoTemplate.find(mongoQuery, BookMongo.class);
        return bookMongos.stream().map(BookDBMapper::toEntity).toList();
    }

    @Override
    @Transactional
    public Book save(Book book) {
        BookMongo bookMongo = BookDBMapper.toMongo(book);
        BookMongo savedBookMongo = mongoTemplate.save(bookMongo);
        return BookDBMapper.toEntity(savedBookMongo);
    }

    @Override
    public void delete(Book book) {
        BookMongo bookMongo = BookDBMapper.toMongo(book);
        DeleteResult result = mongoTemplate.remove(bookMongo);
        if (!result.wasAcknowledged()) {
            throw new RuntimeException("Failed to delete book with ID " + book.getIsbn());
        }
    }

    @Override
    public List<Book> findTopLentBooksByGenre(String genre, Pageable pageable) {
        // MongoDB não suporta operações complexas de agregação com JOINs para contagem de empréstimos
        // Implementação omitida para revisão adicional
        return List.of();
    }
}
