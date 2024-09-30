package com.practice.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data // constr for final
@Builder
public class User {
    int id;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелы")
    String login;

    @Email(message = "Почта должна содержать символ @")
    @NotBlank(message = "Почта не может быть пустой")
    String email;

    String name;

    @PastOrPresent(message = "Дата рождения не должна быть в будущем")
    LocalDate birthday;
}
