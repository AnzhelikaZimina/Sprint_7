import api.OrderApi;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

public class BaseOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void tearDown() {
        OrderApi.cancelAllCreatedOrders();
    }
}
