package com.qa.api.test.GET;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class ApiDisposeTest {
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
    public void disposeResponseTest() {
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/posts");

        //dispose response
        apiResponse.dispose();//only dispose body not status and url
        try{
            System.out.println(apiResponse.text());
        }catch (PlaywrightException e){
            System.out.println("Exception: "+e);
        }
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertTrue(apiResponse.ok());
        System.out.println("Response status text: "+apiResponse.statusText());


        //2nd req:
        APIResponse apiResponse1 = requestContext.get("https://reqres.in/api/users?page=2");
        System.out.println(apiResponse1.status());
        System.out.println(apiResponse1.text());

        //dispose request
        requestContext.dispose();
        try {
            System.out.println(apiResponse1.text());
        }catch (PlaywrightException e){
            System.out.println("Exception: "+e);
        }
    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
