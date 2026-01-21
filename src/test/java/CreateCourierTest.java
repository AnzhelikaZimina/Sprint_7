import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;

public class CreateCourierTest extends BaseCourierTest {
    @Test
    @DisplayName("Проверка успешного создания курьера")
    @Description("Тест создаёт курьера со всеми уникальными параметрами и проверяет, что сервер возвращает 201 Created")
    public void courierShouldBeCreatedSuccessfully() {
        Response response = createCourier();
        checkIfCreatedSuccessfully(response);
    }

    @Test
    @DisplayName("Проверка что успешный запрос возвращает ok: true")
    @Description("Тест создаёт курьера со всеми уникальными параметрами и проверяет, что поле 'ok' в теле ответа имеет значение true")
    public void shouldReturnOkTrueOnSuccessfulRequest() {
        Response response = createCourier();
        checkMessageSuccessfulRequest(response);
    }
    @Step("Проверка поля 'ok' в теле ответа на true")
    public void checkMessageSuccessfulRequest(Response response) {
        response.then()
                .log().all()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка что нельзя создать двух одинаковых курьеров")
    @Description("Тест создаёт курьера с уникальными параметрами, затем пытается создать курьера с тем же логином и проверяет, что сервер возвращает 409 Conflict")
    public void shouldNotCreateDuplicateCourier() {
        String login = RandomStringUtils.randomAlphabetic(10);
        Response firstResponse = createCourier(login, "password", "firstname");
        checkIfCreatedSuccessfully(firstResponse);
        Response secondResponse = createCourier(login, "password", "firstname");
        checkIfCreatedNotSuccessfullyWithExistingLogin(secondResponse);
    }
    @Step("Проверка кода ответа сервера при попытке создать курьера с существующим логином")
    public void checkIfCreatedNotSuccessfullyWithExistingLogin(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    @DisplayName("Проверка ошибки при создании курьера с существующим логином")
    @Description("Тест создаёт курьера с уникальными параметрами, затем пытается создать курьера с тем же логином и проверяет, что сервер возвращает сообщение 'Этот логин уже используется'")
    public void shouldNotCreateWhenLoginIsAlreadyUsed() {
        String login = RandomStringUtils.randomAlphabetic(10);
        Response firstResponse = createCourier(login, "password", "firstname");
        checkIfCreatedSuccessfully(firstResponse);
        Response secondResponse = createCourier(login, "password", "firstname");
        checkMessageWhenLoginIsAlreadyUsed(secondResponse);
    }
    @Step("Проверка текста ошибки при использовании существующего логина")
    public void checkMessageWhenLoginIsAlreadyUsed(Response response) {
        response.then()
                .log().all()
                .assertThat().body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    @Description("Тест проверяет, что при попытке создать курьера без поля login сервер возвращает 400 Bad Request")
    public void shouldNotCreateCourierWithoutLogin() {
        Response response = createCourier(null, "password", "firstname");
        checkResponseWhenLoginIsMissing(response);
    }
    @Step("Проверка, что сервер возвращает 400 при отсутствии login")
    public void checkResponseWhenLoginIsMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    @Description("Тест проверяет, что при попытке создать курьера без поля password сервер возвращает 400 Bad Request")
    public void shouldNotCreateCourierWithoutPassword() {
        Response response = createCourier("login", null, "firstname");
        checkResponseWhenPasswordIsMissing(response);
    }
    @Step("Проверка, что сервер возвращает 400 при отсутствии password")
    public void checkResponseWhenPasswordIsMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина и пароля")
    @Description("Тест проверяет, что при попытке создать курьера без login и password сервер возвращает 400 Bad Request")
    public void shouldNotCreateCourierWithoutLoginAndPassword() {
        Response response = createCourier(null, null, "firstname");
        checkResponseWhenLoginAndPasswordAreMissing(response);
    }
    @Step("Проверка, что сервер возвращает 400 при отсутствии login и password")
    public void checkResponseWhenLoginAndPasswordAreMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Ошибка при отсутствии логина")
    @Description("Тест проверяет, что при попытке создать курьера без login сервер возвращает сообщение об ошибке в теле ответа")
    public void shouldReturnErrorMessageWhenLoginIsMissing() {
        Response response = createCourier(null, "password", "firstname");
        checkErrorMessageWhenLoginIsMissing(response);
    }
    @Step("Проверка текста ошибки при отсутствии login")
    public void checkErrorMessageWhenLoginIsMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при отсутствии пароля")
    @Description("Тест проверяет, что при попытке создать курьера без password сервер возвращает сообщение об ошибке в теле ответа")
    public void shouldReturnErrorMessageWhenPasswordIsMissing() {
        Response response = createCourier("login", null, "firstname");
        checkErrorMessageWhenPasswordIsMissing(response);
    }
    @Step("Проверка текста ошибки при отсутствии password")
    public void checkErrorMessageWhenPasswordIsMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при отсутствии логина и пароля")
    @Description("Тест проверяет, что при попытке создать курьера без login и password сервер возвращает сообщение об ошибке в теле ответа")
    public void shouldReturnErrorMessageWhenLoginAndPasswordAreMissing() {
        Response response = createCourier(null, null, "firstname");
        checkErrorMessageWhenLoginAndPasswordAreMissing(response);
    }
    @Step("Проверка текста ошибки при отсутствии login и password")
    public void checkErrorMessageWhenLoginAndPasswordAreMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
