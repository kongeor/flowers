package io.github.kongeor.flowers.services;

import io.github.kongeor.flowers.Db;
import io.github.kongeor.flowers.domain.User;
import io.github.kongeor.flowers.exceptions.AuthException;
import io.github.kongeor.flowers.util.PasswordAuthentication;

public class UserService {

    private static PasswordAuthentication passwordAuth = new PasswordAuthentication();

    public static void register(String username, String password, String email) {
        String hashed = passwordAuth.hash(password.toCharArray()); // TODO check!

	Db.insertUser(username, hashed, email);
    }

    public static User login(User user) {
	String usernameOrEmail = user.getUsername();
	User dbUser = Db.findUserByNameOrEmail(usernameOrEmail).orElse(null);
	if (dbUser != null && user.getPassword() != null) {
	    String hashed = dbUser.getPassword();
	    boolean authed = passwordAuth.authenticate(user.getPassword().toCharArray(), hashed);
	    if (authed) {
	        return dbUser;
	    }
	}
	throw new AuthException("Invalid credentials");
    }
}
