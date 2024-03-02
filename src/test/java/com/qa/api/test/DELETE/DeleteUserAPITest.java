package com.qa.api.test.DELETE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qa.api.data.User;
import com.qa.api.data.Users;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Random;

public class DeleteUserAPITest {
    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        apiRequest = playwright.request();
        requestContext = apiRequest.newContext();
    }

    public static String getRandomEmail(){
        Random random = new Random();
        String email = "sagTestAuto@"+random.nextInt(10000 - 10 + 1)+"gmail.com";
        return email;
    }

    @Test
    public void createUserTest() throws IOException {

        //Create User with POJO with Lombok
        Users user = Users.builder()
                .name("Tenali")
                .email(getRandomEmail())
                .gender("male")
                .status("active")
                .build();

        System.out.println("----------Create - POST------------");
        //POST - Create
        APIResponse apiResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer c01f6c6bbaf2db114fe8c8ad44368b45e38e8669287852baee8021cff36f3ae4")
                        .setData(user));

        Assert.assertEquals(apiResponse.status(), 201);
        Assert.assertEquals(apiResponse.url(), "https://gorest.co.in/public/v2/users");
        Assert.assertEquals(apiResponse.statusText(), "Created");

        ObjectMapper om = new ObjectMapper();
        User userRes = om.readValue(apiResponse.text(), User.class);
        System.out.println(userRes.getEmail());
        String userid = userRes.getId();
        System.out.println(userid);
        Assert.assertEquals(userRes.getName(), "Tenali");
        Assert.assertEquals(userRes.getGender(), "male");

        System.out.println("----------GET------------");
        //GET call after created with POST
        APIResponse apiResponseGet = requestContext.get("https://gorest.co.in/public/v2/users/"+userid,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer c01f6c6bbaf2db114fe8c8ad44368b45e38e8669287852baee8021cff36f3ae4"));
        Assert.assertEquals(apiResponseGet.status(), 200);
        Assert.assertTrue(apiResponseGet.ok());

        ObjectMapper omGet = new ObjectMapper();
        User userResGet = omGet.readValue(apiResponseGet.text(), User.class);
        Assert.assertEquals(userResGet.getName(), "Tenali");
        Assert.assertEquals(userResGet.getGender(), "male");

        System.out.println("----------DELETE------------");
        //DELETE
        APIResponse apiResponseDelete = requestContext.delete("https://gorest.co.in/public/v2/users/"+userid,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer c01f6c6bbaf2db114fe8c8ad44368b45e38e8669287852baee8021cff36f3ae4"));
        Assert.assertEquals(apiResponseDelete.status(), 204);
        Assert.assertEquals(apiResponseDelete.statusText(), "No Content");

        System.out.println("----------GET After Delete------------");
        //GET call after created with POST
        APIResponse apiResponseGetAfDel = requestContext.get("https://gorest.co.in/public/v2/users/"+userid,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer c01f6c6bbaf2db114fe8c8ad44368b45e38e8669287852baee8021cff36f3ae4"));
        Assert.assertEquals(apiResponseGetAfDel.status(), 404);
        Assert.assertEquals(apiResponseGetAfDel.statusText(), "Not Found");
    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
