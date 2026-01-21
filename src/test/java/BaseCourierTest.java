import entities.CourierLoginRequest;
import entities.CourierLoginResponse;
import entities.CreateCourierRequest;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class BaseCourierTest {
    private final List<CreateCourierRequest> courierDetails = new ArrayList<>();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    protected Response createCourier() {
        return createCourier(RandomStringUtils.randomAlphabetic(20),
                RandomStringUtils.randomAlphabetic(20),
                RandomStringUtils.randomAlphabetic(20));
    }

    @Step("Создаём нового курьера")
    protected Response createCourier(String login, String password, String name) {
        CreateCourierRequest request = new CreateCourierRequest(
                login,
                password,
                name
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().log().all()
                .post("/api/v1/courier");

        if (response.statusCode() == HttpStatus.SC_CREATED) {
            courierDetails.add(request);
        }
        return response;
    }

    @Step("Удаляем курьера")
    protected Response deleteCourier(CreateCourierRequest request) {
        Response response = loginCourier(new CourierLoginRequest(request.getLogin(), request.getPassword()));
        CourierLoginResponse loginResponse = response.as(CourierLoginResponse.class);
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/courier/{id}", loginResponse.getId());
    }

    @Step("Входим в систему с учетной записью курьера")
    protected Response loginCourier(CourierLoginRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().log().all()
                .post("/api/v1/courier/login");
    }

    @Step("Проверка кода ответа сервера при успешном создании курьера")
    public void checkIfCreatedSuccessfully(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_CREATED);

    }

    @After
    public void tearDown() {
        for (CreateCourierRequest courierDetail : courierDetails) {
            deleteCourier(courierDetail);
        }
    }
}
