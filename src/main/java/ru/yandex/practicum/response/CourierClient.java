package ru.yandex.practicum.response;

import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.model.Courier;
import ru.yandex.practicum.model.Credentials;
import ru.yandex.practicum.response.specification.Client;

import java.util.Map;

public class CourierClient extends Client {

    static final String COURIER_PATH = "/courier";

    public ValidatableResponse login(Credentials credentials) {
        return spec()
                .body(credentials)
                .when()
                .post(COURIER_PATH + "/login")
                .then().log().all();
    }

    public ValidatableResponse createCourier(Courier courier) {
        return spec()
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then().log().all();
    }

    public ValidatableResponse delete(int courierId) {
        return spec()
                .body(Map.of("id", courierId))
                .when()
                .delete(COURIER_PATH + "/" + courierId)
                .then().log().all();
    }
}