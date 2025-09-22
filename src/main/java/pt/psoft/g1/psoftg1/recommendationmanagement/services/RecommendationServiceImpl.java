package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.recommendationmanagement.algorithms.AlRecommendation;

import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final AlRecommendation alRecommendation;

    @Autowired
    public RecommendationServiceImpl(ApplicationContext applicationContext,
                                     @Value("${recommendation.algorithm}") String recommendationAlgorithm) {
        this.alRecommendation = (AlRecommendation) applicationContext.getBean(recommendationAlgorithm);
    }

    public List<Book> getAlRecommendation(String authorNumber) {
        return alRecommendation.getLendingRecommendations(authorNumber);
    }
}