import api.CourierApi;
import entities.CreateCourierRequest;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class BaseCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Проверка кода ответа сервера при успешном создании курьера")
    public void checkIfCreatedSuccessfully(Response response) {
        response.then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_CREATED);

    }

    @After
    public void tearDown() {
        CourierApi.deleteAllCreatedCouriers();
    }
}
