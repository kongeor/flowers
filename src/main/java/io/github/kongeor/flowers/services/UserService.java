package io.github.kongeor.flowers.services;

import io.github.kongeor.flowers.Db;
import io.github.kongeor.flowers.util.PasswordAuthentication;

public class UserService {

    private static PasswordAuthentication passwordAuth = new PasswordAuthentication();

    public static void register(String username, String password, String email) {
        String hashed = passwordAuth.hash(password.toCharArray()); // TODO check!

	Db.insertUser(username, hashed, email);
    }
}
