import api.CourierApi;
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
    @Description("Тест создаёт курьера со всеми уникальными параметрами и проверяет, что сервер возвращает 201 Created и поле 'ok' в теле ответа имеет значение true")
    public void courierShouldBeCreatedSuccessfully() {
        Response response = CourierApi.createCourier();
        checkIfCreatedSuccessfully(response);
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
    @Description("Тест создаёт курьера с уникальными параметрами, затем пытается создать курьера с тем же логином и проверяет, что сервер возвращает 409 Conflict и сообщение 'Этот логин уже используется'")
    public void shouldNotCreateDuplicateCourier() {
        String login = RandomStringUtils.randomAlphabetic(10);
        Response firstResponse = CourierApi.createCourier(login, "password", "firstname");
        checkIfCreatedSuccessfully(firstResponse);
        Response secondResponse = CourierApi.createCourier(login, "password", "firstname");
        checkIfCreatedNotSuccessfullyWithExistingLogin(secondResponse);
    }
    @Step("Проверка что сервер возвращает 409 и ошибку при попытке создать курьера с существующим логином")
    public void checkIfCreatedNotSuccessfullyWithExistingLogin(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    @Description("Тест проверяет, что при попытке создать курьера без поля login сервер возвращает 400 Bad Request и сообщение об ошибке в теле ответа")
    public void shouldNotCreateCourierWithoutLogin() {
        Response response = CourierApi.createCourier(null, "password", "firstname");
        checkResponseWhenLoginIsMissing(response);
    }
    @Step("Проверка, что сервер возвращает 400 и ошибку при отсутствии login")
    public void checkResponseWhenLoginIsMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    @Description("Тест проверяет, что при попытке создать курьера без поля password сервер возвращает 400 Bad Request и сообщение об ошибке в теле ответа")
    public void shouldNotCreateCourierWithoutPassword() {
        Response response = CourierApi.createCourier("login", null, "firstname");
        checkResponseWhenPasswordIsMissing(response);
    }
    @Step("Проверка, что сервер возвращает 400 и ошибку при отсутствии password")
    public void checkResponseWhenPasswordIsMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина и пароля")
    @Description("Тест проверяет, что при попытке создать курьера без login и password сервер возвращает 400 Bad Request и сообщение об ошибке в теле ответа")
    public void shouldNotCreateCourierWithoutLoginAndPassword() {
        Response response = CourierApi.createCourier(null, null, "firstname");
        checkResponseWhenLoginAndPasswordAreMissing(response);
    }
    @Step("Проверка, что сервер возвращает 400 и ошибку при отсутствии login и password")
    public void checkResponseWhenLoginAndPasswordAreMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
