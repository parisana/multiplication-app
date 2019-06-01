package microservices.book.socialmultiplication.service;

import microservices.book.socialmultiplication.domain.Multiplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * @author Parisana
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MultiplicationServiceTest {

    @MockBean
    private RandomGeneratorService randomGeneratorService;

    @Autowired
    private MultiplicationService multiplicationService;

    @Test
    public void createRandomMultiplicatin() {

//        given (our mockito random generator service will return first 50, then 30)
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);
//        when
        Multiplication multiplication = multiplicationService.createRandomMultiplication();

//        then
        assertEquals(50, multiplication.getFactorA());
        assertEquals(30, multiplication.getFactorB());
//        assertEquals(1500, multiplication.getResult());
    }
}