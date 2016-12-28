package io.github.kongeor.flowers;

import com.google.gson.Gson;
import io.github.kongeor.flowers.domain.Flower;

import static spark.Spark.*;

public class App {

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        Db.migrate();
//        UserService.register("admin", "admin", "admin@example.com"); // in migration
	setupStaticFiles();
        get("/api/flowers", (req, res) -> Db.findAllFlowers(), gson::toJson);
	post("/api/flowers", (req, res) -> Db.insertFlower(parseJson(req.body(), Flower.class)), gson::toJson);


	exception(Exception.class, (e, req, res) -> {
	    e.printStackTrace();
	});
    }

    public static <T> T parseJson(String payload, Class<T> clazz) {
        return gson.fromJson(payload, clazz);
    }

    public static void setupStaticFiles() {
	if (System.getProperty("flowers.dev") != null) {
	    String projectDir = System.getProperty("user.dir");
	    String staticDir = "/src/main/resources/public";
	    staticFiles.externalLocation(projectDir + staticDir);
	} else {
	    staticFiles.location("/public");
	}
    }
}
