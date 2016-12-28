package io.github.kongeor.flowers;

import io.github.kongeor.flowers.domain.Flower;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Api {

    private static AtomicLong counter = new AtomicLong();

    private static List<Flower> flowers = new ArrayList<>();

    static {
	Flower f1 = new Flower(counter.incrementAndGet(), "Aglaonima", "Prasina fylla");
	Flower f2 = new Flower(counter.incrementAndGet(), "Spathifyllo", "Koftera fylla");
	flowers.add(f1);
	flowers.add(f2);
    }

    public static List<Flower> getAllFlowers() {
        return flowers;
    }

    public static Flower createFlower(Flower flower) {
        flower.setId(counter.incrementAndGet());
        flowers.add(flower);
        return flower;
    }
}
