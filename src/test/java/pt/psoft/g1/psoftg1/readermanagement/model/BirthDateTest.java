package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BirthDateTest {


    @Test
    void ensureBirthDateCanBeCreatedWithValidDate() {
        assertDoesNotThrow(() -> new BirthDate(2000, 1, 1));
    }
    @Test
    void ensureBirthDateCanBeCreatedWithValidDateMelhorado() {
        // Arrange
        int year = 2000;
        int month = 1;
        int day = 1;

        // Act & Assert
        assertDoesNotThrow(() -> new BirthDate(year, month, day));
    }

    @Test
    void ensureBirthDateCanBeCreatedWithValidStringDate() {
        assertDoesNotThrow(() -> new BirthDate("2000-01-01"));
    }
    @Test
    void ensureBirthDateCanBeCreatedWithValidStringDateMelhorado() {
        // Arrange
        String validDate = "2000-01-01";

        // Act & Assert
        assertDoesNotThrow(() -> new BirthDate(validDate));
    }
    @Test
    void ensureExceptionIsThrownForInvalidStringDateFormat() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new BirthDate("01-01-2000"));
        assertEquals("Provided birth date is not in a valid format. Use yyyy-MM-dd", exception.getMessage());
    }
    @Test
    void ensureExceptionIsThrownForInvalidStringDateFormatMelhorado() {
        // Arrange
        String invalidDate = "01-01-2000";

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new BirthDate(invalidDate));

        // Assert
        assertEquals("Provided birth date is not in a valid format. Use yyyy-MM-dd", exception.getMessage());
    }

    @Test
    void ensureCorrectStringRepresentation() {
        BirthDate birthDate = new BirthDate(2000, 1, 1);
        assertEquals("2000-1-1", birthDate.toString());
    }
    @Test
    void ensureCorrectStringRepresentationMelhorado() {
        // Arrange
        BirthDate birthDate = new BirthDate(2000, 1, 1);

        // Act
        String result = birthDate.toString();

        // Assert
        assertEquals("2000-1-1", result);
    }


    //testes adicionados
    @Test
    void ensureCorrectAgeCalculation() {
        // Arrange
        BirthDate birthDate = new BirthDate(2000, 1, 1);

        // Act
        int age = birthDate.getAge();

        // Assert
        assertEquals(LocalDate.now().getYear() - 2000, age);
    }

    /*@Test
    void ensureExceptionIsThrownForUnderageUser() {
        // Arrange
        LocalDate underageDate = LocalDate.now().minusYears(17);

        // Act
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> new BirthDate(underageDate.getYear(), underageDate.getMonthValue(), underageDate.getDayOfMonth()));

        // Assert
        assertEquals("User must be, at least, 18 years old", exception.getMessage());
    }*/
}
