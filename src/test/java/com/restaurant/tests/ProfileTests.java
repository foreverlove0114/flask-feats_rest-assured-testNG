package com.restaurant.tests;

import com.restaurant.base.BaseTest;
import com.restaurant.config.ApiEndpoints;
import com.restaurant.utils.TestUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class ProfileTests extends BaseTest {

    @Test
    public void testUserProfileUnauthenticated() {
        getRequest()
                .get(ApiEndpoints.PROFILE)
                .then()
                .statusCode(200)
                .body(containsString("Account Login"));
    }

    @Test
    public void testUserProfileAuthenticated() {
        // 先登录
        loginTestUser();

        getRequest()
                .get(ApiEndpoints.PROFILE)
                .then()
                .statusCode(200)
                .body(containsString("Personal information"))
                .body(containsString("Manage your personal information and settings"))
                .body(containsString("Your Profile"));
    }

    @Test
    public void testChangePassword() {
        loginTestUser();

        // 获取个人资料页面
        Response profilePage = getRequest().get(ApiEndpoints.PROFILE);
        String csrfToken = TestUtils.extractCsrfToken(profilePage);

        // 修改密码（自动处理重定向）
        Response changeResponse = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("oldpassword", "test123")
                        .formParam("newpassword", "newtest123")
                        .formParam("csrf_token", csrfToken),
                "/change-password"
        );

        changeResponse.then()
                .statusCode(200)
                .body(containsString("Password changed successfully!"));

        // 改回原密码
        Response revertResponse = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("oldpassword", "newtest123")
                        .formParam("newpassword", "test123")
                        .formParam("csrf_token", csrfToken),
                "/change-password"
        );

        revertResponse.then()
                .statusCode(200)
                .body(containsString("Password changed successfully!"));

        System.out.println("✅ 密码更改测试成功，已将密码恢复");
    }

    @Test
    public void testChangePasswordMismatch() {
        loginTestUser();

        Response profilePage = getRequest().get(ApiEndpoints.PROFILE);
        String csrfToken = TestUtils.extractCsrfToken(profilePage);

        Response response = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("oldpassword", "test1234")
                        .formParam("newpassword", "newtest123")
                        .formParam("csrf_token", csrfToken),
                "/change-password"
        );

        response.then()
                .statusCode(200)
                .body(containsString("Passwords do not match!"));

        System.out.println("✅ 测试密码不匹配成功");
    }

    @Test
    public void testChangeAddress() {
        loginTestUser();

        Response profilePage = getRequest().get(ApiEndpoints.PROFILE);
        String csrfToken = TestUtils.extractCsrfToken(profilePage);

        Response response = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("new_address", "123 street")
                        .formParam("csrf_token", csrfToken),
                "/change-address"
        );

        response.then()
                .statusCode(200)
                .body(containsString("Address updated successfully!"));
    }
}