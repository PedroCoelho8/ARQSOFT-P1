package pt.psoft.g1.psoftg1.bookmanagement.services;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookMongo;
import pt.psoft.g1.psoftg1.bookmanagement.model.DescriptionMongo;
import pt.psoft.g1.psoftg1.bookmanagement.model.IsbnMongo;
import pt.psoft.g1.psoftg1.bookmanagement.model.TitleMongo;

public class BookDBMapper {

    public static BookMongo toMongo(Book book) {
        BookMongo bookMongo = new BookMongo();
        bookMongo.setIsbn(new IsbnMongo(book.getIsbn()));
        bookMongo.setTitle(new TitleMongo(book.getTitle().getTitle()));
        bookMongo.setDescription(new DescriptionMongo(book.getDescription()));
        bookMongo.setGenre(book.getGenre());
        bookMongo.setAuthors(book.getAuthors());
        bookMongo.setVersion(book.getVersion());
        return bookMongo;
    }

    public static Book toEntity(BookMongo bookMongo) {
        if (bookMongo == null) {
            return null;
        }

        Isbn isbn = new Isbn(bookMongo.getIsbn().getIsbn());
        Title title = new Title(bookMongo.getTitle().getTitle());
        Description description = new Description(bookMongo.getDescription().getDescription());

        return new Book(
                isbn.toString(),
                title.toString(),
                description.toString(),
                bookMongo.getGenre(),
                bookMongo.getAuthors(),
                bookMongo.getPhotoURI()
        );
    }
}