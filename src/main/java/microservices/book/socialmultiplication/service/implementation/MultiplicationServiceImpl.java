package microservices.book.socialmultiplication.service.implementation;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.socialmultiplication.repository.UserRepository;
import microservices.book.socialmultiplication.service.MultiplicationService;
import microservices.book.socialmultiplication.service.RandomGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author Parisana
 */
@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private final RandomGeneratorService randomGeneratorService;

    private final MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;

    private final UserRepository userRepository;

    @Autowired
    public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService, MultiplicationResultAttemptRepository multiplicationResultAttemptRepository, UserRepository userRepository) {
        this.randomGeneratorService = randomGeneratorService;
        this.multiplicationResultAttemptRepository = multiplicationResultAttemptRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Multiplication createRandomMultiplication() {

        final int factorA = randomGeneratorService.generateRandomFactor();
        final int factorB = randomGeneratorService.generateRandomFactor();

        return new Multiplication(factorA, factorB);
    }

    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt attempt) {

        // Check if the user already exists for this alias
        final Optional<User> optionalUser = userRepository.findByAlias(attempt.getUser().getAlias());

        // Avoid hack attempts
        Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct!");

        final boolean isAttemptCorrect = attempt.getResultAttempt() == (attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB());

        final MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(optionalUser.orElse(attempt.getUser()),
                                                                                            attempt.getMultiplication(),
                                                                                            attempt.getResultAttempt(),
                                                                                            isAttemptCorrect);

        // store the checked-attempt
        multiplicationResultAttemptRepository.save(checkedAttempt);

        return isAttemptCorrect;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }
}
