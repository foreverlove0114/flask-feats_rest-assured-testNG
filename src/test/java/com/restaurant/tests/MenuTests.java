package com.restaurant.tests;

import com.restaurant.base.BaseTest;
import com.restaurant.config.ApiEndpoints;
import com.restaurant.config.TestConfig;
import com.restaurant.utils.TestUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class MenuTests extends BaseTest {

    @Test
    public void testGetMenuPage() {
        getRequest()
                .get(ApiEndpoints.MENU)
                .then()
                .statusCode(200)
                .body(containsString("Our Menu"));
    }

    @Test
    public void testGetDetails() {
        // Using a known product ID
        int productId = 2;

        getRequest()
                .get(ApiEndpoints.POSITION + productId)
                .then()
                .statusCode(200)
                .body(containsString("Ingredients:"))
                .body(containsString("Description:"));
    }

    @Test
    public void testAddItemUnauthenticated() {
        int productId = 2;
        String fullProductUrl = TestConfig.BASE_URL + ApiEndpoints.POSITION + productId;

        // Get CSRF token - 使用修复后的方法
        String csrfToken = TestUtils.getCsrfTokenFromPage(fullProductUrl);

        Response response = getRequest()
                .formParam("csrf_token", csrfToken)
                .formParam("num", 1)
                .post(ApiEndpoints.POSITION + productId);

        response.then()
                .statusCode(200)
                .body(containsString("To add an item to the cart, please log in first!"));

        System.out.println("✅ 未登录用户正确被阻止添加商品");
    }

    @Test
    public void testAddItemAuthenticated() {
        loginTestUser();
        int productId = 2;
        String fullProductUrl = TestConfig.BASE_URL + ApiEndpoints.POSITION + productId;

        // Get CSRF token with session - 使用修复后的方法
        String csrfToken = TestUtils.getCsrfTokenFromPage(fullProductUrl, sessionId);

        Response response = getRequest()
                .formParam("csrf_token", csrfToken)
                .formParam("num", 1)
                .post(ApiEndpoints.POSITION + productId);

        response.then()
                .statusCode(200)
                .body(containsString("Item added to cart!"));

        System.out.println("✅ 登录用户成功添加商品到购物车");
    }
}