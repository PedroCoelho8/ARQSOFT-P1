package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class DescriptionMongo {

    @Field("description")
    private String description;

    public DescriptionMongo(String description) {
        this.description = description;
    }

    protected DescriptionMongo() {}

}