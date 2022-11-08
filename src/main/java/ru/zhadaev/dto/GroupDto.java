package ru.zhadaev.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GroupDto {
    private UUID id;
    private String name;
}
