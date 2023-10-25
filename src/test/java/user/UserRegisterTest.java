package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.ResponseBodyFromRegisterAndLogin;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserValidations;
import org.junit.Assert;
import org.junit.Test;

public class UserRegisterTest {
    private final UserClient client = new UserClient();
    private final UserValidations check = new UserValidations();
    private String accessToken;


    @Test
    @DisplayName("Регистрация нового пользователя")
    public void registerNewUser() {
        var user = new User("testovoe8934@yandex.ru", "PassWord0912", "AmelyLu");
        ValidatableResponse response = client.register(user);
        ResponseBodyFromRegisterAndLogin body = check.registerSuccessfully(response);
        Assert.assertEquals(user.getEmail(), body.getUser().getEmail());
        Assert.assertEquals(user.getName(), body.getUser().getName());
        accessToken = body.getAccessToken();
        client.delete(accessToken);
    }

    @Test
    @DisplayName("Регистрация двух одинаковых пользователей")
    public void registerTwoIdenticalUsers() {
        var user = new User("testovoe8934@yandex.ru", "PassWord0912", "AmelyLu");
        ValidatableResponse responseFirstUser = client.register(user);
        accessToken = client.getAccessToken(responseFirstUser);
        ValidatableResponse responseSecondUser = client.register(user);
        check.registerTwoIdenticalUserFailed(responseSecondUser);
        client.delete(accessToken);
    }

    @Test
    @DisplayName("Регистраиця пользователя без email")
    public void registerWithoutEmail() {
        var user = new User(null, "PassWord0912", "AmelyLu");
        ValidatableResponse response = client.register(user);
        check.registerWithoutFullDataFailed(response);
    }

    @Test
    @DisplayName("Регистрация пользователя без пароля")
    public void registerWithoutPassword() {
        var user = new User("testovoe8934@yandex.ru", null, "AmelyLu");
        ValidatableResponse response = client.register(user);
        check.registerWithoutFullDataFailed(response);
    }

    @Test
    @DisplayName("Регистрация пользователя без имени")
    public void registerWithoutName() {
        var user = new User("testovoe8934@yandex.ru", "PassWord0912", null);
        ValidatableResponse response = client.register(user);
        check.registerWithoutFullDataFailed(response);
    }
}
