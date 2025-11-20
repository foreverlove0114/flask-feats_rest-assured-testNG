package com.restaurant.utils;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CSRFExtractor {

    public static String getCsrfTokenFromPage(String url) {
        Response response = given().get(url);
        return TestUtils.extractCsrfToken(response);
    }

    public static String getCsrfTokenFromPage(String url, String sessionId) {
        Response response = given()
                .cookie("session", sessionId)
                .get(url);
        return TestUtils.extractCsrfToken(response);
    }
}