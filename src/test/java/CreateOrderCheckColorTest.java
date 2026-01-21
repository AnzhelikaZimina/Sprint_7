import entities.OrderRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderCheckColorTest extends BaseOrderTest {
    @Parameterized.Parameters(name = "{index}: Тест с цветами = {0}")
    public static Iterable<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},          // один цвет
                {new String[]{"GREY"}},           // один другой цвет
                {new String[]{"BLACK", "GREY"}},  // оба цвета
                {null}                            // без цвета
        });
    }

    private final String[] colors;

    public CreateOrderCheckColorTest(String[] colors) {
        this.colors = colors;
    }

    @Test
    @DisplayName("Проверка успешного создания заказа с разными вариантами цвета")
    @Description("Тест создаёт заказ с разными вариантами цвета и проверяет, что сервер возвращает 201 и поле track присутствует")
    public void orderShouldBeCreatedSuccessfully() {
        OrderRequest orderRequest = new OrderRequest(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                colors
        );
        Response response = createOrder(orderRequest);
        checkOrderCreatedSuccessfully(response);
    }

    @Step("Проверка, что заказ успешно создан и поле track присутствует")
    public void checkOrderCreatedSuccessfully(Response response) {
        response.then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue());
    }
}
