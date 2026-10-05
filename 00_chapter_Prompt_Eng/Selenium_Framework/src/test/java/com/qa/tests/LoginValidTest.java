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

public class LoginValidTest {
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
            throw new RuntimeException("Valid login setup failed.", e);
        }
    }

    @Test
    public void validLoginTest() {
        try {
            String username = System.getProperty("salesforce.username", System.getenv("SALESFORCE_USERNAME"));
            String password = System.getProperty("salesforce.password", System.getenv("SALESFORCE_PASSWORD"));
            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                throw new IllegalStateException("Configure salesforce.username and salesforce.password system properties or SALESFORCE_USERNAME and SALESFORCE_PASSWORD environment variables.");
            }
            loginPage.login(username, password);
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Valid credentials did not advance beyond the Salesforce login page.");
            Assert.assertFalse(loginPage.isErrorDisplayed(), "The system displayed an error for valid credentials.");
        } catch (Exception e) {
            Assert.fail("Valid login scenario failed.", e);
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
