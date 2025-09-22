package pt.psoft.g1.psoftg1.recommendationmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.recommendationmanagement.algorithms.AlRecommendationByAge;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlRecommendationByAgeTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private ReaderRepository readerRepository;

    @InjectMocks
    private AlRecommendationByAge recommendationByAge;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(recommendationByAge, "numrecomendacoes", 5);
        ReflectionTestUtils.setField(recommendationByAge, "minAge", 10);
        ReflectionTestUtils.setField(recommendationByAge, "maxAge", 18);
    }


    @Test
    void testGetLendingRecommendations_InvalidReaderNumberFormat() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> recommendationByAge.getLendingRecommendations("invalid"));
    }

    @Test
    void testGetLendingRecommendations_ReaderNotFound() {
        // Arrange
        when(readerRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> recommendationByAge.getLendingRecommendations("1"));
    }
}