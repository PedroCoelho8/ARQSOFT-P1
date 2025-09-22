package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ReaderTest {
    private Reader mockReader ;
    private List<Genre> genreList ;
    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        genreList  = new ArrayList<>();
    }

    @Test
    void ensureValidReaderDetailsAreCreated() {
        Reader mockReader = mock(Reader.class);
        assertDoesNotThrow(() -> new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,null, null));
    }

    @Test
    void ensureValidReaderDetailsAreCreatedMelhorado() {
        // Arrange
        int readerNumber = 123;
        String birthDate = "2010-01-01";
        String phoneNumber = "912345678";
        boolean gdprConsent = true;
        boolean marketingConsent = false;
        boolean thirdPartySharingConsent = false;
        String photoURI = null;

        // Act & Assert
        assertDoesNotThrow(() -> new ReaderDetails(readerNumber, mockReader, birthDate, phoneNumber, gdprConsent, marketingConsent, thirdPartySharingConsent, photoURI, genreList));
    }

    @Test
    void ensureExceptionIsThrownForNullReader() {
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, null, "2010-01-01", "912345678", true, false, false,null,null));
    }
    @Test
    void ensureExceptionIsThrownForNullReaderMelhorado() {
        // Arrange
        int readerNumber = 123;
        String birthDate = "2010-01-01";
        String phoneNumber = "912345678";
        boolean gdprConsent = true;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(readerNumber, null, birthDate, phoneNumber, gdprConsent, false, false, null, genreList));
        assertEquals("Provided argument resolves to null object", exception.getMessage());
    }

    @Test
    void ensureExceptionIsThrownForNullPhoneNumber() {
        Reader mockReader = mock(Reader.class);
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, mockReader, "2010-01-01", null, true, false, false,null,null));
    }
    @Test
    void ensureExceptionIsThrownForNullPhoneNumberMelhorado() {
        // Arrange
        int readerNumber = 123;
        String birthDate = "2010-01-01";
        boolean gdprConsent = true;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(readerNumber, mockReader, birthDate, null, gdprConsent, false, false, null, genreList));
        assertEquals("Provided argument resolves to null object", exception.getMessage());
    }


    @Test
    void ensureExceptionIsThrownForNoGdprConsent() {
        Reader mockReader = mock(Reader.class);
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, mockReader, "2010-01-01", "912345678", false, false, false,null,null));
    }
    @Test
    void ensureExceptionIsThrownForNoGdprConsentMelhorado() {
        // Arrange
        int readerNumber = 123;
        String birthDate = "2010-01-01";
        String phoneNumber = "912345678";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(readerNumber, mockReader, birthDate, phoneNumber, false, false, false, null, genreList));
        assertEquals("Readers must agree with the GDPR rules", exception.getMessage());
    }



    @Test
    void ensureGdprConsentIsTrue() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,null,null);
        assertTrue(readerDetails.isGdprConsent());
    }
    @Test
    void ensureGdprConsentIsTrueMelhorado() {
        // Arrange
        int readerNumber = 123;
        String birthDate = "2010-01-01";
        String phoneNumber = "912345678";
        boolean gdprConsent = true;

        // Act
        ReaderDetails readerDetails = new ReaderDetails(readerNumber, mockReader, birthDate, phoneNumber, gdprConsent, false, false, null, genreList);

        // Assert
        assertTrue(readerDetails.isGdprConsent());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,null,null);
        assertNull(readerDetails.getPhoto());
    }
    @Test
    void ensurePhotoCanBeNull_AkaOptionalMelhorada() {
        // Arrange
        int readerNumber = 123;
        String birthDate = "2010-01-01";
        String phoneNumber = "912345678";
        boolean gdprConsent = true;

        // Act
        ReaderDetails readerDetails = new ReaderDetails(readerNumber, mockReader, birthDate, phoneNumber, gdprConsent, false, false, null, genreList);

        // Assert
        assertNull(readerDetails.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",null);
        Photo photo = readerDetails.getPhoto();

        //This is here to force the test to fail if the photo is null
        assertNotNull(photo);

        String readerPhoto = photo.getPhotoFile();
        assertEquals("readerPhotoTest.jpg", readerPhoto);
    }


    @Test
    void ensureInterestListCanBeNullOrEmptyList_AkaOptional() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetailsNullInterestList = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",null);
        assertNull(readerDetailsNullInterestList.getInterestList());

        ReaderDetails readerDetailsInterestListEmpty = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",new ArrayList<>());
        assertEquals(0, readerDetailsInterestListEmpty.getInterestList().size());
    }

    @Test
    void ensureInterestListCanBeNullOrEmptyList_AkaOptionalMelhorado() {
        // Arrange
        int readerNumber = 123;
        String birthDate = "2010-01-01";
        String phoneNumber = "912345678";
        boolean gdprConsent = true;

        // Act
        ReaderDetails readerDetailsNullInterestList = new ReaderDetails(readerNumber, mockReader, birthDate, phoneNumber, gdprConsent, false, false, "readerPhotoTest.jpg", null);
        ReaderDetails readerDetailsInterestListEmpty = new ReaderDetails(readerNumber, mockReader, birthDate, phoneNumber, gdprConsent, false, false, "readerPhotoTest.jpg", new ArrayList<>());

        // Assert
        assertNull(readerDetailsNullInterestList.getInterestList());
        assertEquals(0, readerDetailsInterestListEmpty.getInterestList().size());
    }

    @Test
    void ensureInterestListCanTakeAnyValidGenre() {
        Reader mockReader = mock(Reader.class);
        Genre g1 = new Genre("genre1");
        Genre g2 = new Genre("genre2");
        List<Genre> genreList = new ArrayList<>();
        genreList.add(g1);
        genreList.add(g2);

        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",genreList);
        assertEquals(2, readerDetails.getInterestList().size());
    }
    @Test
    void ensureInterestListCanTakeAnyValidGenreMelhorado() {
        // Arrange
        int readerNumber = 123;
        String birthDate = "2010-01-01";
        String phoneNumber = "912345678";
        boolean gdprConsent = true;
        Genre g1 = new Genre("genre1");
        Genre g2 = new Genre("genre2");
        genreList.add(g1);
        genreList.add(g2);

        // Act
        ReaderDetails readerDetails = new ReaderDetails(readerNumber, mockReader, birthDate, phoneNumber, gdprConsent, false, false, "readerPhotoTest.jpg", genreList);

        // Assert
        assertEquals(2, readerDetails.getInterestList().size());
    }





}
