package io.github.kongeor.flowers.domain;

import com.google.gson.annotations.Expose;

public class Flower {

    @Expose
    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Expose
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Expose
    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Flower() {
    }

    public Flower(Long id, String name, String description) {
	this.id = id;
	this.name = name;
	this.description = description;
    }

    @Override
    public String toString() {
	return "Flower{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }
}
