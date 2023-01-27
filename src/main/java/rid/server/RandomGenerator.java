package rid.server;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomGenerator {

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public int getNumber(final int bound) {
        return random.nextInt(bound) + 1;
    }
}
