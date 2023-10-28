package ru.yandex.practicum;

import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.response.OrderClient;

import java.net.HttpURLConnection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotEquals;

public class GetOrderListTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    public void getListOrder() {
        ValidatableResponse listOrderResponse = orderClient.getlOrderList();
        int statusCode = listOrderResponse.extract().statusCode();
        List<Object> orders = listOrderResponse.extract().path("orders");

        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_OK));
        assertNotEquals(0, orders.size());
    }
}