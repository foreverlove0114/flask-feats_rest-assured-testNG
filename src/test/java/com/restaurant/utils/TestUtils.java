package com.restaurant.utils;

import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class TestUtils {

    public static String extractCsrfToken(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        Elements csrfInput = doc.select("input[name=csrf_token]");
        return csrfInput.attr("value");
    }

    public static String extractCsrfToken(Response response) {
        return extractCsrfToken(response.getBody().asString());
    }

    public static Map<String, String> getAuthCookies(String sessionId) {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("session", sessionId);
        return cookies;
    }

    public static int extractOrderIdFromText(String text) {
        // 匹配 "Your Order №5" 格式
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("Your Order №(\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }

    public static String getFutureTime(int days) {
        java.time.LocalDateTime future = java.time.LocalDateTime.now().plusDays(days);
        return future.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    // 统一的方法 - 使用完整URL和可选的sessionId
    public static String getCsrfTokenFromPage(String fullUrl) {
        Response response = given().get(fullUrl);
        return extractCsrfToken(response);
    }

    public static String getCsrfTokenFromPage(String fullUrl, String sessionId) {
        Response response = given()
                .cookie("session", sessionId)
                .get(fullUrl);
        return extractCsrfToken(response);
    }

    // 如果需要基于baseUrl和path的方法，可以这样写：
    public static String getCsrfTokenFromPage(String baseUrl, String path, String sessionId) {
        return getCsrfTokenFromPage(baseUrl + path, sessionId);
    }
}