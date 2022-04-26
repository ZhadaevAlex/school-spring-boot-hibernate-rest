package ru.zhadaev.dao.entitie;

public class Group {
    private Integer id;
    private String name;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
