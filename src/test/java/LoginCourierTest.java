import entities.CourierLoginRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class LoginCourierTest extends BaseCourierTest {
    @Test
    @DisplayName("Проверка успешной авторизации курьера")
    @Description("Тест создаёт курьера со всеми уникальными параметрами, авторизуется и проверяет что сервер возвращает 200")
    public void courierShouldBeAuthorizedSuccessfully() {
        String login = RandomStringUtils.randomAlphabetic(20);
        String password = RandomStringUtils.randomAlphabetic(20);
        createCourier(login, password, "firstname");
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, password);
        Response response = loginCourier(loginRequest);
        checkIfAuthorizedSuccessfully(response);
    }
    @Step("Проверка кода ответа и сообщения при успешной авторизации")
    public void checkIfAuthorizedSuccessfully(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Нельзя авторизоваться под несуществующим логином")
    @Description("Тест проверяет, что попытка авторизации с несуществующим логином возвращает 404 и сообщение об ошибке")
    public void shouldReturnNotFoundAndErrorMessageWhenLoginDoesNotExist() {
        String login = RandomStringUtils.randomAlphabetic(20);
        String password = RandomStringUtils.randomAlphabetic(20);
        Response firstResponse = createCourier(login, password, "firstname");
        checkIfCreatedSuccessfully(firstResponse);
        String login1 = RandomStringUtils.randomAlphabetic(20);
        CourierLoginRequest loginRequest = new CourierLoginRequest(login1, password);
        Response response = loginCourier(loginRequest);
        checkNotFoundWhenLoginDoesNotExist(response);
    }
    @Step("Проверка кода ответа и сообщения при попытке авторизоваться с несуществующим логином")
    public void checkNotFoundWhenLoginDoesNotExist(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с неправильным паролем")
    @Description("Тест проверяет, что попытка авторизации с неправильным паролем возвращает 404 и сообщение об ошибке")
    public void shouldReturnNotFoundAndErrorMessageWhenPasswordIsIncorrect() {
        String login = RandomStringUtils.randomAlphabetic(20);
        String password = RandomStringUtils.randomAlphabetic(20);
        Response firstResponse = createCourier(login, password, "firstname");
        checkIfCreatedSuccessfully(firstResponse);
        String password1 = RandomStringUtils.randomAlphabetic(20);
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, password1);
        Response response = loginCourier(loginRequest);
        checkNotFoundWhenPasswordIsIncorrect(response);
    }
    @Step("Проверка кода ответа и сообщения при попытке авторизоваться с неправильным паролем")
    public void checkNotFoundWhenPasswordIsIncorrect(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться под несуществующим логином и неправильным паролем")
    @Description("Тест проверяет, что попытка авторизации с несуществующим логином и неправильным паролем возвращает 404 и сообщение об ошибке")
    public void shouldReturnNotFoundAndErrorMessageWhenLoginAndPasswordDoNotExist() {
        String login = RandomStringUtils.randomAlphabetic(20);
        String password = RandomStringUtils.randomAlphabetic(20);
        Response firstResponse = createCourier(login, password, "firstname");
        checkIfCreatedSuccessfully(firstResponse);
        String login1 = RandomStringUtils.randomAlphabetic(20);
        String password1 = RandomStringUtils.randomAlphabetic(20);
        CourierLoginRequest loginRequest = new CourierLoginRequest(login1, password1);
        Response response = loginCourier(loginRequest);
        checkNotFoundWhenLoginAndPasswordDoNotExist(response);
    }
    @Step("Проверка кода ответа и сообщения при попытке авторизоваться с несуществующим логином и паролем")
    public void checkNotFoundWhenLoginAndPasswordDoNotExist(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с пустым полем login")
    @Description("Тест проверяет, что попытка авторизации с пустым полем login возвращает 400 и сообщение об ошибке")
    public void shouldReturnBadRequestAndErrorMessageWhenLoginIsMissing() {
        String login = RandomStringUtils.randomAlphabetic(20);
        String password = RandomStringUtils.randomAlphabetic(20);
        Response firstResponse = createCourier(login, password, "firstname");
        checkIfCreatedSuccessfully(firstResponse);
        CourierLoginRequest loginRequest = new CourierLoginRequest(null, password);
        Response response = loginCourier(loginRequest);
        checkBadRequestWhenLoginIsMissing(response);
    }
    @Step("Проверка кода ответа и сообщения при попытке авторизоваться с пустым полем login")
    public void checkBadRequestWhenLoginIsMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с пустым полем password")
    @Description("Тест проверяет, что попытка авторизации с пустым полем password возвращает 400 и сообщение об ошибке")
    public void shouldReturnBadRequestAndErrorMessageWhenPasswordIsMissing() {
        String login = RandomStringUtils.randomAlphabetic(20);
        String password = RandomStringUtils.randomAlphabetic(20);
        Response firstResponse = createCourier(login, password, "firstname");
        checkIfCreatedSuccessfully(firstResponse);
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, null);
        Response response = loginCourier(loginRequest);
        checkBadRequestWhenPasswordIsMissing(response);
    }

    @Step("Проверка кода ответа и сообщения при попытке авторизоваться с пустым полем password")
    public void checkBadRequestWhenPasswordIsMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с пустыми полями login и password")
    @Description("Тест проверяет, что попытка авторизации с пустыми полями login и password возвращает 400 и сообщение об ошибке")
    public void shouldReturnBadRequestAndErrorMessageWhenLoginAndPasswordAreMissing() {
        String login = RandomStringUtils.randomAlphabetic(20);
        String password = RandomStringUtils.randomAlphabetic(20);
        Response firstResponse = createCourier(login, password, "firstname");
        checkIfCreatedSuccessfully(firstResponse);
        CourierLoginRequest loginRequest = new CourierLoginRequest(null, null);
        Response response = loginCourier(loginRequest);
        checkBadRequestWhenLoginAndPasswordAreMissing(response);
    }
    @Step("Проверка кода ответа и сообщения при попытке авторизоваться с пустыми полями login и password")
    public void checkBadRequestWhenLoginAndPasswordAreMissing(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
