package com.application.signup.api;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;

@SpringBootTest
class ApplicationSignupApiApplicationTests {

	private long userID = 0;

	@BeforeSuite
	void beforeTest() {
		baseURI = "http://localhost:8089/api/signup";
	}
	
	@Test
	void signupCheckIfThereIsNoUserRegistered() throws JSONException {
		given()
    		.auth().preemptive().basic("admin", "admin")		
    		.accept(ContentType.JSON).contentType(ContentType.JSON)
        .when()
    		.get("/users").then().body("user", not(hasSize(greaterThan(0))));
	}

	@Test(priority=1)
	void signupCreateUserSuccessDueFilledUsernameEmailPassword() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "test");
		user.put("email", "test@test.com");
		user.put("password", "P@ssw0rd");		
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(201);
		
		this.userID = Long.valueOf(
				given()
				.auth().preemptive().basic("admin", "admin")
					.accept(ContentType.JSON).contentType(ContentType.JSON)
				.when()
		       		.get("/users")
		       	.then()
		       		.extract().response().path("id").toString().replaceAll("[\\[\\](){}]",""));
	}
	
	@Test(priority=2)
	void signupCreateUserFailedDueUserDuplicated() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "test");
		user.put("email", "test@testy.com");
		user.put("password", "P@ssw0rd");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
        .when()
        	.post("/users")
        .then()
            .statusCode(409);
	}
	
	@Test(priority=3)
	void signupCreateUserFailedDueEmailDuplicated() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "testy");
		user.put("email", "test@test.com");
		user.put("password", "P@ssw0rd");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
        .when()
        	.post("/users")
        .then()
            .statusCode(409);
	}
	
	@Test(priority=4)
	void signupCreateUserFailedDueUsernameAlreadyInUse() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "test");
		user.put("email", "test@test.com");
		user.put("password", "P@ssw0rd");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
        .when()
        	.post("/users")
        .then()
        	.statusCode(409);
	}
	
	@Test(priority=5)
	void signupCreateUserFailedDueEmailAlreadyInUse() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "testa");
		user.put("email", "test@test.com");
		user.put("password", "P@ssw0rd");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
        .when()
        	.post("/users")
        .then()
            .statusCode(409);
	}
	
	
	@Test(priority=6)
	void signupFindAllUsers() {
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON)
			.get("/users").then().body("users", hasSize(greaterThan(0)));
	}
	
	@Test(priority=7)
	void signupFindUserById() {
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON)
		.when()
			.get("/users/" + this.userID)
		.then()
			.body("username", equalTo("test"));
    }
	
	@Test
	void signupCreateUserFailureDueEmptyUsername() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("email", "test@testc.com");
		user.put("password", "P@ssw0rd");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(400);
	}
	
	@Test
	void signupCreateUserFailureDueWrongUsernameCharactersSize() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "te");
		user.put("email", "test@testd.com");
		user.put("password", "P@ssw0rd");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(400);
	}
	
	@Test
	void signupCreateUserFailureDueEmptyEmail() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "testh");
		user.put("password", "P@ssw0rd");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(400);
	}
	
	@Test
	void signupCreateUserFailureDueEmptyPassword() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "testk");
		user.put("email", "test@testl.com");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(400);
	}
	
	@Test
	void signupCreateUserFailureDueWrongPasswordCharactersSize() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "testyy");
		user.put("email", "test@testyy.com");
		user.put("password", "test");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(400);
	}
	
	@Test
	void signupCreateUserFailurePasswordDueMissingUppercaseCharacter() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "testyy");
		user.put("email", "test@testyy.com");
		user.put("password", "p@ssw0rd");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(400);
	}
	
	@Test
	void signupCreateUserFailurePasswordDueMissingLowercaseCharacter() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "testyy");
		user.put("email", "test@testyy.com");
		user.put("password", "P@SSW0RD");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(400);
	}
	
	@Test
	void signupCreateUserFailurePasswordDueMissingSpecialCharacter() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "testyy");
		user.put("email", "test@testyy.com");
		user.put("password", "Passw0rd");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(400);
	}
	
	@Test
	void signupCreateUserFailurePasswordDueMissingNumericCharacter() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "testyy");
		user.put("email", "test@testyy.com");
		user.put("password", "P@ssword");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(400);
	}
	
	@Test
	void signupCreateUserFailureDueNotWellFormedEmail() throws JSONException {
		JSONObject user = new JSONObject();
		user.put("username", "testp");
		user.put("email", "test");
		user.put("password", "P4ss0rdd");
		
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON).body(user.toString())
		.when()
			.post("/users")
		.then()
			.statusCode(400);
	}	
	
	@AfterSuite
	void signupDeleteUserSuccess() throws JSONException {
		given()
			.auth().preemptive().basic("admin", "admin")
			.accept(ContentType.JSON).contentType(ContentType.JSON)
		.when()
			.delete("/users/" + this.userID)
		.then()
			.statusCode(200);
	}
	
}