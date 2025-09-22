package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.List;

public interface RecommendationService {
    List<Book> getAlRecommendation(String authorNumber);
}
