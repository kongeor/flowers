package io.github.kongeor.flowers;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class AppTest {

    @BeforeClass
    public static void setup() {
        App.main(null);
    }

    @Test
    public void test_get_default_user() {
        given()
            .body("{ \"username\": \"admin\", \"password\": \"admin\" }")
        .expect()
                .body("username", equalTo("admin"))
                .body("email", equalTo("admin@example.com"))
                .when()
        .post("http://localhost:4567/api/login");
    }

    @AfterClass
    public static void tearDown() {
        Spark.stop();
    }
}