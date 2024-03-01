package com.qa.api.test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import com.qa.api.data.User;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateUSerTestWithPOJO {
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

        //Create User with POJO
        User user = new User("Tenali Ramakrishna", getRandomEmail(), "male", "active");

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
        Assert.assertEquals(userRes.getName(), "Tenali Ramakrishna");
        Assert.assertEquals(userRes.getGender(), "male");

        //GET call after created with POST
        APIResponse apiResponseGet = requestContext.get("https://gorest.co.in/public/v2/users/"+userid,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer c01f6c6bbaf2db114fe8c8ad44368b45e38e8669287852baee8021cff36f3ae4"));
        Assert.assertEquals(apiResponseGet.status(), 200);
        Assert.assertTrue(apiResponseGet.ok());
    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
