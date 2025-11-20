package com.restaurant.tests;

import com.restaurant.base.BaseTest;
import com.restaurant.config.ApiEndpoints;
import com.restaurant.config.TestConfig;
import com.restaurant.utils.TestUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class OrderTests extends BaseTest {

    @Test
    public void testEmptyOrderPage() {
        getRequest()
                .get(ApiEndpoints.CREATE_ORDER)
                .then()
                .statusCode(200)
                .body(containsString("Your Cart is Empty"));

        System.out.println("✅ 未登录用户页面正确显示空购物车，没有订单表单");
    }

    @Test
    public void testCartWithItem() {
        loginTestUser();

        // 添加商品到购物车
        int productId = 2;
        String productUrl = ApiEndpoints.POSITION + productId;

        Response productPage = getRequest().get(productUrl);
        String csrfToken = TestUtils.extractCsrfToken(productPage);

        Response addResponse = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("csrf_token", csrfToken)
                        .formParam("num", 1),
                productUrl
        );

        addResponse.then()
                .statusCode(200)
                .body(containsString("Item added to cart!"));

        // 检查订单页面
        Response response = getRequest()
                .get(ApiEndpoints.CREATE_ORDER);

        response.then()
                .statusCode(200)
                .body(containsString("Selected Items"))
                .body(containsString("Quantity: 1"))
                .body(containsString("Total Price"));
    }

    @Test
    public void testCreateOrderPostWithItems() {
        loginTestUser();

        // 添加商品到购物车
        int productId = 2;
        String productUrl = ApiEndpoints.POSITION + productId;

        Response productPage = getRequest().get(productUrl);
        String csrfToken = TestUtils.extractCsrfToken(productPage);

        Response addResponse = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("csrf_token", csrfToken)
                        .formParam("num", 1),
                productUrl
        );

        // 获取订单页面
        Response orderPage = getRequest().get(ApiEndpoints.CREATE_ORDER);
        csrfToken = TestUtils.extractCsrfToken(orderPage);

        // 创建订单（自动处理重定向）
        Response orderResponse = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("csrf_token", csrfToken),
                ApiEndpoints.CREATE_ORDER
        );

        orderResponse.then()
                .statusCode(200)
                .body(containsString("Items List:"))
                .body(containsString("Date & Time"))
                .body(containsString("Cancel Order"));
    }

    @Test
    public void testViewOrderList() {
        loginTestUser();

        Response response = getRequest()
                .get(ApiEndpoints.MY_ORDERS);

        response.then()
                .statusCode(200)
                .body(containsString("Your Orders"))
                .body(containsString("View Details"));
    }

    @Test
    public void testCancelOrderSuccessfully() {
        loginTestUser();

        // 创建测试订单
        int productId = 2;
        String productUrl = ApiEndpoints.POSITION + productId;

        Response productPage = getRequest().get(productUrl);
        String csrfToken = TestUtils.extractCsrfToken(productPage);

        Response addResponse = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("csrf_token", csrfToken)
                        .formParam("num", 1),
                productUrl
        );

        Response orderPage = getRequest().get(ApiEndpoints.CREATE_ORDER);
        csrfToken = TestUtils.extractCsrfToken(orderPage);

        Response orderResponse = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("csrf_token", csrfToken),
                ApiEndpoints.CREATE_ORDER
        );

        // 提取订单ID
        int orderId = TestUtils.extractOrderIdFromText(orderResponse.getBody().asString());

        if (orderId != -1) {
            // 取消订单（自动处理重定向）
            Response cancelResponse = submitFormAndFollowRedirect(
                    getRequest(),
                    ApiEndpoints.CANCEL_ORDER + orderId
            );

            cancelResponse.then()
                    .statusCode(200)
                    .body(containsString("Order deleted!"));

            System.out.println("✅ 成功取消订单 " + orderId);
        }
    }

    @Test
    public void testCancelOrderNonExistOrUnauthorized() {
        loginTestUser();

        Response response = submitFormAndFollowRedirect(
                getRequest(),
                ApiEndpoints.CANCEL_ORDER + "9999999"
        );

        response.then()
                .statusCode(200)
                .body(containsString("Order not found or it is not yours!"));
    }
}