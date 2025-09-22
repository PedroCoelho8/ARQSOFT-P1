package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class TitleMongo {

    @Field("title")
    private String title;

    public TitleMongo(String title) {
        this.title = title;
    }

    protected TitleMongo() {}

}