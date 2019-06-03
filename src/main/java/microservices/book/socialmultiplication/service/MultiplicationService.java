package microservices.book.socialmultiplication.service;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;

import java.util.List;

/**
 * @author Parisana
 */
public interface MultiplicationService {

    /**
     * Generates a random Multiplication object.
     * @return a Multiplication of randomly generated numbers.
     * */
    Multiplication createRandomMultiplication();

    /**
     * @return true if the attempt matches the result of the multiplication, otherwise false.
     * */
    boolean checkAttempt(final MultiplicationResultAttempt multiplicationResultAttempt);

    List<MultiplicationResultAttempt>
    getStatsForUser(String userAlias);
}
