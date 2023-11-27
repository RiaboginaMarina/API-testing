package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static org.hamcrest.Matchers.*;

public class OrderValidations {
    @Step("Сравнить код, статус и тело ответа с заданными при создании заказа авторизованным пользователем")
    public void creatingOrderWithAuthorizationSuccessfully(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue())
        ;
    }

    @Step("Сравнить код, статус и тело ответа с заданными при создании заказа с пустым списком ингредиентов в теле запроса")

    public void creatingOrderWithoutIngredientsFailed(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"))
        ;
    }

    @Step("Сравнить код и статус ответа с заданными при создании заказа с неверным хэшем ингредиентов")

    public void creatingOrderWithInvalidIngredientHashFailed(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    @Step("Сравнить код и статус ответа с заданными при создании заказа неавторизованным пользователем")

    public void creatingOrderWithoutAuthorizationFailed(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(is(not(HttpURLConnection.HTTP_OK)));
    }

    @Step("Сравнить код, статус и тело ответа с заданными при получении списка заказов авторизованного пользователя")
    public void getAuthorizedUserOrdersSuccessfully(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true))
                .body("orders", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue())
        ;
    }

    @Step("Сравнить код, статус и тело ответа с заданными при получении списка заказов неавторизованного пользователя")
    public void getUserOrdersWithoutAuthorizationFailed(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"))
        ;
    }
}
