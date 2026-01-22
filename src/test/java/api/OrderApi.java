package api;

import entities.CancelOrderRequest;
import entities.CreateOrderRequest;
import entities.CreateOrderResponse;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderApi {
    public static final List<Integer> createdOrders = new ArrayList<>();

    @Step("Создаем новый заказ")
    public static Response createOrder(CreateOrderRequest order) {
        Response rawResponse = given()
                .contentType(ContentType.JSON)
                .body(order)
                .when().log().all()
                .post("/api/v1/orders");
        CreateOrderResponse response = rawResponse.as(CreateOrderResponse.class);
        createdOrders.add(response.getTrack());
        return rawResponse;
    }

    @Step("Отменяем заказ")
    public static Response cancelOrder(int track) {
        return given()
                .contentType(ContentType.JSON)
                .body(new CancelOrderRequest(track))
                .when().log().all()
                .put("/api/v1/orders/cancel");
    }

    public static void cancelAllCreatedOrders() {
        for (Integer createdOrder : OrderApi.createdOrders) {
            OrderApi.cancelOrder(createdOrder);
        }
    }
}
