package pt.psoft.g1.psoftg1.idgeneratormanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorServiceImpl;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class IdGeneratorHexadecimalTest {


    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CreateAuthorRequest createAuthorRequest;

    @Mock
    private MultipartFile photo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateIdLength() {
        IdGeneratorHexadecimal idGenerator = new IdGeneratorHexadecimal();
        String generatedId = idGenerator.generateId();

        assertEquals(24, generatedId.length());
    }

    @Test
    void testGeneratedIdIsHexadecimal() {
        IdGeneratorHexadecimal idGenerator = new IdGeneratorHexadecimal();
        String generatedId = idGenerator.generateId();

        assertTrue(generatedId.matches("[0-9a-fA-F]+"));
    }

    @Test
    void testGenerateMultipleIdsAreUnique() {
        IdGeneratorHexadecimal idGenerator = new IdGeneratorHexadecimal();
        String id1 = idGenerator.generateId();
        String id2 = idGenerator.generateId();

        assertTrue(!id1.equals(id2));
    }

    /*
    @Test
    void testCreateAuthorWithSpecificId() {
        String expectedAuthorId = "1234567890abcdef12345678"; // Exemplo de um ID específico
        String name = "John Doe";
        String bio = "Author bio";
        String photoURI = "http://example.com/photo.jpg";

        // Configura o comportamento do idGenerator
        when(idGenerator.generateId()).thenReturn(expectedAuthorId);


        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest(name, bio, null, null); // null para MultipartFile

        // Cria um autor para simular a persistência
        Author authorToCreate = new Author(expectedAuthorId, name, bio, photoURI);

        // Configura o comportamento do authorRepository para simular a persistência
        when(authorRepository.save(authorToCreate)).thenReturn(authorToCreate);


        Author createdAuthor = authorService.create(createAuthorRequest); // Passa CreateAuthorRequest

        // Verifica se o authorId gerado é o esperado
        assertEquals(expectedAuthorId, createdAuthor.getAuthorId(), "The author ID should match the expected ID.");
    }

     */
}
