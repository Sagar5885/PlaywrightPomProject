package com.qa.api.test;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ApiResponseHeadersTest {
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
    public void getHeadersTest() {
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");
        Assert.assertEquals(apiResponse.status(), 200);
        Assert.assertTrue(apiResponse.ok());
        Assert.assertEquals(apiResponse.url(), "https://gorest.co.in/public/v2/users");

        //Headers with Map
        Map<String, String> mpHeaders = apiResponse.headers();
        mpHeaders.forEach((k,v) -> System.out.println(k+": "+v));
        System.out.println("Total headers: "+mpHeaders.size());
        Assert.assertEquals(mpHeaders.get("server"), "cloudflare");
        Assert.assertEquals(mpHeaders.get("vary"), "Origin");
        System.out.println();

        //Headers with List
        List<HttpHeader> listHeaders = apiResponse.headersArray();
        for (HttpHeader hh: listHeaders) {
            System.out.println(hh.name+": "+hh.value);
        }
    }


    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
