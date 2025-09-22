package pt.psoft.g1.psoftg1.idgeneratormanagement.factories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.psoft.g1.psoftg1.idgeneratormanagement.IdGenerator;
import pt.psoft.g1.psoftg1.idgeneratormanagement.IdGeneratorAlphanumeric;
import pt.psoft.g1.psoftg1.idgeneratormanagement.IdGeneratorHexadecimal;

@Configuration
public class IdGeneratorFactory {

    @Value("${id.generator.strategy}")
    private String idGeneratorStrategy;

    @Bean
    public IdGenerator createIdGenerator() {
        return switch (idGeneratorStrategy.toLowerCase()) {
            case "hexadecimal" -> new IdGeneratorHexadecimal();
            case "alphanumeric" -> new IdGeneratorAlphanumeric();
            default -> throw new IllegalArgumentException("Invalid ID generator strategy: " + idGeneratorStrategy);
        };
    }
}
