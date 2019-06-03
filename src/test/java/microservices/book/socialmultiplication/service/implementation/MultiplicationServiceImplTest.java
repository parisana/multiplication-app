package microservices.book.socialmultiplication.service.implementation;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.socialmultiplication.repository.UserRepository;
import microservices.book.socialmultiplication.service.RandomGeneratorService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Parisana
 */
public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;
    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp(){
         // With this call to initMocks we tell Mockito to
        // process the annotations
        MockitoAnnotations.initMocks(this);
        this.multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService, multiplicationResultAttemptRepository, userRepository);
    }

    @Test
    public void createRandomMultiplication() {
        // given (our mocked Random Generator service will
        // return first 50, then 30)
        given(randomGeneratorService.generateRandomFactor()).
                willReturn(50, 30);
        // when
        Multiplication multiplication =
                multiplicationServiceImpl.createRandomMultiplication();
        // assert
        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
//        assertThat(multiplication.getResult()).isEqualTo(1500);
    }

    @Test
    public void checkCorrectAttempt() {

        // give
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt multiplicationResultAttempt =
                new MultiplicationResultAttempt(user, multiplication, 3_000, false);

        MultiplicationResultAttempt verifiedAttempt =
                new MultiplicationResultAttempt(user, multiplication, 3_000, true);

        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        // when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);

        // then
        assertThat(attemptResult).isTrue();
        verify(multiplicationResultAttemptRepository).save(verifiedAttempt);

    }

    @Test
    public void checkIncorrectAttempt() {

        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3_001, false);
        given(userRepository.findByAlias("jogn_doe")).willReturn(Optional.empty());

        // when
        boolean resultAttempt = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);

        // then
        assertThat(resultAttempt).isFalse();
        verify(multiplicationResultAttemptRepository).save(multiplicationResultAttempt);

    }

    @Test
    public void getStatsForUser() {

        // given an user "john_doe" and his 2 attempts
        User user = new User("john_doe");
        final Multiplication multiplication = new Multiplication(20, 30);
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 40, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 600, false);

        List<MultiplicationResultAttempt> submissionList = Lists.newArrayList(attempt1, attempt2);
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());
        given(multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc("john_doe")).willReturn(submissionList);

        // when
        final List<MultiplicationResultAttempt> statsForUser = multiplicationServiceImpl.getStatsForUser("john_doe");

        //then
        assertThat(statsForUser).isEqualTo(submissionList);
    }
}