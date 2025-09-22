package pt.psoft.g1.psoftg1.recommendationmanagement.algorithms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.util.ArrayList;
import java.util.List;

@Component("RecAlgByAge")
public class AlRecommendationByAge implements AlRecommendation {

    @Value("${suggestionsLimitPerGenre}")
    private int numrecomendacoes;
    @Value("${min_age}")
    private long minAge;
    @Value("${max_age}")
    private long maxAge;

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final ReaderRepository readerRepository;

    public AlRecommendationByAge(BookRepository bookRepository, GenreRepository genreRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.readerRepository = readerRepository;
    }

    public List<Book> getLendingRecommendations(String readerNumber) {
        Long readerId;
        try {
            readerId = Long.parseLong(readerNumber);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de readerNumber inválido: " + readerNumber);
        }

        // Usa o readerId convertido para buscar o ReaderDetails
        ReaderDetails readerDetails = readerRepository.findByUserId(readerId)
                .orElseThrow(() -> new NotFoundException("Reader not found"));


        Pageable pageable = PageRequest.of(0, numrecomendacoes);


        int age = readerDetails.getBirthDate().getAge();
        System.out.println("Idade do Reader: " + age);
        List<Book> recommendations = new ArrayList<>();

        //if age < min age (idade ate 10) age<10: recomendacoes books of genre “children”
        if(age <= minAge)
        {
            System.out.println("A usar a recomendacao Infantil");
           recommendations = bookRepository.findTopLentBooksByGenre("Infantil", pageable);

        }else         //if min age < max age  (de 10 a 18)  recomendacoes books of genre “juvenile”  //Verificar com o professor
        if(minAge < age && age < maxAge)
        {
            System.out.println("A usar a recomendacao Thriller");
            recommendations = bookRepository.findTopLentBooksByGenre("Thriller", pageable); //Verificar com o professor

        }else        //if max age =< age (18 =+) recomendacoes of the most lent genre of the reader
        {
            // Define um Pageable para obter apenas o primeiro gênero mais emprestado
            Pageable topGenrePageable = PageRequest.of(0, 1); // Apenas 1 genero
            System.out.println("A usar a recomendacao do genero mais requisitado");
            // Recupera o primeiro gênero com mais empréstimos
            GenreBookCountDTO topGenre = genreRepository.findTopGenresByLendCount(1, topGenrePageable)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("No genre found"));

            recommendations = bookRepository.findTopLentBooksByGenre(topGenre.getGenre(), pageable);

        }

        return recommendations;
    }
}
