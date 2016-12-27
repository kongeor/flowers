package io.github.kongeor.flowers;

import com.google.gson.Gson;
import spark.Spark;

public class App {

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
	Spark.staticFileLocation("/public");
        Spark.get("/api/flowers", (req, res) -> Api.getAllFlowers(), gson::toJson);
    }
}
