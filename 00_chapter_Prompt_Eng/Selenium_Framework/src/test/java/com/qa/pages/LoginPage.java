package com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//input[@id='username']")
    private WebElement usernameInput;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@id='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//input[@id='rememberUn']")
    private WebElement rememberMeCheckbox;

    @FindBy(xpath = "//div[contains(.,'Please check your username and password')]")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public void open(String url) {
        try {
            driver.get(url);
            wait.until(ExpectedConditions.visibilityOf(usernameInput));
        } catch (Exception e) {
            throw new RuntimeException("Unable to load Salesforce login page: " + url, e);
        }
    }

    public void enterUsername(String username) {
        try {
            wait.until(ExpectedConditions.visibilityOf(usernameInput));
            usernameInput.clear();
            usernameInput.sendKeys(username);
        } catch (Exception e) {
            throw new RuntimeException("Unable to enter username on Salesforce login page.", e);
        }
    }

    public void enterPassword(String password) {
        try {
            wait.until(ExpectedConditions.visibilityOf(passwordInput));
            passwordInput.clear();
            passwordInput.sendKeys(password);
        } catch (Exception e) {
            throw new RuntimeException("Unable to enter password on Salesforce login page.", e);
        }
    }

    public void clickRememberMe() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(rememberMeCheckbox));
            if (!rememberMeCheckbox.isSelected()) {
                rememberMeCheckbox.click();
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to interact with the Remember Me checkbox.", e);
        }
    }

    public void clickLogin() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            loginButton.click();
        } catch (Exception e) {
            throw new RuntimeException("Unable to click the login button.", e);
        }
    }

    public void login(String username, String password) {
        enterUsername(username);
        if (driver.findElements(By.xpath("//input[@id='password']")).stream().noneMatch(WebElement::isDisplayed)) {
            clickLogin();
        }
        enterPassword(password);
        clickLogin();
    }

    public boolean isLoginPageLoaded() {
        boolean loginInputVisible = driver.findElements(By.xpath("//input[@id='username'] | //input[@id='password']"))
                .stream()
                .anyMatch(WebElement::isDisplayed);
        return driver.getCurrentUrl().contains("login.salesforce.com") && loginButton.isDisplayed() && loginInputVisible;
    }

    public boolean isErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(errorMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("login.salesforce.com")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
