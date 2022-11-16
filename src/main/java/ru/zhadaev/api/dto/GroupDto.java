package ru.zhadaev.api.dto;

import lombok.Data;
import ru.zhadaev.api.validation.Marker;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class GroupDto {
    private UUID id;

    @NotBlank(groups = Marker.OnPostPut.class,
            message = "The group name must consist of at least one character")
    private String name;
}
