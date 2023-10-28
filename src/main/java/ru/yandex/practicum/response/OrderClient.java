package ru.yandex.practicum.response;

import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.response.specification.Client;

public class OrderClient extends Client {

    static final String ORDER_PATH = "/orders";

    public ValidatableResponse createOrder (Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    public ValidatableResponse cancelOrder (int track) {
        String json = String.format("{\"track\": %d}", track);
        return spec()
                .body(json)
                .when()
                .put(ORDER_PATH)
                .then().log().all();
    }

    public ValidatableResponse getlOrderList () {
        return spec()
                .get(ORDER_PATH)
                .then().log().all();
    }
}