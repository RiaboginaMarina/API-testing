package org.example.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.Client;

import static org.hamcrest.Matchers.notNullValue;

public class UserClient extends Client {
    public static final String AUTH_PATH = "/auth";

    @Step("Отправить POST запрос на ручку /api/auth/register")

    public ValidatableResponse register(User user) {
        return spec()
                .body(user)
                .when()
                .post(AUTH_PATH + "/register")
                .then().log().all()
                ;
    }

    @Step("Отправить POST запрос на ручку /api/auth/login")

    public ValidatableResponse login(Credentials creds) {
        return spec()
                .body(creds)
                .when()
                .post(AUTH_PATH + "/login")
                .then().log().all()
                ;
    }

    @Step("Отправить PATCH запрос с токеном авторизации на ручку /api/auth/user")

    public ValidatableResponse changeAuthorizedUserData(User user, String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(AUTH_PATH + "/user")
                .then().log().all()
                ;
    }

    @Step("Отправить PATCH запрос без токена авторизации на ручку /api/auth/user")
    public ValidatableResponse changeUnauthorizedUserData(User user) {
        return spec()
                .body(user)
                .when()
                .patch(AUTH_PATH + "/user")
                .then().log().all()
                ;
    }

    @Step("Получение токена авторизации из тела запроса")

    public String getAccessToken(ValidatableResponse response) {
        return response
                .assertThat()
                .body("accessToken", notNullValue())
                .extract().path("accessToken")
                ;
    }

    @Step("Отправить DELETE запрос с токеном авторизации на ручку /api/auth/user")

    public void delete(String accessToken) {
        spec()
                .header("Authorization", accessToken)
                .delete(AUTH_PATH + "/user")
        ;
    }
}

