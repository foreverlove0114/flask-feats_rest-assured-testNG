package com.restaurant.base;

import com.restaurant.config.TestConfig;
import com.restaurant.utils.RedirectHelper;
import com.restaurant.utils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeMethod;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected String sessionId;
    protected String csrfToken;

    @BeforeSuite
    public void setupSuite() {
        RestAssured.baseURI = TestConfig.BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeMethod
    public void setupMethod() {
        // Reset session for each test
        sessionId = null;
        csrfToken = null;
    }

    protected RequestSpecification getRequest() {
        RequestSpecification request = given()
                .contentType("application/x-www-form-urlencoded")
                .redirects().follow(true);

        if (sessionId != null) {
            request.cookie("session", sessionId);
        }

        return request;
    }

    protected String loginUser(String nickname, String password) {
        // First get CSRF token from login page
        Response loginPage = getRequest().get("/login");
        String csrf = TestUtils.extractCsrfToken(loginPage);

        // Perform login
        Response loginResponse = getRequest()
                .formParam("nickname", nickname)
                .formParam("password", password)
                .formParam("csrf_token", csrf)
                .post("/login");

        // Extract session ID from response cookies
        this.sessionId = loginResponse.getCookie("session");
        return this.sessionId;
    }

    protected String loginTestUser() {
        Response loginPage = getRequest().get("/login");
        String csrfToken = TestUtils.extractCsrfToken(loginPage);
        this.sessionId = loginPage.getCookie("session");

        Response loginResponse = submitFormAndFollowRedirect(
                getRequest()
                        .formParam("nickname", TestConfig.TEST_USER_NICKNAME)
                        .formParam("password", TestConfig.TEST_USER_PASSWORD)
                        .formParam("csrf_token", csrfToken),
                "/login"
        );

        // 更新为登录后的session
        String newSession = loginResponse.getCookie("session");
        if (newSession != null) {
            this.sessionId = newSession;
        }

        return this.sessionId;
    }

    protected String loginAdmin() {
        return loginUser(TestConfig.ADMIN_USERNAME, TestConfig.ADMIN_PASSWORD);
    }

    // 添加辅助方法获取带session的CSRF token
    protected String getCsrfTokenWithSession(String urlPath) {
        Response response = getRequest().get(urlPath);
        return TestUtils.extractCsrfToken(response);
    }

    protected Response executeWithRedirectHandling(RequestSpecification request, String method, String url) {
        Response response = request
                .redirects().follow(false)  // 先不自动重定向
                .request(method, url);

        if (RedirectHelper.isRedirect(response)) {
            // 更新session（重定向后可能有新的session）
            String newSession = response.getCookie("session");
            if (newSession != null) {
                this.sessionId = newSession;
            }

            // 跟随重定向
            return RedirectHelper.followRedirect(response, this.sessionId);
        }

        return response;
    }

    /**
     * 专门用于表单提交后的重定向处理
     */
    protected Response submitFormAndFollowRedirect(RequestSpecification request, String url) {
        // 先不自动重定向，获取初始响应
        Response initialResponse = request
                .redirects().follow(false)
                .post(url);

        // 如果是重定向，手动跟随
        if (initialResponse.getStatusCode() >= 300 && initialResponse.getStatusCode() < 400) {
            String redirectUrl = initialResponse.getHeader("Location");
            if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
                // 更新session
                String newSession = initialResponse.getCookie("session");
                if (newSession != null) {
                    this.sessionId = newSession;
                }
                // 跟随重定向
                return getRequest().get(redirectUrl);
            }
        }

        return initialResponse;
    }

    /**
     * 获取重定向目标URL（用于手动处理）
     */
    protected String getRedirectTarget(Response response) {
        return RedirectHelper.getRedirectUrl(response);
    }
}