package com.restaurant.tests;

import com.restaurant.base.BaseTest;
import com.restaurant.config.ApiEndpoints;
import com.restaurant.config.TestConfig;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class MainTests extends BaseTest {

    @Test
    public void testHomePage() {
        Response response = getRequest()
                .get(ApiEndpoints.HOME);

        response.then()
                .statusCode(200)
                .body(containsString("Delicious dishes delivered straight to your home"));
    }

    @Test
    public void testAboutPage() {
        getRequest()
                .get(ApiEndpoints.ABOUT)
                .then()
                .statusCode(200)
                .body(containsString("About Flask & Feats"));
    }

    @Test
    public void testGetContactPage() {
        getRequest()
                .get(ApiEndpoints.CONTACT)
                .then()
                .statusCode(200)
                .body(containsString("Send Us a Message"));
    }

    @Test
    public void testPostContactPage() {
        Response response = getRequest()
                .formParam("name", "testuser")
                .formParam("email", "testuser@gmail.com")
                .formParam("subject", "Complain about...")
                .formParam("message", "I wanna complain about...")
                .post(ApiEndpoints.CONTACT);

        response.then()
                .statusCode(200)
                .body(containsString("We are always happy to hear from you"));
    }
}