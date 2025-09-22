package pt.psoft.g1.psoftg1.authormanagement.services;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;

public class AuthorConverter {

    public static AuthorMongo toAuthorMongo(Author author) {
        if (author == null) {
            return null;
        }
        return new AuthorMongo(
                author.getName(),
                author.getBio(),
                null
        );
    }

    public static Author toAuthor(AuthorMongo authorMongo) {
        if (authorMongo == null) {
            return null;
        }

        return new Author(
                null,
                authorMongo.getName(),
                authorMongo.getBio(),
                null
        );
    }

}
