package de.hsos.roomplanner;

import static org.junit.jupiter.api.Assertions.fail;

import java.net.URL;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import de.hsos.roomplanner.furniture.boundary.rest.FurnitureResource;
import de.hsos.roomplanner.furniture.control.FurnitureService;
import de.hsos.roomplanner.furniture.control.dto.FurnitureDto;
import de.hsos.roomplanner.furniture.control.dto.FurnitureDtoCreateUpdate;
import de.hsos.roomplanner.user.control.UserService;
import de.hsos.roomplanner.util.exception.EntityAlreadyExistsException;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;
import de.hsos.roomplanner.util.Dimension;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.color.Color;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Christoph Freimuth
 */
@QuarkusTest
@TestHTTPEndpoint(FurnitureResource.class)
public class FurnitureResourceTest {

    public static final String TEST_USER_NAME = "TEST_USER";

    @TestHTTPResource("/user/login")
    URL loginUrl;

    @Inject
    UserService userService;

    @Inject
    FurnitureService furnitureService;

    @Inject
    ObjectMapper objectMapper;

    public void setupTestUser() {

        try {
            this.userService.createUser(TEST_USER_NAME, TEST_USER_NAME);
        } catch (EntityAlreadyExistsException e) {
        }

    }

    public void clearTestUserFurniture() {

        try {
            Page<FurnitureDto> furnitures = this.furnitureService
                    .findFurnitures(TEST_USER_NAME, "", Integer.MAX_VALUE, 0);
            for (FurnitureDto furniture : furnitures.getData()) {
                this.furnitureService.deleteFurniture(TEST_USER_NAME, furniture.getId());
            }
        } catch (Exception e) {
            fail(e);
        }

    }

    public FurnitureDto createTestFurniture(String name, float width, float height, String color){

        FurnitureDtoCreateUpdate testFurniture = new FurnitureDtoCreateUpdate();

        testFurniture.setDimensions(new Dimension(width, height));
        testFurniture.setColor(new Color(color));
        testFurniture.setName(name);

        try {
            return this.furnitureService.createFurniture(TEST_USER_NAME, testFurniture);
        } catch (UserNotFoundException e) {
            fail(e);
            return null;
        }

    }

    @Test
    public void testGetFurnituresNoAuth() {

        given().when()
                .redirects()
                .follow(false)
                .get()
                .then()
                .statusCode(302)
                .and()
                .header("Location", loginUrl.toString());

    }

    @Test
    public void testGetFurnitureNoAuth() {

        given().when()
                .redirects()
                .follow(false)
                .get("/123")
                .then()
                .statusCode(302)
                .and()
                .header("Location", loginUrl.toString());

    }

    @Test
    public void testPostFurnitureNoAuth() {

        given().contentType(ContentType.JSON)
                .when()
                .redirects()
                .follow(false)
                .post()
                .then()
                .statusCode(302)
                .and()
                .header("Location", loginUrl.toString());

    }

    @Test
    public void testPutFurnitureNoAuth() {

        given().contentType(ContentType.JSON)
                .when()
                .redirects()
                .follow(false)
                .put("/123")
                .then()
                .statusCode(302)
                .and()
                .header("Location", loginUrl.toString());

    }

    @Test
    public void testDeleteFurnitureNoAuth() {

        given().when()
                .redirects()
                .follow(false)
                .delete("/123")
                .then()
                .statusCode(302)
                .and()
                .header("Location", loginUrl.toString());

    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testGetFurnituresEmpty() {

        this.setupTestUser();
        this.clearTestUserFurniture();

        given().when()
                .get()
                .then()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("totalCount", is(0))
                .body("data", empty());

    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testGetFurnitures() {

        this.setupTestUser();
        this.clearTestUserFurniture();

        String testName = "Testname";
        float testWidth = 1;
        float testHeight = 2;
        String testColor = "#ffffff";

        this.createTestFurniture(testName, testWidth, testHeight, testColor);

        given().when()
                .get()
                .then()
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("totalCount", is(1))
                .body("data", hasSize(1))
                .body("data.name", hasItem(equalTo(testName)))
                .body("data.dimensions.width", hasItem(equalTo(testWidth)))
                .body("data.dimensions.height", hasItem(equalTo(testHeight)))
                .body("data.color", hasItem(equalTo(testColor)));

    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testGetFurnituresListing() {

        this.setupTestUser();
        this.clearTestUserFurniture();

        String testName = "testName";
        float testWidth = 3;
        float testHeight = 4;
        String testColor = "#ffffff";
        int furnitureCount = 10;
        int[] pageSizes = { 2, 5, 10 };

        for (int i = 0; i < furnitureCount; i++) {
            this.createTestFurniture(testName, testWidth, testHeight, testColor);
        }

        for (int pageSize : pageSizes) {
            given().queryParam("page[size]", pageSize)
                    .when()
                    .get()
                    .then()
                    .statusCode(200)
                    .and()
                    .contentType(ContentType.JSON)
                    .and()
                    .body("totalCount", is(furnitureCount))
                    .body("data", hasSize(pageSize));
        }

    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testGetFurniture() {

        this.setupTestUser();
        this.clearTestUserFurniture();

        String testName = "testName";
        float testWidth = 5;
        float testHeight = 6;
        String testColor = "#ffffff";

        long id = this.createTestFurniture(testName, testWidth, testHeight, testColor).getId();

        given().when()
                .get("/" + id)
                .then()
                .contentType(ContentType.JSON)
                .and()
                .body("name", is(testName))
                .body("dimensions.width", is(testWidth))
                .body("dimensions.height", is(testHeight))
                .body("color", is(testColor))
                .body("id", is(Long.valueOf(id).intValue()));

    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testGetFurnitureNotFound() {

        this.setupTestUser();
        this.clearTestUserFurniture();

        long id = 123;

        given().when().get("/" + id).then().statusCode(404);

    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testPostFurniture() {

        this.setupTestUser();
        this.clearTestUserFurniture();

        String testName = "testName";
        float testWidth = 7;
        float testHeight = 8;
        String testColor = "#ffffff";

        FurnitureDtoCreateUpdate furnitureDtoCreateUpdate = new FurnitureDtoCreateUpdate();

        furnitureDtoCreateUpdate.setColor(new Color(testColor));
        furnitureDtoCreateUpdate.setDimensions(new Dimension(testWidth, testHeight));
        furnitureDtoCreateUpdate.setName(testName);

        String bodyString;
        try {
            bodyString = objectMapper.writeValueAsString(furnitureDtoCreateUpdate);
        } catch (JsonProcessingException e) {
            fail(e);
            return;
        }

        given().contentType(ContentType.JSON)
                .body(bodyString)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("name", is(testName))
                .body("dimensions.width", is(testWidth))
                .body("dimensions.height", is(testHeight))
                .body("color", is(testColor));

    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testPutFurniture() {

        this.setupTestUser();
        this.clearTestUserFurniture();

        long id = this.createTestFurniture("otherName", 12, 13, "#000000").getId();

        String testName = "testName";
        float testWidth = 9;
        float testHeight = 10;
        String testColor = "#ffffff";

        FurnitureDtoCreateUpdate furnitureDtoCreateUpdate = new FurnitureDtoCreateUpdate();

        furnitureDtoCreateUpdate.setColor(new Color(testColor));
        furnitureDtoCreateUpdate.setDimensions(new Dimension(testWidth, testHeight));
        furnitureDtoCreateUpdate.setName(testName);

        String bodyString;
        try {
            bodyString = objectMapper.writeValueAsString(furnitureDtoCreateUpdate);
        } catch (JsonProcessingException e) {
            fail(e);
            return;
        }

        given().contentType(ContentType.JSON)
                .body(bodyString)
                .when()
                .put("/" + id)
                .then()
                .statusCode(200)
                .body("name", is(testName))
                .body("dimensions.width", is(testWidth))
                .body("dimensions.height", is(testHeight))
                .body("color", is(testColor));

    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testPutFurnitureNotFound() {

        this.setupTestUser();
        this.clearTestUserFurniture();

        String testName = "testName";
        float testWidth = 11;
        float testHeight = 12;
        String testColor = "#ffffff";

        FurnitureDtoCreateUpdate furnitureDtoCreateUpdate = new FurnitureDtoCreateUpdate();

        furnitureDtoCreateUpdate.setName(testName);
        furnitureDtoCreateUpdate.setDimensions(new Dimension(testWidth, testHeight));
        furnitureDtoCreateUpdate.setColor(new Color(testColor));

        long id = 123;

        String bodyString;
        try {
            bodyString = objectMapper.writeValueAsString(furnitureDtoCreateUpdate);
        } catch (JsonProcessingException e) {
            fail(e);
            return;
        }

        given().contentType(ContentType.JSON).body(bodyString).when().put("/" + id).then().statusCode(404);

    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testDeleteFurniture() {

        this.setupTestUser();

        long id = this.createTestFurniture("testName", 10, 10, "#ffffff").getId();

        given().contentType(ContentType.JSON).when().delete("/" + id).then().statusCode(200);

        try {
            this.furnitureService.findFurniture(TEST_USER_NAME, id);
            fail();
        } catch (EntityDoesNotExistException ex) {

        } catch (UserNotFoundException ex) {
            fail(ex);
        }

    }

}
