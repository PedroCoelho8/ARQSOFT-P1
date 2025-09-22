package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class IsbnMongo {

    @Field("isbn")
    private String isbn;

    public IsbnMongo(String isbn) {
        this.isbn = isbn;
    }

    protected IsbnMongo() {}

}