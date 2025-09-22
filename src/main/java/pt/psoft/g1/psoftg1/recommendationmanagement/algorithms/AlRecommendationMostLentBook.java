package pt.psoft.g1.psoftg1.recommendationmanagement.algorithms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@Component("TopLentAlg")
public class AlRecommendationMostLentBook implements AlRecommendation {

    @Value("${suggestionsLimitPerGenre}")
    private long xBooksMostLent;

    @Value("${topGenresLimit}")
    private int yMostLentGenre;

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    public AlRecommendationMostLentBook(BookRepository bookRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }

    public void setXBooksMostLent(long xBooksMostLent) {
        this.xBooksMostLent = xBooksMostLent;
    }

    public void setYMostLentGenre(int yMostLentGenre) {
        this.yMostLentGenre = yMostLentGenre;
    }

    @Override
    public List<Book> getLendingRecommendations(String readerNumber) {
        Pageable topGenresPageable = PageRequest.of(0, yMostLentGenre);
        Page<GenreBookCountDTO> topGenres = genreRepository.findTopGenresByLendCount(yMostLentGenre, topGenresPageable);

        List<Book> recommendations = new ArrayList<>();

        for (GenreBookCountDTO genre : topGenres.getContent()) {
            Pageable booksPageable = PageRequest.of(0, (int) xBooksMostLent);
            List<Book> mostLentBooks = bookRepository.findTopLentBooksByGenre(genre.getGenre(), booksPageable);

            recommendations.addAll(mostLentBooks);
        }

        return recommendations;
    }
}
