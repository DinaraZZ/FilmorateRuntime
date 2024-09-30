package com.practice.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data // constr for final
@Builder
public class Film {
    int id;

    @NotBlank(message = "Название не может быть пустым")
    String name;

    @Size(max = 200)
    String description;

    LocalDate releaseDate;

    @Positive
    int duration;
}
