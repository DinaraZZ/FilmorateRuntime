package com.practice.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data // constr for final
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Genre implements Comparable<Genre> {
    @EqualsAndHashCode.Include
    int id;

    @NotBlank(message = "Название не может быть пустым")
    String name;

    @Override
    public int compareTo(Genre o) {
        return Integer.compare(id, o.id);
    }
}
