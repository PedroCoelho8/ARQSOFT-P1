package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorConverter;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AuthorMongoRepositoryImpl implements AuthorRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        Query query = new Query(Criteria.where("authorNumber").is(authorNumber.toString()));
        AuthorMongo authorMongo = mongoTemplate.findOne(query, AuthorMongo.class);
        return Optional.ofNullable(AuthorConverter.toAuthor(authorMongo));
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        Query query = new Query(Criteria.where("name.name").regex("^" + name));
        List<AuthorMongo> authorMongos = mongoTemplate.find(query, AuthorMongo.class);
        return authorMongos.stream().map(AuthorConverter::toAuthor).collect(Collectors.toList());
    }

    @Override
    public List<Author> searchByNameName(String name) {
        Query query = new Query(Criteria.where("name.name").is(name));
        List<AuthorMongo> authorMongos = mongoTemplate.find(query, AuthorMongo.class);
        return authorMongos.stream().map(AuthorConverter::toAuthor).collect(Collectors.toList());
    }

    @Override
    public Author save(Author author) {
        AuthorMongo authorMongo = AuthorConverter.toAuthorMongo(author);
        AuthorMongo savedAuthorMongo = mongoTemplate.save(authorMongo);
        return AuthorConverter.toAuthor(savedAuthorMongo);
    }

    @Override
    public Iterable<Author> findAll() {
        List<AuthorMongo> authorMongos = mongoTemplate.findAll(AuthorMongo.class);
        return authorMongos.stream().map(AuthorConverter::toAuthor).collect(Collectors.toList());
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {
        MatchOperation matchLendings = Aggregation.match(Criteria.where("lendings").exists(true));
        Aggregation aggregation = Aggregation.newAggregation(
                matchLendings,
                Aggregation.unwind("lendings"),
                Aggregation.group("name").count().as("lendingsCount"),
                Aggregation.sort(pageableRules.getSort().isSorted() ? pageableRules.getSort() : Sort.by(Sort.Order.desc("lendingsCount"))),
                Aggregation.skip(pageableRules.getOffset()),
                Aggregation.limit(pageableRules.getPageSize())
        );

        AggregationResults<AuthorLendingView> results = mongoTemplate.aggregate(aggregation, "author", AuthorLendingView.class);
        List<AuthorLendingView> authors = results.getMappedResults();

        return new PageImpl<>(authors, pageableRules, authors.size());
    }

    @Override
    public void delete(Author author) {
        AuthorMongo authorMongo = AuthorConverter.toAuthorMongo(author);
        mongoTemplate.remove(authorMongo);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("books.authors.authorNumber").is(authorNumber.toString())
                .and("authorNumber").ne(authorNumber.toString()));

        List<AuthorMongo> coAuthors = mongoTemplate.find(query, AuthorMongo.class);
        return coAuthors.stream().map(AuthorConverter::toAuthor).collect(Collectors.toList());
    }
}
