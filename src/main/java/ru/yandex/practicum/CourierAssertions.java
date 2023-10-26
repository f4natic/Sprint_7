package ru.yandex.practicum;

import io.restassured.response.ValidatableResponse;
import java.net.HttpURLConnection;
import static org.hamcrest.core.Is.is;

public class CourierAssertions {
    public  int loggedInSuccessfully(ValidatableResponse loginResponse) {
        int id =  loginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract()
                .path("id");
        return id;
    }

    public  void createdSuccessfully(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .body("ok", is(true));
    }

    public void deletedSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_OK);
    }

    public void createdUnsuccessfully409(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HttpURLConnection.HTTP_CONFLICT);
    }
}