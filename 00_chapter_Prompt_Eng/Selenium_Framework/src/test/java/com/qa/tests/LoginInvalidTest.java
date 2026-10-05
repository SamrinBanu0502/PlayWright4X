package com.qa.tests;

import com.qa.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginInvalidTest {
    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeTest
    public void setUp() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            driver = new ChromeDriver(options);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            loginPage = new LoginPage(driver);
            loginPage.open("https://login.salesforce.com/?locale=in");
        } catch (Exception e) {
            throw new RuntimeException("Invalid login setup failed.", e);
        }
    }

    @Test
    public void invalidLoginTest() {
        try {
            loginPage.login("invalid.user@example.com", "WrongPassword@123");
            Assert.assertTrue(loginPage.isLoginPageLoaded(), "The Salesforce login page did not load correctly.");
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Invalid credentials validation message was not displayed.");
        } catch (Exception e) {
            Assert.fail("Invalid login scenario failed.", e);
        }
    }

    @AfterTest
    public void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to close the browser session.", e);
        }
    }
}
