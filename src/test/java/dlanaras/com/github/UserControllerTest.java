package dlanaras.com.github;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import com.google.inject.Inject;

import dlanaras.com.github.exceptions.InvalidValueException;
import dlanaras.com.github.models.User;
import dlanaras.com.github.models.dto.Login;
import dlanaras.com.github.services.TokenService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import java.time.LocalDateTime;
import java.time.Month;

@QuarkusTest
public class UserControllerTest {
    @Test
    public void getAllUserUnauthorisedWithoutToken() {
        given()
                .contentType(ContentType.JSON)
                .when().get("/users")
                .then()
                .statusCode(401);
    }

    @Test
    public void getAllUsersWorks() {

        User user = new User();

        try {
            user.setEmail("lfredi0@eepurl.com");
        } catch (InvalidValueException e) {

        }
        user.setPassword("1QWrfIEgoq");
        user.setId(1L);
        user.setAdmin(true);

        String token = new TokenService().createToken(user);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(user)
                .when().get("/users")
                .then()
                .statusCode(200);
    }

    @Test
    public void unauthorizedDeleteWhenNoToken() {
        given()
                .contentType(ContentType.JSON)
                .when().delete("/users/1")
                .then()
                .statusCode(401);
    }

    @Test
    public void deleteWorks() {
        User user = new User();

        try {
            user.setEmail("lfredi0@eepurl.com");
        } catch (InvalidValueException e) {

        }
        user.setPassword("1QWrfIEgoq");
        user.setId(1L);
        user.setAdmin(true);

        String token = new TokenService().createToken(user);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when().delete("/users/1")
                .then()
                .statusCode(204);
    }
}