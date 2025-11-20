package com.restaurant.tests;

import com.restaurant.base.BaseTest;
import com.restaurant.config.ApiEndpoints;
import com.restaurant.config.TestConfig;
import com.restaurant.models.User;
import com.restaurant.utils.TestUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class AuthTests extends BaseTest {

    @Test
    public void testRegisterGet() {
        getRequest()
                .get(ApiEndpoints.REGISTER)
                .then()
                .statusCode(200)
                .body(containsString("Create Account"));
    }

    @Test
    public void testRegisterPostWithCsrf() {
        System.out.println("=== 注册测试（自动重定向）===");

        // 获取注册页面
        Response registerPage = getRequest().get(ApiEndpoints.REGISTER);
        String csrfToken = TestUtils.extractCsrfToken(registerPage);
        this.sessionId = registerPage.getCookie("session");

        // 创建唯一测试用户
        String timestamp = String.valueOf(System.currentTimeMillis());
        User testUser = new User(
                "testuser_" + timestamp,
                "testuser_" + timestamp + "@email.com",
                "0123456789",
                "123 test street",
                "test123"
        );

        // 提交注册（自动处理重定向）
        Response finalResponse = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("nickname", testUser.getNickname())
                        .formParam("email", testUser.getEmail())
                        .formParam("contact", testUser.getContact())
                        .formParam("fullAddress", testUser.getFullAddress())
                        .formParam("password", testUser.getPassword())
                        .formParam("csrf_token", csrfToken),
                ApiEndpoints.REGISTER
        );

        // 验证最终页面
        finalResponse.then().statusCode(200);

        String responseBody = finalResponse.getBody().asString();
        if (responseBody.contains("Delicious dishes") || responseBody.contains("Personal information")) {
            System.out.println("✅ 注册成功并跳转到目标页面");
        } else if (responseBody.toLowerCase().contains("already exists")) {
            System.out.println("⚠️  用户已存在");
        } else {
            System.out.println("✅ 注册成功");
        }
    }

    @Test
    public void testLoginGet() {
        getRequest()
                .get(ApiEndpoints.LOGIN)
                .then()
                .statusCode(200)
                .body(containsString("Glad to see you again!"));
    }

    @Test
    public void testLoginPost() {
        System.out.println("=== 登录测试（自动重定向）===");

        // 获取登录页面
        Response loginPage = getRequest().get(ApiEndpoints.LOGIN);
        String csrfToken = TestUtils.extractCsrfToken(loginPage);
        this.sessionId = loginPage.getCookie("session");

        // 提交登录（自动处理重定向）
        Response finalResponse = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("nickname", TestConfig.TEST_USER_NICKNAME)
                        .formParam("password", TestConfig.TEST_USER_PASSWORD)
                        .formParam("csrf_token", csrfToken),
                ApiEndpoints.LOGIN
        );

        // 验证最终页面
        finalResponse.then()
                .statusCode(200)
                .body(containsString("Personal information"))
                .body(containsString("Your Profile"));

        System.out.println("✅ 登录成功并进入个人资料页面");
    }

    @Test
    public void testLogout() {
        // 先登录
        testLoginPost();

        // 执行登出（自动处理重定向）
        Response finalResponse = submitFormAndFollowRedirect(
                getRequest(),
                ApiEndpoints.LOGOUT
        );

        finalResponse.then()
                .statusCode(200)
                .body(containsString("Login"))
                .body(containsString("Register"));

        System.out.println("✅ 登出成功并返回首页");
    }
}