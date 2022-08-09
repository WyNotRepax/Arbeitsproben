package de.hsos.roomplanner;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import de.hsos.roomplanner.furniture.control.FurnitureService;
import de.hsos.roomplanner.furniture.control.dto.FurnitureDto;
import de.hsos.roomplanner.furniture.control.dto.FurnitureDtoCreateUpdate;
import de.hsos.roomplanner.plan.boundary.rest.PlanResource;
import de.hsos.roomplanner.plan.control.PlanService;
import de.hsos.roomplanner.plan.control.dto.FurnitureInPlanDtoCreateUpdate;
import de.hsos.roomplanner.plan.control.dto.PlanDtoCreateUpdate;
import de.hsos.roomplanner.plan.control.dto.PlanDtoDetail;
import de.hsos.roomplanner.plan.control.dto.PlanDtoListing;
import de.hsos.roomplanner.user.control.UserService;
import de.hsos.roomplanner.util.Page;
import de.hsos.roomplanner.util.Position;
import de.hsos.roomplanner.util.color.Color;
import de.hsos.roomplanner.util.exception.EntityAlreadyExistsException;
import de.hsos.roomplanner.util.exception.EntityDoesNotExistException;
import de.hsos.roomplanner.util.exception.UserNotFoundException;
import de.hsos.roomplanner.util.Dimension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URL;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Benno Steinkamp
 */
@QuarkusTest
@TestHTTPEndpoint(PlanResource.class)
public class PlanResourceTest {

    public static final String TEST_USER_NAME = "TEST_USER";

    @TestHTTPResource("/user/login")
    URL loginUrl;

    @Inject
    UserService userService;

    @Inject
    PlanService planService;

    @Inject
    FurnitureService furnitureService;

    @Inject
    ObjectMapper objectMapper;

    public void setupTestUser() {
        try {
            userService.createUser(TEST_USER_NAME, TEST_USER_NAME);
        } catch (EntityAlreadyExistsException ex) {
        }
    }

    public void clearTestUserPlans() {
        try {
            Page<PlanDtoListing> plans = planService.findPlans(TEST_USER_NAME, "", null, null, Integer.MAX_VALUE, 0);
            for (PlanDtoListing plan : plans.getData()) {
                planService.deletePlan(TEST_USER_NAME, plan.getId());
            }
        } catch (Exception e) {
            fail(e);
        }
    }

    public void clearTestUserFurniture() {
        try {
            Page<FurnitureDto> furniture = furnitureService.findFurnitures(TEST_USER_NAME, "", Integer.MAX_VALUE, 0);
            for (FurnitureDto furnitureDto : furniture.getData()) {
                furnitureService.deleteFurniture(TEST_USER_NAME, furnitureDto.getId());
            }
        } catch (Exception e) {
            fail(e);
        }
    }

    public PlanDtoDetail createTestPlan(String name, float width, float height) {

        PlanDtoCreateUpdate testPlan = new PlanDtoCreateUpdate();

        testPlan.setDimension(new Dimension(width, height));
        testPlan.setName(name);

        try {
            return planService.createPlan(TEST_USER_NAME, testPlan);
        } catch (UserNotFoundException e) {
            fail(e);
            return null;
        }
    }

    public FurnitureDto createTestFurniture(String name, float width, float height, String color) {

        FurnitureDtoCreateUpdate testFurniture = new FurnitureDtoCreateUpdate();

        testFurniture.setDimensions(new Dimension(width, height));
        testFurniture.setName(name);
        testFurniture.setColor(new Color(color));

        try {
            return furnitureService.createFurniture(TEST_USER_NAME, testFurniture);
        } catch (UserNotFoundException e) {
            fail(e);
            return null;
        }
    }

    @Test
    public void testGetPlanNoAuth() {
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
    public void testGetPlansNoAuth() {
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
    public void testPostPlanNoAuth() {
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
    public void testPutPlanNoAuth() {
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
    public void testDeletePlanNoAuth() {
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
    public void testPostFurnitureInPlanNoAuth() {
        given().contentType(ContentType.JSON)
                .redirects()
                .follow(false)
                .when()
                .post("/123/furniture")
                .then()
                .statusCode(302)
                .and()
                .header("Location", loginUrl.toString());
    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testGetPlansEmpty() {
        this.setupTestUser();
        this.clearTestUserPlans();

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
    public void testGetPlans() {

        this.setupTestUser();
        this.clearTestUserPlans();

        String testName = "TestName";
        float testWidth = 1;
        float testHeight = 2;

        this.createTestPlan(testName, testWidth, testHeight);

        given().when()
                .get()
                .then()
                // .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .and()
                .body("totalCount", is(1))
                .body("data", hasSize(1))
                .body("data.name", hasItem(equalTo(testName)))
                .body("data.dimensions.width", hasItem(equalTo(testWidth)))
                .body("data.dimensions.height", hasItem(equalTo(testHeight)));

    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testGetPlansPaging() {
        this.setupTestUser();
        this.clearTestUserPlans();

        String testName = "testName";
        float testWidth = 3;
        float testHeight = 4;
        int planCount = 10;
        int[] pageSizes = { 2, 5, 10 };

        for (int i = 0; i < planCount; i++) {
            this.createTestPlan(testName + "_" + i, testWidth, testHeight);
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
                    .body("totalCount", is(planCount))
                    .body("data", hasSize(pageSize));
        }
    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testGetPlan() {
        this.setupTestUser();
        this.clearTestUserPlans();

        String testName = "testName";
        float testWidth = 5;
        float testHeight = 6;

        long id = this.createTestPlan(testName, testWidth, testHeight).getId();

        given().when()
                .get("/" + id)
                .then()
                .contentType(ContentType.JSON)
                .and()
                .body("name", is(testName))
                .body("dimensions.width", is(testWidth))
                .body("dimensions.height", is(testHeight))
                .body("id", is(Long.valueOf(id).intValue()))
                .body("furniture", empty());
    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testGetPlanNotFound() {
        this.setupTestUser();
        this.clearTestUserPlans();

        long id = 123;

        given().when().get("/" + id).then().statusCode(404);
    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testPostPlan() {
        this.setupTestUser();
        this.clearTestUserPlans();

        String testName = "testName";
        float testWidth = 7;
        float testHeight = 8;

        PlanDtoCreateUpdate planDtoCreateUpdate = new PlanDtoCreateUpdate();

        planDtoCreateUpdate.setDimension(new Dimension(testWidth, testHeight));
        planDtoCreateUpdate.setName("testName");

        String bodyString;
        try {
            bodyString = objectMapper.writeValueAsString(planDtoCreateUpdate);
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
                .body("dimensions.height", is(testHeight));
    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testPutPlan() {
        this.setupTestUser();
        this.clearTestUserPlans();

        long id = this.createTestPlan("otherName", 12, 13).getId();

        String testName = "testName";
        float testWidth = 9;
        float testHeight = 10;

        PlanDtoCreateUpdate planDtoCreateUpdate = new PlanDtoCreateUpdate();

        planDtoCreateUpdate.setDimension(new Dimension(testWidth, testHeight));
        planDtoCreateUpdate.setName(testName);

        String bodyString;
        try {
            bodyString = objectMapper.writeValueAsString(planDtoCreateUpdate);
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
                .body("dimensions.height", is(testHeight));
    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testPutPlanNotFound() {
        this.setupTestUser();
        this.clearTestUserPlans();

        String testName = "testName";
        float testWidth = 9;
        float testHeight = 10;

        PlanDtoCreateUpdate planDtoCreateUpdate = new PlanDtoCreateUpdate();

        planDtoCreateUpdate.setDimension(new Dimension(testWidth, testHeight));
        planDtoCreateUpdate.setName(testName);

        long id = 123;

        String bodyString;
        try {
            bodyString = objectMapper.writeValueAsString(planDtoCreateUpdate);
        } catch (JsonProcessingException e) {
            fail(e);
            return;
        }

        given().contentType(ContentType.JSON).body(bodyString).when().put("/" + id).then().statusCode(404);
    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testDeletePlan() {
        this.setupTestUser();
        long id = this.createTestPlan("testName", 10, 10).getId();

        given().contentType(ContentType.JSON).when().delete("/" + id).then().statusCode(200);

        try {
            this.planService.findPlan(TEST_USER_NAME, id);
            fail();
        } catch (EntityDoesNotExistException ex) {

        } catch (UserNotFoundException ex) {
            fail(ex);
        }
    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testPostFurnitureInPlan() {
        this.setupTestUser();
        this.clearTestUserPlans();
        this.clearTestUserFurniture();

        long planId = this.createTestPlan("testPlan", 10, 10).getId();
        long furnitureId = this.createTestFurniture("testFurniture", 10, 10, "#ffffff").getId();

        FurnitureInPlanDtoCreateUpdate furnitureInPlanDtoCreateUpdate = new FurnitureInPlanDtoCreateUpdate();
        furnitureInPlanDtoCreateUpdate.setFurnitureId(furnitureId);
        furnitureInPlanDtoCreateUpdate.setPosition(new Position());

        String bodyString;
        try {
            bodyString = objectMapper.writeValueAsString(furnitureInPlanDtoCreateUpdate);
        } catch (JsonProcessingException e) {
            fail(e);
            return;
        }

        given().contentType(ContentType.JSON)
                .body(bodyString)
                .when()
                .post("/" + planId + "/furniture")
                .then()
                .statusCode(201);
    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testPutFurnitureInPlan() {
        this.setupTestUser();
        this.clearTestUserPlans();
        this.clearTestUserFurniture();

        long planId = this.createTestPlan("testPlan", 10, 10).getId();
        long furnitureIdOld = this.createTestFurniture("testFurnitureOld", 10, 10, "#ffffff").getId();
        long furnitureIdNew = this.createTestFurniture("testFurnitureNew", 10, 10, "#ffffff").getId();

        FurnitureInPlanDtoCreateUpdate furnitureInPlanDtoCreateUpdateOld = new FurnitureInPlanDtoCreateUpdate();
        furnitureInPlanDtoCreateUpdateOld.setFurnitureId(furnitureIdOld);
        furnitureInPlanDtoCreateUpdateOld.setPosition(new Position());
        long furnitureInPlanId;
        try {
            furnitureInPlanId = this.planService
                    .createFurnitureInPlan(TEST_USER_NAME, planId, furnitureInPlanDtoCreateUpdateOld)
                    .getId();

        } catch (Exception e) {
            fail(e);
            return;
        }

        FurnitureInPlanDtoCreateUpdate furnitureInPlanDtoCreateUpdateNew = new FurnitureInPlanDtoCreateUpdate();
        furnitureInPlanDtoCreateUpdateNew.setFurnitureId(furnitureIdNew);
        float x = 1f;
        float y = 2f;
        float z = 3f;
        float r = 4f;
        furnitureInPlanDtoCreateUpdateNew.setPosition(new Position(x, y, z, r));

        String bodyString;
        try {
            bodyString = objectMapper.writeValueAsString(furnitureInPlanDtoCreateUpdateNew);
        } catch (JsonProcessingException e) {
            fail(e);
            return;
        }

        given().contentType(ContentType.JSON)
                .body(bodyString)
                .when()
                .put("/" + planId + "/furniture/" + furnitureInPlanId)
                .then()
                .statusCode(200)
                .and()
                .body("furnitureId", is(Long.valueOf(furnitureIdNew).intValue()))
                .body("position.x", is(x))
                .body("position.y", is(y))
                .body("position.z", is(z))
                .body("position.rotation", is(r));
    }

    @Test
    @TestSecurity(user = TEST_USER_NAME, roles = "user")
    public void testDeleteFurnitureInPlan() {

        this.setupTestUser();
        this.clearTestUserPlans();
        this.clearTestUserFurniture();

        long planId = this.createTestPlan("testPlan", 10, 10).getId();

        long furnitureId = this.createTestFurniture("testFurniture", 10, 10, "#ffffff").getId();

        FurnitureInPlanDtoCreateUpdate furnitureInPlanDtoCreateUpdate = new FurnitureInPlanDtoCreateUpdate();
        furnitureInPlanDtoCreateUpdate.setFurnitureId(furnitureId);
        furnitureInPlanDtoCreateUpdate.setPosition(new Position());
        long furnitureInPlanId;
        try {
            furnitureInPlanId = this.planService
                    .createFurnitureInPlan(TEST_USER_NAME, planId, furnitureInPlanDtoCreateUpdate)
                    .getId();

        } catch (Exception e) {
            fail(e);
            return;
        }

        given().when().delete("/" + planId + "/furniture/" + furnitureInPlanId).then().statusCode(200);
    }

}