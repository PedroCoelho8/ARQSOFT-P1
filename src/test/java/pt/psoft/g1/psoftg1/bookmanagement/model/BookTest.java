package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    private final String validIsbn = "9782826012092";
    private final String validTitle = "Encantos de contar";
    private final Author validAuthor1 = new Author(null,"João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);
    private final Author validAuthor2 = new Author(null,"Maria José", "A Maria José nasceu em Viseu e só come laranjas às segundas feiras.", null);
    private final Genre validGenre = new Genre("Fantasia");
    private ArrayList<Author> authors = new ArrayList<>();

    @BeforeEach
    void setUp(){
        authors.clear();
    }

    @Test
    void ensureIsbnNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(null, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureIsbnNotNullMelhorado() {
        // Arrange
        authors.add(validAuthor1);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Book(null, validTitle, null, validGenre, authors, null));

        // Verifica se a mensagem de erro é a esperada
        assertEquals("Isbn cannot be null", exception.getMessage());
    }

    @Test
    void ensureTitleNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, null, null, validGenre, authors, null));
    }

    @Test
    void ensureTitleNotNullMelhorado() {
        // Arrange
        authors.add(validAuthor1);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Book(validIsbn, null, null, validGenre, authors, null));

        // Verifica se a mensagem de erro é a esperada
        assertEquals("Title cannot be null", exception.getMessage());
    }

    @Test
    void ensureGenreNotNull(){
        authors.add(validAuthor1);
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null,null, authors, null));
    }

    @Test
    void ensureGenreNotNullMelhorado() {
        // Arrange
        authors.add(validAuthor1);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Book(validIsbn, validTitle, null, null, authors, null));

        // Verifica se a mensagem de erro é a esperada
        assertEquals("Genre cannot be null", exception.getMessage());
    }


    @Test
    void ensureGenreMustNotBeOversizeMelhorado() {
        // Arrange
        String longGenre = "a".repeat(4097); // Create a string with 4097 characters

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Genre(longGenre));

        // Verify if the error message is as expected
        assertEquals("Genre has a maximum of 4096 characters", exception.getMessage());
    }



    @Test
    void ensureAuthorsNotEmpty(){
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureAuthorsNotEmptyMelhorado() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Book(validIsbn, validTitle, null, validGenre, authors, null));

        // Verifica se a mensagem de erro é a esperada
        assertEquals("Author list is empty", exception.getMessage());
    }


    @Test
    void ensureBookCreatedWithMultipleAuthors() {
        authors.add(validAuthor1);
        authors.add(validAuthor2);
        assertDoesNotThrow(() -> new Book(validIsbn, validTitle, null, validGenre, authors, null));
    }

    @Test
    void ensureBookCreatedWithMultipleAuthorsMelhorado() {
        // Arrange
        authors.add(validAuthor1);
        authors.add(validAuthor2);

        // Act
        Book book = new Book(validIsbn, validTitle, null, validGenre, authors, null);

        // Assert
        assertNotNull(book); // Verifica se o livro foi criado
        assertEquals(2, book.getAuthors().size()); // Verifica se o livro tem o número correto de autores
        assertTrue(book.getAuthors().contains(validAuthor1)); // Verifica se o autor 1 está na lista
        assertTrue(book.getAuthors().contains(validAuthor2)); // Verifica se o autor 2 está na lista
    }


    //Novos testes


    @Test
    void ensureBookCreatedWithDescription() {
        // Arrange
        authors.add(validAuthor1);
        String description = "Uma história mágica.";

        // Act
        Book book = new Book(validIsbn, validTitle, description, validGenre, authors, null);

        // Assert
        assertNotNull(book);
        assertEquals(description, book.getDescription()); // Verifica se a descrição foi setada corretamente
    }





    @Test
    void whenAuthorsListHasOneAuthor_thenBookCreatedSuccessfully() {
        // Arrange
        authors.add(validAuthor1);

        // Act
        Book book = new Book(validIsbn, validTitle, null, validGenre, authors, null);

        // Assert
        assertNotNull(book);
        assertEquals(1, book.getAuthors().size());
        assertTrue(book.getAuthors().contains(validAuthor1));
    }



}