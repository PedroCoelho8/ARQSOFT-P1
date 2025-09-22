package pt.psoft.g1.psoftg1.recommendationmanagement.algorithms;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.List;

public interface AlRecommendation {
    List<Book> getLendingRecommendations(String readerNumber);
}
