package pt.psoft.g1.psoftg1.recommendationmanagement;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.recommendationmanagement.algorithms.AlRecommendationMostLentBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AlRecommendationMostLentBookTest {

    @InjectMocks
    private AlRecommendationMostLentBook recommendationAlgorithm;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    private final long xBooksMostLent = 2;
    private final int yMostLentGenre = 2;

    @BeforeEach
    void setUp() {
        recommendationAlgorithm.setXBooksMostLent(xBooksMostLent);
        recommendationAlgorithm.setYMostLentGenre(yMostLentGenre);
    }

    @Test
    void testGetLendingRecommendations() {

        GenreBookCountDTO genre1 = new GenreBookCountDTO("Fantasy", 10);
        GenreBookCountDTO genre2 = new GenreBookCountDTO("Sci-Fi", 8);
        GenreBookCountDTO genre3 = new GenreBookCountDTO("Mystery", 5);
        List<GenreBookCountDTO> genres = Arrays.asList(genre1, genre2, genre3);

        Page<GenreBookCountDTO> genrePage = new PageImpl<>(genres);
        when(genreRepository.findTopGenresByLendCount(yMostLentGenre, Pageable.ofSize(yMostLentGenre)))
                .thenReturn(genrePage);


        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepository.findTopLentBooksByGenre("Fantasy", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Arrays.asList(book1, book2));
        when(bookRepository.findTopLentBooksByGenre("Sci-Fi", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Arrays.asList(book1));
        when(bookRepository.findTopLentBooksByGenre("Mystery", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Arrays.asList());


        List<Book> recommendations = recommendationAlgorithm.getLendingRecommendations("reader123");


        assertEquals(3, recommendations.size());
        assertTrue(recommendations.contains(book1));
        assertTrue(recommendations.contains(book2));
        assertFalse(recommendations.contains(new Book()));
    }

    @Test
    void testNoGenresAvailable() {
        Page<GenreBookCountDTO> emptyGenrePage = new PageImpl<>(Collections.emptyList());
        when(genreRepository.findTopGenresByLendCount(yMostLentGenre, Pageable.ofSize(yMostLentGenre)))
                .thenReturn(emptyGenrePage);

        List<Book> recommendations = recommendationAlgorithm.getLendingRecommendations("reader123");

        assertTrue(recommendations.isEmpty());
    }

    @Test
    void testGenresWithNoBooks() {
        // Setup mock genre data
        GenreBookCountDTO genre1 = new GenreBookCountDTO("Fantasy", 10);
        GenreBookCountDTO genre2 = new GenreBookCountDTO("Sci-Fi", 8);
        List<GenreBookCountDTO> genres = Arrays.asList(genre1, genre2);

        Page<GenreBookCountDTO> genrePage = new PageImpl<>(genres);
        when(genreRepository.findTopGenresByLendCount(yMostLentGenre, Pageable.ofSize(yMostLentGenre)))
                .thenReturn(genrePage);

        // Setup mock book data to return empty list for Sci-Fi
        when(bookRepository.findTopLentBooksByGenre("Fantasy", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Arrays.asList(new Book()));
        when(bookRepository.findTopLentBooksByGenre("Sci-Fi", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Collections.emptyList());

        // Call the method under test
        List<Book> recommendations = recommendationAlgorithm.getLendingRecommendations("reader123");

        // Verify results
        assertEquals(1, recommendations.size());
        assertTrue(recommendations.stream().anyMatch(book -> book != null)); // Assuming at least one book is not null
    }

    @Test
    void testSingleGenre() {
        // Setup mock genre data with a single genre
        GenreBookCountDTO genre = new GenreBookCountDTO("Fantasy", 10);
        List<GenreBookCountDTO> genres = Collections.singletonList(genre);

        Page<GenreBookCountDTO> genrePage = new PageImpl<>(genres);
        when(genreRepository.findTopGenresByLendCount(yMostLentGenre, Pageable.ofSize(yMostLentGenre)))
                .thenReturn(genrePage);

        // Setup mock book data
        Book book1 = new Book();
        when(bookRepository.findTopLentBooksByGenre("Fantasy", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Arrays.asList(book1));

        // Call the method under test
        List<Book> recommendations = recommendationAlgorithm.getLendingRecommendations("reader123");

        // Verify results
        assertEquals(1, recommendations.size());
        assertTrue(recommendations.contains(book1));
    }

    @Test
    void testLimitBooksReturned() {
        // Setup mock genre data
        GenreBookCountDTO genre = new GenreBookCountDTO("Fantasy", 10);
        List<GenreBookCountDTO> genres = Collections.singletonList(genre);

        Page<GenreBookCountDTO> genrePage = new PageImpl<>(genres);
        when(genreRepository.findTopGenresByLendCount(yMostLentGenre, Pageable.ofSize(yMostLentGenre)))
                .thenReturn(genrePage);

        // Setup mock book data with more books than the limit
        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepository.findTopLentBooksByGenre("Fantasy", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Arrays.asList(book1, book2));

        // Call the method under test
        List<Book> recommendations = recommendationAlgorithm.getLendingRecommendations("reader123");

        // Verify results
        assertEquals(2, recommendations.size()); // Expecting both books returned
        assertTrue(recommendations.contains(book1));
        assertTrue(recommendations.contains(book2));
    }

    @Test
    void testEqualBooksToLimit() {
        // Testa o caso onde o número de livros emprestados é igual ao limite.
        GenreBookCountDTO genre = new GenreBookCountDTO("Fantasy", 10);
        List<GenreBookCountDTO> genres = Collections.singletonList(genre);

        Page<GenreBookCountDTO> genrePage = new PageImpl<>(genres);
        when(genreRepository.findTopGenresByLendCount(yMostLentGenre, Pageable.ofSize(yMostLentGenre)))
                .thenReturn(genrePage);

        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepository.findTopLentBooksByGenre("Fantasy", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Arrays.asList(book1, book2));

        List<Book> recommendations = recommendationAlgorithm.getLendingRecommendations("reader123");

        assertEquals(2, recommendations.size()); // Espera-se que ambos os livros sejam retornados
        assertTrue(recommendations.contains(book1));
        assertTrue(recommendations.contains(book2));
    }

    @Test
    void testSameLendCountGenres() {
        // Testa o caso onde dois gêneros têm a mesma contagem de empréstimos.
        GenreBookCountDTO genre1 = new GenreBookCountDTO("Fantasy", 10);
        GenreBookCountDTO genre2 = new GenreBookCountDTO("Sci-Fi", 10);
        List<GenreBookCountDTO> genres = Arrays.asList(genre1, genre2);

        Page<GenreBookCountDTO> genrePage = new PageImpl<>(genres);
        when(genreRepository.findTopGenresByLendCount(yMostLentGenre, Pageable.ofSize(yMostLentGenre)))
                .thenReturn(genrePage);

        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepository.findTopLentBooksByGenre("Fantasy", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Arrays.asList(book1));
        when(bookRepository.findTopLentBooksByGenre("Sci-Fi", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Arrays.asList(book2));

        List<Book> recommendations = recommendationAlgorithm.getLendingRecommendations("reader123");

        assertEquals(2, recommendations.size()); // Espera-se um livro de cada gênero
        assertTrue(recommendations.contains(book1));
        assertTrue(recommendations.contains(book2));
    }

    @Test
    void testMixedGenresWithNoBooks() {
        // Testa a combinação de gêneros com livros e sem livros.
        GenreBookCountDTO genre1 = new GenreBookCountDTO("Fantasy", 10);
        GenreBookCountDTO genre2 = new GenreBookCountDTO("Sci-Fi", 8);
        GenreBookCountDTO genre3 = new GenreBookCountDTO("Mystery", 5);
        List<GenreBookCountDTO> genres = Arrays.asList(genre1, genre2, genre3);

        Page<GenreBookCountDTO> genrePage = new PageImpl<>(genres);
        when(genreRepository.findTopGenresByLendCount(yMostLentGenre, Pageable.ofSize(yMostLentGenre)))
                .thenReturn(genrePage);

        when(bookRepository.findTopLentBooksByGenre("Fantasy", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Arrays.asList(new Book()));
        when(bookRepository.findTopLentBooksByGenre("Sci-Fi", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Collections.emptyList());
        when(bookRepository.findTopLentBooksByGenre("Mystery", Pageable.ofSize((int) xBooksMostLent)))
                .thenReturn(Collections.emptyList());

        List<Book> recommendations = recommendationAlgorithm.getLendingRecommendations("reader123");

        assertEquals(1, recommendations.size());
        assertTrue(recommendations.stream().anyMatch(book -> book != null)); // Deve retornar o único livro de Fantasy
    }

}
