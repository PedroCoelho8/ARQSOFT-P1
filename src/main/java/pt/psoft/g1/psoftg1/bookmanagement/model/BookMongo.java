package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

@Document(collection = "books")
@Data
public class BookMongo {
    @Id
    private String id;

    private IsbnMongo isbn;

    private TitleMongo title;

    private DescriptionMongo description;

    private Genre genre;

    private List<Author> authors;

    private String photoURI;

    private Long version;

}