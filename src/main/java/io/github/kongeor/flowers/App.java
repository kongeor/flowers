package io.github.kongeor.flowers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.kongeor.flowers.domain.Flower;
import io.github.kongeor.flowers.domain.User;
import io.github.kongeor.flowers.exceptions.AuthException;
import io.github.kongeor.flowers.exceptions.NotFound;
import io.github.kongeor.flowers.services.UserService;

import java.util.Collections;

import static spark.Spark.*;

public class App {

    private static final String UID = "UID";

    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static void main(String[] args) {
        Db.migrate();
//        UserService.register("admin", "admin", "admin@example.com"); // in migration
	setupStaticFiles();
        get("/api/flowers", (req, res) -> Db.findAllFlowers(), gson::toJson);
	post("/api/flowers", (req, res) -> Db.insertFlower(parseJson(req.body(), Flower.class)), gson::toJson);

	post("/api/login", (req, res) -> {
	    User user = UserService.login(parseJson(req.body(), User.class));
	    req.session().attribute(UID, user.getId());
	    return user;
	}, gson::toJson);
	post("/api/logout", (req, res) -> {
	    req.session().removeAttribute(UID);
	    res.status(204);
	    return null;
	}, gson::toJson);
	get("/api/session", (req, res) -> {
	    return Db.findUserById(req.session().<Long>attribute(UID))
		.orElseThrow(() -> new NotFound("No active session"));
	}, gson::toJson);

	exception(NotFound.class, (e, req, res) -> {
	    res.status(404);
	    res.body(formatError(e.getMessage()));
	});

	exception(AuthException.class, (e, req, res) -> {
	    res.status(401);
	    res.body(formatError(e.getMessage()));
	});

	exception(Exception.class, (e, req, res) -> {
	    e.printStackTrace();
	});
    }

    private static String formatError(String error) {
        return gson.toJson(Collections.singletonMap("error", error));
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
