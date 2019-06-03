package microservices.book.socialmultiplication.service.implementation;

import microservices.book.socialmultiplication.service.implementation.RandomGeneratorServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


/**
 * @author Parisana
 */
public class RandomGeneratorServiceImplTest {

    RandomGeneratorServiceImpl randomGeneratorService;

    @Before
    public void setUp(){
        this.randomGeneratorService = new RandomGeneratorServiceImpl();
    }

    @Test
    public void generateRandomNumberIsBetweenExpectedLimits(){

        // when a good sample of randomly generated factors is generated
        final List<Integer> randomFactors = IntStream.range(0, 1000).map(i -> randomGeneratorService.generateRandomFactor()).boxed().collect(Collectors.toList());

        // then all of them should be between 11 and 100 aswell, because we want a middle-complexity calculation.
        assertThat(randomFactors).containsOnlyElementsOf(IntStream.range(11,100).boxed().collect(Collectors.toList()));

    }

}