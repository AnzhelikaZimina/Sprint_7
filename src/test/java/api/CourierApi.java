package api;

import entities.CourierLoginRequest;
import entities.CourierLoginResponse;
import entities.CreateCourierRequest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class CourierApi {
    private static final List<CreateCourierRequest> courierDetails = new ArrayList<>();

    public static Response createCourier() {
        return createCourier(RandomStringUtils.randomAlphabetic(20),
                RandomStringUtils.randomAlphabetic(20),
                RandomStringUtils.randomAlphabetic(20));
    }

    @Step("Создаём нового курьера")
    public static Response createCourier(String login, String password, String name) {
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
    public static Response deleteCourier(CreateCourierRequest request) {
        Response response = loginCourier(new CourierLoginRequest(request.getLogin(), request.getPassword()));
        CourierLoginResponse loginResponse = response.as(CourierLoginResponse.class);
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/courier/{id}", loginResponse.getId());
    }

    @Step("Входим в систему с учетной записью курьера")
    public static Response loginCourier(CourierLoginRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().log().all()
                .post("/api/v1/courier/login");
    }

    public static void deleteAllCreatedCouriers() {
        for (CreateCourierRequest courierDetail : courierDetails) {
            CourierApi.deleteCourier(courierDetail);
        }
    }
}
