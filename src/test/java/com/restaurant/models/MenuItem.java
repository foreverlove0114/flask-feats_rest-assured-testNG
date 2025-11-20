package com.restaurant.models;

public class MenuItem {
    private int id;
    private String name;
    private String ingredients;
    private String description;
    private double price;
    private String weight;
    private String file_name;
    private boolean active;

    public MenuItem() {}

    public MenuItem(String name, String ingredients, String description, double price, String weight) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.active = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getFileName() { return file_name; }
    public void setFileName(String file_name) { this.file_name = file_name; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}