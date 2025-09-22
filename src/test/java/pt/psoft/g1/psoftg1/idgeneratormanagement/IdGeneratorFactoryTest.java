package pt.psoft.g1.psoftg1.idgeneratormanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import pt.psoft.g1.psoftg1.idgeneratormanagement.factories.IdGeneratorFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class IdGeneratorFactoryTest {

    @Autowired
    private IdGeneratorFactory idGeneratorFactory;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        // Definindo a propriedade para hexadecimal no primeiro teste
        registry.add("id.generator.strategy", () -> "hexadecimal");
    }

    @Test
    void testHexadecimalIdGeneratorSelection() {
        IdGenerator idGenerator = idGeneratorFactory.createIdGenerator();
        assertTrue(idGenerator instanceof IdGeneratorHexadecimal,
                "Should create a Hexadecimal IdGenerator.");
    }
/*
    @Test
    void testAlphanumericIdGeneratorSelection() {
        // Mudando a propriedade para alphanumeric apenas para este teste
        System.setProperty("id.generator.strategy", "alphanumeric");

        IdGenerator idGenerator = idGeneratorFactory.createIdGenerator();
        assertTrue(idGenerator instanceof IdGeneratorAlphanumeric,
                "Should create an Alphanumeric IdGenerator.");
    }

 */
}
