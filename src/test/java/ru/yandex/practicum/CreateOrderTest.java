package ru.yandex.practicum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.response.OrderClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotEquals;


import java.util.List;
@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final List<String> color;
    private final int expectedCode;
    private OrderClient orderClient;
    private int track;


    public CreateOrderTest(List<String> color, int expectedCode) {
        this.color = color;
        this.expectedCode = expectedCode;
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        orderClient.cancelOrder(track);
    }

    @Test
    public void createOrderWithValidData() {
        Order order = new Order(color);
        ValidatableResponse orderCreationResponse = orderClient.createOrder(order);
        int statusCode = orderCreationResponse.extract().statusCode();
        track = orderCreationResponse.extract().path("track");

        assertThat(statusCode, equalTo(expectedCode));
        assertNotEquals(0, track);

    }

    @Parameterized.Parameters
    public static Object[][] createOrderWithColor() {
        return new Object[][]{
                {List.of("BLACK"), 201},
                {List.of("GREY"), 201},
                {List.of("BLACK", "GREY"), 201},
                {null, 201}
        };
    }
}