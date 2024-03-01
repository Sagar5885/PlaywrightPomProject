package com.qa.api.test;

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

import java.io.IOException;
import java.util.Map;

public class GetAPITest {

    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        apiRequest = playwright.request();
        requestContext = apiRequest.newContext();
    }

    @Test
    public void getSpecificUserTest() throws IOException {
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setQueryParam("id", 2139089)
                        .setQueryParam("status", "inactive"));
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertTrue(apiResponse.ok());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiResponse.body());
        System.out.println(jsonNode.toPrettyString());
    }


    @Test
    public void getUserTest() throws IOException {
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/posts");
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertTrue(apiResponse.ok());
        Assert.assertEquals(apiResponse.url(), "https://gorest.co.in/public/v2/posts");

        Map<String, String> headers = apiResponse.headers();
        System.out.println(headers);
        Assert.assertEquals(headers.get("content-type"), "application/json; charset=utf-8");
        Assert.assertEquals(headers.get("x-download-options"), "noopen");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiResponse.body());
        System.out.println(jsonNode.toPrettyString());
    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
