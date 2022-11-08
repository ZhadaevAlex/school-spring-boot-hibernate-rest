package ru.zhadaev.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CourseDto {
    private UUID id;
    private String name;
    private String description;
}
