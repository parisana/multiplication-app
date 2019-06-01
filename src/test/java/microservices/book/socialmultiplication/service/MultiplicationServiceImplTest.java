package microservices.book.socialmultiplication.service;

import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * @author Parisana
 */
public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;
    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Before
    public void setUp(){
         // With this call to initMocks we tell Mockito to
        // process the annotations
        MockitoAnnotations.initMocks(this);
        this.multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService);
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
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3_000);

        assertThat(multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt)).isTrue();

    }

    @Test
    public void checkIncorrectAttempt() {

        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3_001);

        assertThat(multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt)).isFalse();

    }
}