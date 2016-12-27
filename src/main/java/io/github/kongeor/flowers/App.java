package io.github.kongeor.flowers;

import com.google.gson.Gson;

import static spark.Spark.get;
import static spark.Spark.staticFiles;

public class App {

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
	setupStaticFiles();
        get("/api/flowers", (req, res) -> Api.getAllFlowers(), gson::toJson);
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
