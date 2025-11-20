package com.restaurant.config;

public class ApiEndpoints {
    // Auth endpoints
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String LOGOUT = "/logout";
    public static final String PROFILE = "/profile";

    // Menu endpoints
    public static final String MENU = "/menu";
    public static final String POSITION = "/position/";

    // Order endpoints
    public static final String CREATE_ORDER = "/create_order";
    public static final String MY_ORDERS = "/my_orders";
    public static final String MY_ORDER = "/my_order/";
    public static final String CANCEL_ORDER = "/cancel_order/";

    // Reservation endpoints
    public static final String RESERVED = "/reserved";
    public static final String RESERVATIONS_CHECK = "/reservations_check";

    // Admin endpoints
    public static final String ADD_POSITION = "/add_position";
    public static final String EDIT_POSITION = "/edit_position/";
    public static final String MENU_CHECK = "/menu_check";
    public static final String ALL_USERS = "/all_users";

    // Main endpoints
    public static final String HOME = "/";
    public static final String ABOUT = "/about";
    public static final String CONTACT = "/contact";
}