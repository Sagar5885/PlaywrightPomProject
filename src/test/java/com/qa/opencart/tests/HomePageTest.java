package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.ApplicationConstants;
import org.testng.Assert;
import org.testng.annotations.*;

public class HomePageTest extends BaseTest {

    @Test
    public void homePageTitleTest(){
        String actualTitle = homePage.getHomePageTitle();
        Assert.assertEquals(actualTitle, ApplicationConstants.HOME_PAGE_TITLE);
    }

    @Test(dependsOnMethods = "homePageTitleTest")
    public void homePageUrlTest(){
        String actualURL = homePage.getHomePageURL();
        Assert.assertEquals(actualURL, prop.getProperty("url"));
    }

    @DataProvider
    public Object[][] getProductData() {
        return new Object[][] {
                { "Macbook" },
                { "iMac" },
                { "Samsung" }
        };
    }

    @Test(dataProvider = "getProductData", dependsOnMethods = "homePageUrlTest")
    public void homePageSearchTest(String productName){
        String headerOfSearchPage = homePage.doSearch(productName);
        Assert.assertEquals(headerOfSearchPage, "Search - "+productName);
    }
}
