package dlanaras.com.github;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import dlanaras.com.github.exceptions.InvalidValueException;
import dlanaras.com.github.models.User;
import dlanaras.com.github.services.TokenService;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class BookingControllerTest {

    @Test
    public void getAllBookingsUnauthorisedWithoutToken() {
        given()
                .contentType(ContentType.JSON)
                .when().get("/bookings")
                .then()
                .statusCode(401);
    }

    @Test
    public void getAllBookingsWorks() {

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
                .when().get("/bookings")
                .then()
                .statusCode(200);
    }

    @Test
    public void unauthorizedDeleteBookingWhenNoToken() {
        given()
                .contentType(ContentType.JSON)
                .when().delete("/bookings/1")
                .then()
                .statusCode(401);
    }

    @Test
    public void deleteWorks() {
        User user = new User();

        try {
            user.setEmail("lfredi0@eepurl.com");
        } catch (InvalidValueException e) {
            System.out.print(e);
        }
        user.setPassword("1QWrfIEgoq");
        user.setId(1L);
        user.setAdmin(true);

        String token = new TokenService().createToken(user);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when().delete("/bookings/1")
                .then()
                .statusCode(204);
    }

    @Test
    public void getsNewestPriceWorks() {
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
                .when().get("/bookings/price/2")
                .then()
                .statusCode(200);
    }

    @Test
    public void doesNotGetNewestPriceWithoutToken() {
        given()
                .contentType(ContentType.JSON)
                .when().get("/bookings/price/1")
                .then()
                .statusCode(401);
    }
}
