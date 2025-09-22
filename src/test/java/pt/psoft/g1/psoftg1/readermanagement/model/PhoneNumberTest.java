package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneNumberTest {
    @Test
    void ensureValidMobilePhoneNumberIsAccepted() {
        assertDoesNotThrow(() -> new PhoneNumber("912345678"));
    }
    @Test
    void ensureValidMobilePhoneNumberIsAcceptedMelhorado() {
        // Arrange
        String validMobileNumber = "912345678";

        // Act & Assert
        assertDoesNotThrow(() -> new PhoneNumber(validMobileNumber));
    }
    /* Teste duplicado?
    @Test
    void ensureValidFixedPhoneNumberIsAccepted() {
        assertDoesNotThrow(() -> new PhoneNumber("212345678"));
    }
    */
    @Test
    void ensureInvalidPhoneNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("12345678")); // Too short
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("00123456789")); // Too long
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("abcdefghij")); // Non-numeric
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("512345678")); // Invalid start digit
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("91234567")); // Too short by one digit
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("21234567")); // Too short by one digit
    }
    @Test
    void ensureInvalidPhoneNumberThrowsExceptionMelhorado() {
        // Arrange
        String tooShortNumber = "12345678";
        String tooLongNumber = "00123456789";
        String nonNumericNumber = "abcdefghij";
        String invalidStartDigitNumber = "512345678";
        String tooShortByOneDigitMobile = "91234567";
        String tooShortByOneDigitFixed = "21234567";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(tooShortNumber)); // Too short
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(tooLongNumber)); // Too long
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(nonNumericNumber)); // Non-numeric
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(invalidStartDigitNumber)); // Invalid start digit
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(tooShortByOneDigitMobile)); // Too short by one digit (mobile)
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(tooShortByOneDigitFixed)); // Too short by one digit (fixed)
    }

    @Test
    void ensureCorrectStringRepresentation() {
        PhoneNumber phoneNumber = new PhoneNumber("912345678");
        assertEquals("912345678", phoneNumber.toString());

        PhoneNumber anotherPhoneNumber = new PhoneNumber("212345678");
        assertEquals("212345678", anotherPhoneNumber.toString());
    }
    @Test
    void ensureCorrectStringRepresentationMelhorado() {
        // Arrange
        String mobileNumber = "912345678";
        String fixedNumber = "212345678";

        // Act
        PhoneNumber phoneNumber = new PhoneNumber(mobileNumber);
        PhoneNumber anotherPhoneNumber = new PhoneNumber(fixedNumber);

        // Assert
        assertEquals(mobileNumber, phoneNumber.toString());
        assertEquals(fixedNumber, anotherPhoneNumber.toString());
    }
    //Testes adicionados
    @Test
    void ensureNullPhoneNumberThrowsException() {
        // Arrange
        String nullNumber = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new PhoneNumber(nullNumber));
    }

    @Test
    void ensureEmptyPhoneNumberThrowsException() {
        // Arrange
        String emptyNumber = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(emptyNumber));
    }

    @Test
    void ensurePhoneNumberWithLeadingWhitespaceThrowsException() {
        // Arrange
        String numberWithLeadingWhitespace = " 912345678";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(numberWithLeadingWhitespace));
    }

    @Test
    void ensurePhoneNumberWithTrailingWhitespaceThrowsException() {
        // Arrange
        String numberWithTrailingWhitespace = "912345678 ";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(numberWithTrailingWhitespace));
    }

    @Test
    void ensurePhoneNumberWithLeadingAndTrailingWhitespaceThrowsException() {
        // Arrange
        String numberWithLeadingAndTrailingWhitespace = " 912345678 ";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(numberWithLeadingAndTrailingWhitespace));
    }

    @Test
    void ensurePhoneNumberWithNonNumericCharactersThrowsException() {
        // Arrange
        String numberWithNonNumericCharacters = "a1234abcd";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(numberWithNonNumericCharacters));
    }
}
