package pt.psoft.g1.psoftg1.authormanagement.model;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorMongoTest {

    private AuthorMongo authorMongo;
    private final String validName = "Test Author";
    private final String validBio = "This is a bio.";
    private final String photoURI = "photo.jpg";

    @BeforeEach
    void setUp() {
        // Arrange - Setup initial state for each test
        authorMongo = spy(new AuthorMongo(validName, validBio, photoURI));
    }

    @Test
    void testSetName() {
        // Arrange
        String newName = "New Author Name";

        // Act
        authorMongo.setName(newName);

        // Assert
        assertEquals(newName, authorMongo.getName());
    }

    @Test
    void testSetBio() {
        // Arrange
        String newBio = "New Bio Content";

        // Act
        authorMongo.setBio(newBio);

        // Assert
        assertEquals(newBio, authorMongo.getBio());
    }

    @Test
    void testGetVersion() {
        // Assert
        assertNull(authorMongo.getVersion(), "Initial version should be null");
    }


    @Test
    void testGetName() {
        // Act
        String name = authorMongo.getName();

        // Assert
        assertEquals(validName, name);
    }

    @Test
    void testGetBio() {
        // Act
        String bio = authorMongo.getBio();

        // Assert
        assertEquals(validBio, bio);
    }
}
