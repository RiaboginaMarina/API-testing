package org.example.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UserValidations {
    @Step("Сравнить код, статус и тело ответа с заданными при успешной регистрации нового пользователя")
    public ResponseBodyFromRegisterAndLogin registerSuccessfully(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract()
                .as(ResponseBodyFromRegisterAndLogin.class)
                ;
    }

    @Step("Сравнить код, статус и тело ответа с заданными при успешной авторизации пользователя")
    public ResponseBodyFromRegisterAndLogin loggedInSuccessfully(ValidatableResponse loginResponse) {
        return loginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract()
                .as(ResponseBodyFromRegisterAndLogin.class)
                ;
    }

    @Step("Сравнить код, статус и тело ответа с заданными при изменении данных авторизованным пользователем")
    public ChangingResponseBody changingAuthorizedUserDataSuccessfully(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true))
                .extract()
                .as(ChangingResponseBody.class)
                ;
    }

    @Step("Сравнить код, статус и тело ответа с заданными при регистрации двух одинаковых пользователей")
    public void registerTwoIdenticalUserFailed(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("User already exists"))
        ;
    }

    @Step("Сравнить код, статус и тело ответа с заданными при регистрации пользователя без одного из обязательных полей")
    public void registerWithoutFullDataFailed(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"))
        ;
    }

    @Step("Сравнить код, статус и тело ответа с заданными при авторизации с некорректными паролем или email")
    public void logInFailedDueWrongData(ValidatableResponse loginResponse) {
        loginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"))
        ;
    }

    @Step("Сравнить код, статус и тело ответа с заданными при попытке изменить данные без авторизации")
    public void changeUserDataWithoutAuthorizationFailed(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"))
        ;
    }

    @Step("Сравнить код, статус и тело ответа с заданными при смене пароля на уже существующий")
    public void changeEmailToExistingOneFailed(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("User with such email already exists"))
        ;
    }
}
