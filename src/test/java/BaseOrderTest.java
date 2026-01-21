import entities.OrderRequest;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;

import static io.restassured.RestAssured.given;

public class BaseOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Создаем новый заказ")
    protected Response createOrder(OrderRequest order) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(order)
                .when().log().all()
                .post("/api/v1/orders");
        return response;
    }
}

