/*



import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class UserTestSher {

    protected static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void createUserFailLoginTest() {
        User user  = User.builder()
                .email("email@email.com")
                .name("someName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        String expected = "Логин не может быть пустым";

        String actual = validateAndGetFirstMessageTemplate(user);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void createUserFailEmail() {
        User user  = User.builder()
                .login("someLogin")
                .email("emailemail.com")
                .name("someName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        String expected = "Почта должна содержать символ @";

        String actual = validateAndGetFirstMessageTemplate(user);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void createUserFutureBirthday() {
        User user  = User.builder()
                .login("someLogin")
                .email("email@email.com")
                .name("someName")
                .birthday(LocalDate.of(2500, 1, 1))
                .build();
        String expected = "Дата рождение не должна быть в будущем";

        String actual = validateAndGetFirstMessageTemplate(user);

        Assertions.assertEquals(expected, actual);
    }

    protected String validateAndGetFirstMessageTemplate(User obj) {
        return validator.validate(obj).stream()
                .findFirst()
                .orElseThrow()
                .getConstraintDescriptor()
                .getMessageTemplate();
    }
}*/
