package ru.yandex.practicum.util;

import org.apache.commons.lang.RandomStringUtils;
import ru.yandex.practicum.model.Courier;

public class CourierGenerator {

    public static Courier random() {
        return new Courier("Test" + RandomStringUtils.randomAlphanumeric(5), "123", "BossOfTheGYM");
    }
}