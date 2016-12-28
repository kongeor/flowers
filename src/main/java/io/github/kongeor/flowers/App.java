package io.github.kongeor.flowers;

import com.google.gson.Gson;
import io.github.kongeor.flowers.domain.Flower;
import io.github.kongeor.flowers.services.UserService;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class App {

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        Db.migrate();
//        UserService.register("admin", "admin", "admin@example.com"); // in migration
	setupStaticFiles();
        get("/api/flowers", (req, res) -> Api.getAllFlowers(), gson::toJson);
	post("/api/flowers", (req, res) -> Api.createFlower(parseJson(req.body(), Flower.class)), gson::toJson);
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
