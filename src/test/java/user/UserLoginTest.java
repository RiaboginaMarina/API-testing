package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserLoginTest {
    private final UserClient client = new UserClient();
    private final UserValidations check = new UserValidations();
    private final User user = new User("testovoe8934@yandex.ru", "PassWord0912", "AmelyLu");
    private String accessToken;

    @Before
    public void registerNewUser() {
        ValidatableResponse response = client.register(user);
        accessToken = client.getAccessToken(response);
    }

    @After
    public void deleteUser() {
        client.delete(accessToken);
    }

    @Test
    @DisplayName("Успешная авторизация пользователя")
    public void userLoggedIn() {
        var creds = Credentials.from(user);
        ValidatableResponse loginResponse = client.login(creds);
        ResponseBodyFromRegisterAndLogin body = check.loggedInSuccessfully(loginResponse);
        Assert.assertEquals(creds.getEmail(), body.getUser().getEmail());
        Assert.assertEquals(user.getName(), body.getUser().getName());
    }

    @Test
    @DisplayName("Авторизация при неправильном email")
    public void userLoggedInWithWrongEmail() {
        var creds = new Credentials("test8934@yandex.ru", user.getPassword());
        ValidatableResponse loginResponse = client.login(creds);
        check.logInFailedDueWrongData(loginResponse);
    }

    @Test
    @DisplayName("Авторизация при неправильном пароле")
    public void userLoggedInWithWrongPassword() {
        var creds = new Credentials(user.getEmail(), "Password1234");
        ValidatableResponse loginResponse = client.login(creds);
        check.logInFailedDueWrongData(loginResponse);
    }

    @Test
    @DisplayName("Авторизация без email")
    public void userLoggedInWithoutEmail() {
        var creds = new Credentials(null, user.getPassword());
        ValidatableResponse loginResponse = client.login(creds);
        check.logInFailedDueWrongData(loginResponse);
    }

    @Test
    @DisplayName("Авторизация без пароля")
    public void userLoggedInWithoutPassword() {
        var creds = new Credentials(user.getEmail(), null);
        ValidatableResponse loginResponse = client.login(creds);
        check.logInFailedDueWrongData(loginResponse);
    }
}
