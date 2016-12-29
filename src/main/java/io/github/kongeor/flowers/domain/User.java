package io.github.kongeor.flowers.domain;

import com.google.gson.annotations.Expose;

public class User {

    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Expose
    private String username;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Expose(serialize = false)
    private String password;
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Expose
    private String email;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
	return "User{" + "id=" + id + ", username='" + username + '\'' + ", email='" + email + '\'' + '}';
    }
}
