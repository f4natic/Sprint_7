package ru.yandex.practicum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.model.Courier;
import ru.yandex.practicum.model.Credentials;
import ru.yandex.practicum.response.CourierClient;
import ru.yandex.practicum.util.CourierGenerator;

import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class LoginCourierNegativeTest {

    private CourierClient courierClient;
    private int courierId;

    private Courier courier;
    private ValidatableResponse loginResponse;
    private int statusCode;
    private String message;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Can't Login Without Login In Body")
    public void courierCantLoginWithoutLoginInBody400() {
        String password = RandomStringUtils.randomAlphanumeric(10);
        Credentials creds = new Credentials(password);

        ValidatableResponse loginResponse = courierClient.login(creds);
        int statusCode = loginResponse.extract().statusCode();
        String massage = loginResponse.extract().path("message");

        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_BAD_REQUEST));
        assertThat(massage, equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Can't Login With Null Login")
    public void courierCantLoginWithNullLogin400() {
        Courier courier = CourierGenerator.random();
        ValidatableResponse loginResponse = courierClient.login(new Credentials(null, courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_BAD_REQUEST));
        assertThat(message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Can't Login With Incorrect Login")
    public void courierCantLoginWithIncorrectLogin404() {
        createCourier();
        validate();
        auth();
        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        assertThat(message, equalTo("Учетная запись не найдена"));
    }

    @Step
    private void createCourier() {
        courier = CourierGenerator.random();
        courierClient.createCourier(courier);
    }

    @Step
    private void validate() {
        Credentials creds = Credentials.from(courier);
        ValidatableResponse loginResponse = courierClient.login(creds);
        courierId = loginResponse.extract().path("id");
    }

    @Step
    private void auth() {
        loginResponse = courierClient.login(new Credentials("         " + courier.getLogin(), courier.getPassword()));
        statusCode = loginResponse.extract().statusCode();
        message = loginResponse.extract().path("message");
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Can't Login With Incorrect Password")
    public  void courierCantLoginWithIncorrectPassword404() {
        Courier courier = CourierGenerator.random();
        courierClient.createCourier(courier);

        Credentials creds = Credentials.from(courier);
        ValidatableResponse loginResponse = courierClient.login(creds);
        courierId = loginResponse.extract().path("id");

        loginResponse =  courierClient.login(new Credentials(courier.getLogin(), "        " + courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        courierClient.delete(courierId);

        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        assertThat(message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Can't Login Without Data")
    public void courierCantLoginWithoutCreatedData404() {
        ValidatableResponse loginResponse = courierClient.login(new Credentials("loginWithoutCreation", "sOmepssw"));
        int statusCode = loginResponse.extract().statusCode();
        String message =  loginResponse.extract().path("message");

        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        assertThat(message, equalTo("Учетная запись не найдена"));
    }
}