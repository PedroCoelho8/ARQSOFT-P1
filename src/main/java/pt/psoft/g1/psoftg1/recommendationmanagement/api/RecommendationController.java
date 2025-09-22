package pt.psoft.g1.psoftg1.recommendationmanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.RecommendationService;

import java.util.List;

@Tag(name = "Recommendations", description = "Endpoints for getting book recommendations")
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "Get recommendations")
    @GetMapping("/{readerNumber}")
    public ResponseEntity<List<Book>> getRecommendations(String readerNumber) {
        List<Book> recommendations = recommendationService.getAlRecommendation(readerNumber);
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }
}