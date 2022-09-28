package dlanaras.com.github;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import dlanaras.com.github.exceptions.InvalidValueException;
import dlanaras.com.github.models.User;
import dlanaras.com.github.models.dto.Login;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import java.time.LocalDateTime;
import java.time.Month;

@QuarkusTest
public class AuthenticationControllerTest {

    @Test
    public void unsuccessfulLogin() {
        Login invalidLogin = new Login();

        invalidLogin.setEmail("invalid@invalid.invalid");
        invalidLogin.setPassword("securepassword");

        given()
                .contentType(ContentType.JSON)
                .body(invalidLogin)
                .when().post("/login")
                .then()
                .statusCode(400);
    }

    @Test
    public void successfulLogin() {
        Login successfulLogin = new Login();

        successfulLogin.setEmail("lfredi0@eepurl.com");
        successfulLogin.setPassword("1QWrfIEgoq");

        given()
                .contentType(ContentType.JSON)
                .body(successfulLogin)
                .when().post("/login")
                .then()
                .statusCode(200);
    }

    @Test
    public void unsuccessfulRegister() {
        User invalidUser = new User();

        given()
                .contentType(ContentType.JSON)
                .body(invalidUser)
                .when().post("/register")
                .then()
                .statusCode(400);
    }

    @Test
    public void successfulRegister() {
        User validUser = new User();

        validUser.setAdmin(true);
        try {
            validUser.setEmail("email@email.co");
        } catch (InvalidValueException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        validUser.setForeName("joe");
        validUser.setLastName("biden");
        validUser.setPassword("securityismypassion");

        given()
                .contentType(ContentType.JSON)
                .body(validUser)

                .when().post("/register")
                .then()
                .statusCode(200);
    }
}