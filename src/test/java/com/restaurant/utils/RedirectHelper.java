package com.restaurant.utils;

import io.restassured.response.Response;

public class RedirectHelper {

    /**
     * 从响应中提取重定向URL
     */
    public static String getRedirectUrl(Response response) {
        String locationHeader = response.getHeader("Location");
        if (locationHeader != null && !locationHeader.trim().isEmpty()) {
            return locationHeader.startsWith("/") ? locationHeader : "/" + locationHeader;
        }

        // 如果没有Location头，尝试从HTML内容中提取
        String body = response.getBody().asString();
        if (body.contains("href=")) {
            // 简单提取，如果需要更复杂的解析可以使用JSoup
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("href=\"([^\"]+)\"");
            java.util.regex.Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                String href = matcher.group(1);
                if (!href.startsWith("http")) { // 只处理相对路径
                    return href;
                }
            }
        }

        return null; // 没有找到重定向URL
    }

    /**
     * 验证响应是否是重定向
     */
    public static boolean isRedirect(Response response) {
        int statusCode = response.getStatusCode();
        return statusCode >= 300 && statusCode < 400;
    }

    /**
     * 执行重定向并返回最终响应
     */
    public static Response followRedirect(Response redirectResponse, String sessionId) {
        String redirectUrl = getRedirectUrl(redirectResponse);
        if (redirectUrl == null) {
            throw new RuntimeException("无法从响应中提取重定向URL");
        }

        return io.restassured.RestAssured.given()
                .cookie("session", sessionId)
                .get(redirectUrl);
    }
}