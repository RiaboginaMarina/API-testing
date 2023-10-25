package Order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.order.OrderClient;
import org.example.order.OrderValidations;
import org.example.user.Credentials;
import org.example.user.User;
import org.example.user.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetUserOrdersTest {
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
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getAuthorizedUserOrders() {
        var creds = Credentials.from(user);
        userClient.login(creds);
        ValidatableResponse response = client.getAuthorizedUserOrders(accessToken);
        check.getAuthorizedUserOrdersSuccessfully(response);
    }

    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    public void getUnauthorizedUserOrders() {
        ValidatableResponse response = client.getUnauthorizedUserOrders();
        check.getUserOrdersWithoutAuthorizationFailed(response);
    }
}
