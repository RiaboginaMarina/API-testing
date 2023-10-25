package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChangingUserDataTest {
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
    @DisplayName("Изменение имени авторизованного пользователя")
    public void changeUserNameWithAuthorization() {
        var creds = Credentials.from(user);
        client.login(creds);
        var userWithChangeData = new User(null, "AmelyLee");
        ValidatableResponse response = client.changeAuthorizedUserData(userWithChangeData, accessToken);
        ChangingResponseBody body = check.changingAuthorizedUserDataSuccessfully(response);
        Assert.assertEquals(user.getEmail(), body.getUser().getEmail());
        Assert.assertEquals(userWithChangeData.getName(), body.getUser().getName());
    }

    @Test
    @DisplayName("Изменение адреса почты авторизованного пользователя")
    public void changeUserEmailWithAuthorization() {
        var creds = Credentials.from(user);
        client.login(creds);
        var userWithChangeData = new User("newadress1234@yandex.ru", null);
        ValidatableResponse response = client.changeAuthorizedUserData(userWithChangeData, accessToken);
        ChangingResponseBody body = check.changingAuthorizedUserDataSuccessfully(response);
        Assert.assertEquals(user.getName(), body.getUser().getName());
        Assert.assertEquals(userWithChangeData.getEmail(), body.getUser().getEmail());
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    public void changeUserNameWithoutAuthorization() {
        var userWithChangeData = new User(null, "AmelyLee");
        ValidatableResponse response = client.changeUnauthorizedUserData(userWithChangeData);
        check.changeUserDataWithoutAuthorizationFailed(response);
    }

    @Test
    @DisplayName("Изменение адреса почты пользователя без авторизации")
    public void changeUserEmailWithoutAuthorization() {
        var userWithChangeData = new User("newadress1234@yandex.ru", null);
        ValidatableResponse response = client.changeUnauthorizedUserData(userWithChangeData);
        check.changeUserDataWithoutAuthorizationFailed(response);
    }

    @Test
    @DisplayName("Изменение почты аторизованного пользователя на уже существующую")
    public void changeAuthorizedUserEmailToExistingOne() {
        var userWithSameEmail = new User("newadress1234@yandex.ru", "Parole1234", "Dale");
        ValidatableResponse responseForUserWithSameEmail = client.register(userWithSameEmail);
        String accessTokenForUserWithSameEmail = client.getAccessToken(responseForUserWithSameEmail);
        var creds = Credentials.from(user);
        client.login(creds);
        var userWithChangeData = new User("newadress1234@yandex.ru", null);
        ValidatableResponse response = client.changeAuthorizedUserData(userWithChangeData, accessToken);
        check.changeEmailToExistingOneFailed(response);
        client.delete(accessTokenForUserWithSameEmail);
    }
}
