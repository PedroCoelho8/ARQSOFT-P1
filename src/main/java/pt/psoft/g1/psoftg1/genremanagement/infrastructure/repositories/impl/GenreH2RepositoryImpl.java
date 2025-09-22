package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

import java.time.LocalDate;
import java.util.*;


public class GenreH2RepositoryImpl implements GenreRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Iterable<Genre> findAll() {
        String genreQuery = "FROM Genre";
        TypedQuery<Genre> query = entityManager.createQuery(genreQuery, Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findByString(String genreName) {
        String genreQuery = "FROM Genre g WHERE g.genre = :genreName";
        TypedQuery<Genre> query = entityManager.createQuery(genreQuery, Genre.class);
        query.setParameter("genreName", genreName);
        return query.getResultList().stream().findFirst();
    }



    @Override
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {
        String sql = "SELECT new pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO(g.genre, COUNT(b)) " +
                "FROM Genre g JOIN Book b ON g.pk = b.genre.pk " +
                "GROUP BY g.genre ORDER BY COUNT(b) DESC";
        TypedQuery<GenreBookCountDTO> query = entityManager.createQuery(sql, GenreBookCountDTO.class);
        List<GenreBookCountDTO> results = query.setMaxResults(5).getResultList();
        return new PageImpl<>(results, pageable, results.size());
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, pt.psoft.g1.psoftg1.shared.services.Page page) {
        // Calculate the first and last days of the month
        int daysInMonth = month.lengthOfMonth();
        LocalDate firstOfMonth = LocalDate.of(month.getYear(), month.getMonth(), 1);
        LocalDate lastOfMonth = LocalDate.of(month.getYear(), month.getMonth(), daysInMonth);

        // Set up the CriteriaBuilder and CriteriaQuery
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GenreLendingsDTO> cq = cb.createQuery(GenreLendingsDTO.class);

        // Define the root and joins
        Root<Lending> lendingRoot = cq.from(Lending.class);
        Join<Lending, Book> bookJoin = lendingRoot.join("book", JoinType.LEFT);
        Join<Book, Genre> genreJoin = bookJoin.join("genre", JoinType.LEFT);

        // Define the fields for the average lending calculation
        Expression<Long> lendingsCount = cb.count(lendingRoot.get("pk"));
        Expression<Number> dailyAvgLendings = cb.quot(cb.toDouble(lendingsCount), cb.literal(daysInMonth));

        // Specify the selection fields and grouping by genre
        cq.multiselect(genreJoin.get("genre"), dailyAvgLendings);
        cq.groupBy(genreJoin.get("genre"));

        // Create date range predicates for the specified month
        Predicate startDatePredicate = cb.greaterThanOrEqualTo(lendingRoot.get("startDate"), firstOfMonth);
        Predicate endDatePredicate = cb.lessThanOrEqualTo(lendingRoot.get("startDate"), lastOfMonth);
        cq.where(cb.and(startDatePredicate, endDatePredicate));

        // Create the query and apply pagination
        TypedQuery<GenreLendingsDTO> query = entityManager.createQuery(cq);
        query.setFirstResult((page.getNumber() - 1) * page.getLimit());
        query.setMaxResults(page.getLimit());

        return query.getResultList();
    }


    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        Root<Lending> lendingRoot = cq.from(Lending.class);
        Join<Lending, Book> bookJoin = lendingRoot.join("book");
        Join<Book, Genre> genreJoin = bookJoin.join("genre");

        // Extract year and month from the start date
        Expression<Integer> yearExpr = cb.function("YEAR", Integer.class, lendingRoot.get("startDate"));
        Expression<Integer> monthExpr = cb.function("MONTH", Integer.class, lendingRoot.get("startDate"));
        Expression<Long> lendingsCount = cb.count(lendingRoot.get("pk"));

        // Select genre, year, month, and count of lendings
        cq.multiselect(genreJoin.get("genre"), yearExpr, monthExpr, lendingsCount);
        cq.groupBy(genreJoin.get("genre"), yearExpr, monthExpr);
        cq.orderBy(cb.asc(yearExpr), cb.asc(monthExpr), cb.asc(genreJoin.get("genre")));

        // Filter lendings for the past year
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        Predicate startDatePredicate = cb.greaterThanOrEqualTo(lendingRoot.get("startDate"), oneYearAgo);
        cq.where(startDatePredicate);

        List<Tuple> results = entityManager.createQuery(cq).getResultList();
        Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedResults = new HashMap<>();

        for (Tuple result : results) {
            String genre = result.get(0, String.class);
            int yearValue = result.get(1, Integer.class);
            int monthValue = result.get(2, Integer.class);
            Long lendingsCountValue = result.get(3, Long.class);
            GenreLendingsDTO genreLendingsDTO = new GenreLendingsDTO(genre, lendingsCountValue.doubleValue());

            groupedResults
                    .computeIfAbsent(yearValue, k -> new HashMap<>())
                    .computeIfAbsent(monthValue, k -> new ArrayList<>())
                    .add(genreLendingsDTO);
        }

        return getGenreLendingsPerMonthDtos(groupedResults);
    }


    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        Root<Lending> lendingRoot = cq.from(Lending.class);
        Join<Lending, Book> bookJoin = lendingRoot.join("book");
        Join<Book, Genre> genreJoin = bookJoin.join("genre");

        // Extract year and month from the start date
        Expression<Integer> yearExpr = cb.function("YEAR", Integer.class, lendingRoot.get("startDate"));
        Expression<Integer> monthExpr = cb.function("MONTH", Integer.class, lendingRoot.get("startDate"));
        Expression<Long> lendingDurationInDays = cb.diff(lendingRoot.get("returnedDate"), lendingRoot.get("startDate"));

        // Convert duration to days and calculate the average
        double nanoSecondsInADay = 86400.0 * 1E9;
        Expression<Number> durationInDays = cb.quot(cb.toDouble(lendingDurationInDays), nanoSecondsInADay);
        Expression<Double> averageDuration = cb.avg(cb.toDouble(durationInDays));

        // Select genre, year, month, and average lending duration
        cq.multiselect(genreJoin.get("genre"), yearExpr, monthExpr, averageDuration);
        cq.groupBy(genreJoin.get("genre"), yearExpr, monthExpr);
        cq.orderBy(cb.asc(yearExpr), cb.asc(monthExpr), cb.asc(genreJoin.get("genre")));

        // Apply date range and non-null returnedDate filters
        Predicate startDatePredicate = cb.greaterThanOrEqualTo(lendingRoot.get("startDate"), startDate);
        Predicate endDatePredicate = cb.lessThanOrEqualTo(lendingRoot.get("startDate"), endDate);
        Predicate returnedPredicate = cb.isNotNull(lendingRoot.get("returnedDate"));
        cq.where(cb.and(startDatePredicate, endDatePredicate, returnedPredicate));

        List<Tuple> results = entityManager.createQuery(cq).getResultList();
        Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedResults = new HashMap<>();

        for (Tuple result : results) {
            String genre = result.get(0, String.class);
            int yearValue = result.get(1, Integer.class);
            int monthValue = result.get(2, Integer.class);
            Double averageDurationValue = result.get(3, Double.class);
            GenreLendingsDTO genreLendingsDTO = new GenreLendingsDTO(genre, averageDurationValue);

            groupedResults
                    .computeIfAbsent(yearValue, k -> new HashMap<>())
                    .computeIfAbsent(monthValue, k -> new ArrayList<>())
                    .add(genreLendingsDTO);
        }

        return getGenreLendingsPerMonthDtos(groupedResults);
    }

    @NotNull
    private List<GenreLendingsPerMonthDTO> getGenreLendingsPerMonthDtos(Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedResults) {
        List<GenreLendingsPerMonthDTO> lendingsPerMonth = new ArrayList<>();
        for (Map.Entry<Integer, Map<Integer, List<GenreLendingsDTO>>> yearEntry : groupedResults.entrySet()) {
            int yearValue = yearEntry.getKey();
            for (Map.Entry<Integer, List<GenreLendingsDTO>> monthEntry : yearEntry.getValue().entrySet()) {
                int monthValue = monthEntry.getKey();
                List<GenreLendingsDTO> values = monthEntry.getValue();
                lendingsPerMonth.add(new GenreLendingsPerMonthDTO(yearValue, monthValue, values));
            }
        }
        return lendingsPerMonth;
    }



    @Override
    @Transactional
    public Genre save(Genre genre) {
        if (genre.pk != 0) {
            entityManager.merge(genre);
        } else {
            entityManager.persist(genre);
        }
        return genre;
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        if (entityManager.contains(genre)) {
            entityManager.remove(genre);
        } else {
            entityManager.remove(entityManager.merge(genre));
        }
    }

    @Override
    public Page<GenreBookCountDTO> findTopGenresByLendCount(int limit, Pageable pageable) {
        String sql = "SELECT new pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO(g.genre, COUNT(l)) " +
                "FROM Genre g JOIN Book b ON g.pk = b.genre.pk JOIN Lending l ON b.pk = l.book.pk " +
                "GROUP BY g.genre ORDER BY COUNT(l) DESC";
        TypedQuery<GenreBookCountDTO> query = entityManager.createQuery(sql, GenreBookCountDTO.class);
        query.setMaxResults(limit);
        List<GenreBookCountDTO> results = query.getResultList();

        // Aqui, você precisa retornar uma Page. Para isso, use PageImpl
        return new PageImpl<>(results, pageable, results.size());
    }



}