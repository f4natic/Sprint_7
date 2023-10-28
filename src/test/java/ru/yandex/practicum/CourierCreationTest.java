package ru.yandex.practicum;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practicum.model.Courier;
import ru.yandex.practicum.model.Credentials;
import ru.yandex.practicum.response.CourierAssertions;
import ru.yandex.practicum.response.CourierClient;
import ru.yandex.practicum.util.CourierGenerator;

public class CourierCreationTest {

    private final CourierClient client = new CourierClient();
    private final CourierAssertions check = new CourierAssertions();
    protected int courierId;


    @After
    public void deleteCourier() {
        ValidatableResponse delete = client.delete(courierId);
        check.deletedSuccessfully(delete);
    }


    @Test
    public void courierCreationPositiveTest() {
        Courier courier = CourierGenerator.random();
        ValidatableResponse response = client.createCourier(courier);
        check.createdSuccessfully(response);

        Credentials credentials = Credentials.from(courier);
        ValidatableResponse loginResponse = client.login(credentials);
        courierId = check.loggedInSuccessfully(loginResponse);

        assert courierId != 0;
    }

    @Test
    public void courierCreationWithSameCredentials() {
        Courier courier = CourierGenerator.random();
        ValidatableResponse response = client.createCourier(courier);
        check.createdSuccessfully(response);

        ValidatableResponse response1 = client.createCourier(courier);
        check.createdUnsuccessfully409(response1);

        Credentials credentials = Credentials.from(courier);
        ValidatableResponse loginResponse = client.login(credentials);
        courierId = check.loggedInSuccessfully(loginResponse);

        assert courierId != 0;
    }
}