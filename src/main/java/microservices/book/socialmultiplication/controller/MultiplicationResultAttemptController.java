package microservices.book.socialmultiplication.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.service.MultiplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Parisana
 */

@RestController
@RequestMapping("/results")
public final class MultiplicationResultAttemptController {

    private final MultiplicationService multiplicationService;

    public MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @PostMapping
    public ResponseEntity<MultiplicationResultAttempt> postResult(@RequestBody MultiplicationResultAttempt multiplicationResultAttempt){
        final boolean isCorrect = multiplicationService.checkAttempt(multiplicationResultAttempt);

        return ResponseEntity.ok(new MultiplicationResultAttempt(multiplicationResultAttempt.getUser(),
                                            multiplicationResultAttempt.getMultiplication(),
                                            multiplicationResultAttempt.getResultAttempt(),
                                            isCorrect));
    }

    @GetMapping
    ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(
                multiplicationService.getStatsForUser(alias)
        );
    }

}
