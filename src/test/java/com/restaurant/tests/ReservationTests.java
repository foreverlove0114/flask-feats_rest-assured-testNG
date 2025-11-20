package com.restaurant.tests;

import com.restaurant.base.BaseTest;
import com.restaurant.config.ApiEndpoints;
import com.restaurant.utils.TestUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class ReservationTests extends BaseTest {

    @Test
    public void testGetReservedPageUnauthenticated() {
        getRequest()
                .get(ApiEndpoints.RESERVED)
                .then()
                .statusCode(200)
                .body(containsString("Please log in to access this page."));
    }

    @Test
    public void testGetReservedPageAuthenticated() {
        loginTestUser();

        getRequest()
                .get(ApiEndpoints.RESERVED)
                .then()
                .statusCode(200)
                .body(containsString("Table Reservation"))
                .body(containsString("Table Type:"))
                .body(containsString("Reservation Time:"))
                .body(containsString("Make Reservation"));
    }

    @Test
    public void testReservedDifferentScenarios() {
        loginTestUser();

        Response reservedPage = getRequest().get(ApiEndpoints.RESERVED);
        String csrfToken = TestUtils.extractCsrfToken(reservedPage);

        String futureTime = TestUtils.getFutureTime(1);

        // 创建预约（自动处理重定向）
        Response response = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("table_type", "2")
                        .formParam("time", futureTime)
                        .formParam("csrf_token", csrfToken),
                ApiEndpoints.RESERVED
        );

        response.then().statusCode(200);

        String responseBody = response.getBody().asString();
        if (responseBody.contains("for a 2-person table successfully created!")) {
            System.out.println("✅ 你已经预约成功了！");
        } else if (responseBody.contains("You can have only one active reservation.")) {
            System.out.println("⚠️  你只能有一次的预约！");
        } else if (responseBody.contains("Unfortunately, a reservation for this type of table is currently not available")) {
            System.out.println("⚠️  该时段的桌位已经被预约了！");
        } else {
            System.out.println("✅ 预约请求已完成");
        }
    }
}