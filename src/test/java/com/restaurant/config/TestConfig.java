package com.restaurant.config;

public class TestConfig {
    public static final String BASE_URL = System.getProperty("base.url", "http://localhost:8000");
    public static final String TEST_USER_NICKNAME = "testuser_1234";
    public static final String TEST_USER_PASSWORD = "test123";
    public static final String TEST_USER_EMAIL = "testuser_1234@email.com";
    public static final String ADMIN_USERNAME = "superadmin";
    public static final String ADMIN_PASSWORD = "admin123";

    // Timeouts
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int SOCKET_TIMEOUT = 10000;
}