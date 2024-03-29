package microservices.book.socialmultiplication.service.implementation;

import microservices.book.socialmultiplication.service.RandomGeneratorService;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author Parisana
 */
@Service
public class RandomGeneratorServiceImpl implements RandomGeneratorService {

    private final static int MINIMUM_FACTOR = 11;
    private final static int MAXIMUM_FACTOR = 99;

    @Override
    public int generateRandomFactor() {
        return new Random().nextInt((MAXIMUM_FACTOR-MINIMUM_FACTOR)+1)+MINIMUM_FACTOR;
    }
}
