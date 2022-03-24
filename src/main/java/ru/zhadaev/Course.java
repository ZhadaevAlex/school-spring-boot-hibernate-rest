package ru.zhadaev;

public class Course {
    private final String name;
    private String description;

    public Course(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
