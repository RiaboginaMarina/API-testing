package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.order.Order;
import org.example.order.OrderClient;
import org.example.order.OrderValidations;
import org.example.user.Credentials;
import org.example.user.User;
import org.example.user.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class OrderCreationTest {
    private final OrderClient client = new OrderClient();
    private final OrderValidations check = new OrderValidations();
    private final UserClient userClient = new UserClient();
    private final User user = new User("testovoe8934@yandex.ru", "PassWord0912", "AmelyLee");
    private String accessToken;

    @Before
    public void createNewUser() {
        ValidatableResponse response = userClient.register(user);
        accessToken = userClient.getAccessToken(response);
    }

    @After
    public void deleteUser() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void createOrderWithAuthorization() {
        var creds = Credentials.from(user);
        userClient.login(creds);
        Order order = new Order(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa7a"));
        ValidatableResponse response = client.createOrderWithAuthorization(order, accessToken);
        check.creatingOrderWithAuthorizationSuccessfully(response);
    }

    @Test
    @DisplayName("Создание заказа с пустым списком ингредиентов теле запроса")
    public void createOrderWithoutIngredients() {
        var creds = Credentials.from(user);
        userClient.login(creds);
        Order order = new Order(Collections.emptyList());
        ValidatableResponse response = client.createOrderWithAuthorization(order, accessToken);
        check.creatingOrderWithoutIngredientsFailed(response);
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингредиентов в теле запроса")
    public void createOrderWithInvalidIngredientHash() {
        var creds = Credentials.from(user);
        userClient.login(creds);
        Order order = new Order(Arrays.asList("61c0c5a71d1f82001bdaaa", "61c0c5a71d1f82001bdaaa", "61c0c5a71d1f82001bdaaa"));
        ValidatableResponse response = client.createOrderWithAuthorization(order, accessToken);
        check.creatingOrderWithInvalidIngredientHashFailed(response);
    }

    @Test
    @DisplayName("Создание заказа пользователем без авторизации")
    public void createOrderWithoutAuthorization() {
        Order order = new Order(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa7a"));
        ValidatableResponse response = client.createOrderWithoutAuthorization(order);
        check.creatingOrderWithoutAuthorizationFailed(response);
    }
}
