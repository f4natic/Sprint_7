package ru.yandex.practicum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.model.Courier;
import ru.yandex.practicum.model.Credentials;
import ru.yandex.practicum.response.CourierClient;
import ru.yandex.practicum.util.CourierGenerator;

import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotEquals;

public class LoginCourierTest {

    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    @Test
    public void courierCanLoginWithValidCredentials() {
        Courier courier = CourierGenerator.random();
        courierClient.createCourier(courier);

        Credentials creds = Credentials.from(courier);
        ValidatableResponse loginResponse = courierClient.login(creds);
        int statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");

        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_OK));
        assertNotEquals(0, courierId);
    }
}