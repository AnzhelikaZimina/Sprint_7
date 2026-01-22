import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest extends BaseOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка что в тело ответа возвращается список заказов ")
    @Description("Тест возвращает список заказов и проверяет что поле orders присутствует и не пустое")
    public void shouldReturnListOfOrders() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when().log().all()
                .get("/api/v1/orders");
        checkOrdersListIsNotEmpty(response);
    }

    @Step("Проверяем что лист заказов не пуст")
    public void checkOrdersListIsNotEmpty(Response response) {
        response.then()
                .log().all()
                .assertThat().body("orders", notNullValue());;
    }
}
