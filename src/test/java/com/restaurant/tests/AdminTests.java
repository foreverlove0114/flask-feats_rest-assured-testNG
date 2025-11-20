package com.restaurant.tests;

import com.restaurant.base.BaseTest;
import com.restaurant.config.ApiEndpoints;
import com.restaurant.utils.TestUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class AdminTests extends BaseTest {

    @Test
    public void testGetAddPosition() {
        loginAdmin();

        getRequest()
                .get(ApiEndpoints.ADD_POSITION)
                .then()
                .statusCode(200)
                .body(containsString("Adding a new item"))
                .body(containsString("Add product"));
    }

    @Test
    public void testGetAddPositionUser() {
        loginTestUser();

        getRequest()
                .get(ApiEndpoints.ADD_POSITION)
                .then()
                .statusCode(200)
                .body(containsString("Access denied! Only administrators can add new items"));
    }

    @Test
    public void testGetEditPosition() {
        loginAdmin();

        getRequest()
                .get(ApiEndpoints.EDIT_POSITION + "2")
                .then()
                .statusCode(200)
                .body(containsString("Editing item"))
                .body(containsString("Active"));
    }

    @Test
    public void testGetEditPositionUser() {
        loginTestUser();

        getRequest()
                .get(ApiEndpoints.EDIT_POSITION + "2")
                .then()
                .statusCode(200)
                .body(containsString("Access denied! Only administrators can edit menu."));
    }

    @Test
    public void testUserCheckReservation() {
        loginTestUser();

        getRequest()
                .get(ApiEndpoints.RESERVATIONS_CHECK)
                .then()
                .statusCode(200)
                .body(containsString("Access denied! Only administrators can check reservations"));
    }

    @Test
    public void testAdminCheckReservation() {
        loginAdmin();

        getRequest()
                .get(ApiEndpoints.RESERVATIONS_CHECK)
                .then()
                .statusCode(200)
                .body(containsString("Checking reservations"));
    }

    @Test
    public void testUserCheckMenu() {
        loginTestUser();

        getRequest()
                .get(ApiEndpoints.MENU_CHECK)
                .then()
                .statusCode(200)
                .body(containsString("Access denied! Only administrators can check menu"));
    }

    @Test
    public void testAdminCheckMenu() {
        loginAdmin();

        getRequest()
                .get(ApiEndpoints.MENU_CHECK)
                .then()
                .statusCode(200)
                .body(containsString("Перевірка меню"));
    }

    @Test
    public void testAdminGetAllUsers() {
        loginAdmin();

        getRequest()
                .get(ApiEndpoints.ALL_USERS)
                .then()
                .statusCode(200)
                .body(containsString("Users"));
    }
}