package ru.zhadaev.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class GroupDto {
    private UUID id;

    @NotBlank(message = "The group name must consist of at least one character")
    private String name;
}
