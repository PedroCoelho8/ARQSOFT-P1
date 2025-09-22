package pt.psoft.g1.psoftg1.genremanagement.services;

import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class GenreServiceImplIT {

    @Mock
    private GenreRepository genreRepo;

    @InjectMocks
    private GenreServiceImpl genreService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnGenreWhenExists() {
        // Arrange
        String genreName = "Fantasy";
        Genre genre = new Genre(genreName);
        when(genreRepo.findByString(genreName)).thenReturn(Optional.of(genre));

        // Act
        Optional<Genre> result = genreService.findByString(genreName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(genreName, result.get().toString());
    }

    @Test
    void shouldReturnAllGenres() {
        // Arrange
        Genre genre = new Genre("Science Fiction");
        when(genreRepo.findAll()).thenReturn(List.of(genre));

        // Act
        Iterable<Genre> result = genreService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
        assertEquals("Science Fiction", result.iterator().next().toString());
    }

    @Test
    void shouldReturnTopGenresByBookCount() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5);
        GenreBookCountDTO genreStats = new GenreBookCountDTO("Fantasy", 100);
        Page<GenreBookCountDTO> page = new PageImpl<>(List.of(genreStats));
        when(genreRepo.findTop5GenreByBookCount(pageable)).thenReturn(page);

        // Act
        List<GenreBookCountDTO> result = genreService.findTopGenreByBooks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(genreStats, result.get(0));
        assertEquals(100, result.get(0).getBookCount());
    }

    @Test
    void shouldSaveNewGenre() {
        // Arrange
        Genre genre = new Genre("Romance");
        when(genreRepo.save(genre)).thenReturn(genre);

        // Act
        Genre result = genreService.save(genre);

        // Assert
        assertNotNull(result);
        assertEquals("Romance", result.toString());
    }

    @Test
    void shouldGetLendingsPerMonthLastYear() {
        // Arrange
        GenreLendingsDTO lendingDTO = new GenreLendingsDTO("Fantasy", 50L);
        GenreLendingsPerMonthDTO monthlyLendings = new GenreLendingsPerMonthDTO(2023, 8, List.of(lendingDTO));

        when(genreRepo.getLendingsPerMonthLastYearByGenre()).thenReturn(List.of(monthlyLendings));

        // Act
        List<GenreLendingsPerMonthDTO> result = genreService.getLendingsPerMonthLastYearByGenre();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2023, result.get(0).getYear());
        assertEquals(8, result.get(0).getMonth());
        assertEquals("Fantasy", result.get(0).getValues().get(0).getGenre());
        assertEquals(50L, result.get(0).getValues().get(0).getValue());
    }

    @Test
    void shouldGetAverageLendingsForGivenMonth() {
        // Arrange
        GetAverageLendingsQuery query = new GetAverageLendingsQuery(2023, 8);
        pt.psoft.g1.psoftg1.shared.services.Page page = new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);
        GenreLendingsDTO dto = new GenreLendingsDTO("Horror", 5.0);
        when(genreRepo.getAverageLendingsInMonth(any(LocalDate.class), any(pt.psoft.g1.psoftg1.shared.services.Page.class)))
                .thenReturn(List.of(dto));

        // Act
        List<GenreLendingsDTO> result = genreService.getAverageLendings(query, page);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        assertEquals(5.0, result.get(0).getValue());
    }

    @Test
    void shouldCalculateAverageLendingDurationPerMonth() {
        // Arrange
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 12, 31);
        GenreLendingsDTO lendingDTO = new GenreLendingsDTO("Fantasy", 30.0);
        GenreLendingsPerMonthDTO monthlyLendings = new GenreLendingsPerMonthDTO(2023, 1, List.of(lendingDTO));

        when(genreRepo.getLendingsAverageDurationPerMonth(start, end)).thenReturn(List.of(monthlyLendings));

        // Act
        List<GenreLendingsPerMonthDTO> result = genreService.getLendingsAverageDurationPerMonth("2023-01-01", "2023-12-31");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2023, result.get(0).getYear());
        assertEquals(1, result.get(0).getMonth());
        assertEquals("Fantasy", result.get(0).getValues().get(0).getGenre());
        assertEquals(30.0, result.get(0).getValues().get(0).getValue());
    }

    @Test
    void shouldThrowExceptionForInvalidDateRange() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                genreService.getLendingsAverageDurationPerMonth("2023-12-31", "2023-01-01")
        );
    }

    @Test
    void shouldThrowNotFoundExceptionForNoResults() {
        // Arrange
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 12, 31);
        when(genreRepo.getLendingsAverageDurationPerMonth(start, end)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(NotFoundException.class, () ->
                genreService.getLendingsAverageDurationPerMonth("2023-01-01", "2023-12-31")
        );
    }
}
