package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.Client;

public class OrderClient extends Client {
    public static final String ORDER_PATH = "/orders";

    @Step("Отправить POST запрос с токеном авторизации на ручку /api/orders")

    public ValidatableResponse createOrderWithAuthorization(Order order, String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then().log().all()
                ;
    }

    @Step("Отправить POST запрос без токена авторизации на ручку /api/orders")

    public ValidatableResponse createOrderWithoutAuthorization(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then().log().all()
                ;
    }

    @Step("Отправить GET запрос с токеном авторизации на ручку /api/orders")

    public ValidatableResponse getAuthorizedUserOrders(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_PATH)
                .then().log().all()
                ;
    }

    @Step("Отправить GET запрос без токена авторизации на ручку /api/orders")

    public ValidatableResponse getUnauthorizedUserOrders() {
        return spec()
                .get(ORDER_PATH)
                .then().log().all()
                ;
    }
}
