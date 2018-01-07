package Tests;

import java.util.Random;
import java.util.stream.IntStream;

public class TestRandomGaussian {
    public static void main(String[] args) {
        Random random = new Random();
        IntStream.range(1,100).forEach((x) -> System.out.println(random.nextDouble()));
    }
}
