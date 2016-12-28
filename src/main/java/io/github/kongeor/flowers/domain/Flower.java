package io.github.kongeor.flowers.domain;

public class Flower {

    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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
}