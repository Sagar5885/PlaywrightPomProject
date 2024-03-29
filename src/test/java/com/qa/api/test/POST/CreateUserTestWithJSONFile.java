package com.qa.api.test.POST;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

public class CreateUserTestWithJSONFile {
    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        apiRequest = playwright.request();
        requestContext = apiRequest.newContext();
    }

    @AfterTest
    public void tearDown(){ playwright.close(); }

    public static String getRandomEmail(){
        Random random = new Random();
        String email = "sagTestAuto@"+random.nextInt(10000 - 10 + 1)+"gmail.com";
        return email;
    }

    @Test
    public void createUserTest() throws IOException {
        //Get Json File:
        File file = new File("./src/test/data/user.json");
        byte[] data = Files.readAllBytes(file.toPath());

        APIResponse apiResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer c01f6c6bbaf2db114fe8c8ad44368b45e38e8669287852baee8021cff36f3ae4")
                        .setData(data));

        Assert.assertEquals(apiResponse.status(), 201);
        Assert.assertEquals(apiResponse.url(), "https://gorest.co.in/public/v2/users");
        Assert.assertEquals(apiResponse.statusText(), "Created");

        ObjectMapper om = new ObjectMapper();
        JsonNode postJsonRes = om.readTree(apiResponse.body());
        System.out.println(postJsonRes.toPrettyString());
        String userid = postJsonRes.get("id").asText();

        //GET call after created with POST
        APIResponse apiResponseGet = requestContext.get("https://gorest.co.in/public/v2/users/"+userid,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer c01f6c6bbaf2db114fe8c8ad44368b45e38e8669287852baee8021cff36f3ae4"));
        Assert.assertEquals(apiResponseGet.status(), 200);
        Assert.assertTrue(apiResponseGet.ok());
    }
}
