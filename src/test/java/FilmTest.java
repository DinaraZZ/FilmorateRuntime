import com.practice.filmorate.controller.FilmController;
import com.practice.filmorate.model.Film;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class FilmTest {
    protected static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    protected String validateAndGetFirstMessageTemplate(Film obj) {
        return validator.validate(obj).stream()
                .findFirst()
                .orElseThrow()
                .getConstraintDescriptor()
                .getMessageTemplate();
    }

    @Test
    public void shouldNotAddIfNameIsBlank() {
        // не должен добавлять, если название пустое
        Film film = Film.builder()
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
        String expectedMessage = "Название не может быть пустым";

        String actualMessage = validateAndGetFirstMessageTemplate(film);

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldNotAddIfDescriptionIs201() {
        // не должен добавлять, если в описании 201 символ
        Film film = Film.builder()
                .name("name")
                .description("a".repeat(201))
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
        String expectedMessage = "Максимальная длина описания — 200 символов";

        String actualMessage = validateAndGetFirstMessageTemplate(film);

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldNotAddIfDescriptionIs1000() {
        // не должен добавлять, если в описании 1000 символов
        Film film = Film.builder()
                .name("name")
                .description("a".repeat(1000))
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
        String expectedMessage = "Максимальная длина описания — 200 символов";

        String actualMessage = validateAndGetFirstMessageTemplate(film);

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldAddIfDescriptionIs200() {
        // должен добавлять, если в описании 200 символов
        Film film = Film.builder()
                .name("name")
                .description("a".repeat(200))
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
        int expectedAmount = 1;

        FilmController controller = new FilmController();
        controller.create(film);
        int actualAmount = controller.findAll().size();

        Assertions.assertEquals(expectedAmount, actualAmount);
    }

    @Test
    public void shouldAddIfDescriptionIs1() { // ?????????
        // должен добавлять, если в описании 1 символ
        Film film = Film.builder()
                .name("name")
                .description("a".repeat(202))
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
        int expectedAmount = 2;

        /*String expectedMessage = "М";
        String actualMessage = validateAndGetFirstMessageTemplate(film);
        Assertions.assertEquals(expectedMessage, actualMessage);*/

        FilmController controller = new FilmController();
        controller.create(film);
        int actualAmount = controller.findAll().size();

        Assertions.assertEquals(expectedAmount, actualAmount);
    }

    @Test
    public void shouldNotAddIfReleaseDateIsBefore18951228() {
        // не должен добавлять, если вышел раньше 1895-12-28
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(120)
                .build();
        String expectedMessage = "Дата релиза — не раньше 28 декабря 1895 года";

        FilmController controller = new FilmController();
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class,
                () -> controller.create(film));

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldAddIfReleaseDateIs18951228() {
        // должен добавлять, если вышел 1895-12-28
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(120)
                .build();
        int expectedAmount = 1;

        FilmController controller = new FilmController();
        controller.create(film);
        int actualAmount = controller.findAll().size();

        Assertions.assertEquals(expectedAmount, actualAmount);
    }

    @Test
    public void shouldNotAddIfDurationIsNegative() {
        // не должен добавлять, если продолжительность отрицательная
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(-1)
                .build();
        String expectedMessage = "Продолжительность фильма должна быть положительной";

        String actualMessage = validateAndGetFirstMessageTemplate(film);

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldNotAddIfDurationIs0() {
        // не должен добавлять, если продолжительность равна 0
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(0)
                .build();
        String expectedMessage = "Продолжительность фильма должна быть положительной";

        String actualMessage = validateAndGetFirstMessageTemplate(film);

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldNotAddIfDurationIsPositive() {
        // должен добавлять, если продолжительность больше нуля
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(1)
                .build();
        int expectedAmount = 1;

        FilmController controller = new FilmController();
        controller.create(film);
        int actualAmount = controller.findAll().size();

        Assertions.assertEquals(expectedAmount, actualAmount);
    }
}