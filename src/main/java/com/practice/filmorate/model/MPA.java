package com.practice.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data // constr for final
@Builder
public class MPA {
    int id;

    @NotBlank(message = "Название не может быть пустым")
    String name;
}
