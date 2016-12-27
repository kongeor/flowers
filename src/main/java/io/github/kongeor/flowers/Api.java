package io.github.kongeor.flowers;

import io.github.kongeor.flowers.domain.Flower;

import java.util.Arrays;
import java.util.List;

public class Api {

    public static List<Flower> getAllFlowers() {
        Flower f1 = new Flower(1L, "Aglaonima", "Prasina fylla");
	Flower f2 = new Flower(2L, "Spathifyllo", "Koftera fylla");

	return Arrays.asList(f1, f2);
    }
}
