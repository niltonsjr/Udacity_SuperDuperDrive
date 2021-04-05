package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(id = "success-message")
    private WebElement successMessage;

    @FindBy(id = "success-span-msg")
    private WebElement successSpanMessage;

    @FindBy(id = "success-redirect")
    private WebElement successRedirectLink;

    @FindBy(id = "fail-message")
    private WebElement failMessage;

    @FindBy(id = "fail-span-msg")
    private WebElement failSpanMessage;

    @FindBy(id = "fail-redirect")
    private WebElement failRedirectLink;

    WebDriver driver;

    public ResultPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getSuccessMessage() {
        return successSpanMessage.getText();
    }

    public String getErrorMessage() {
        return failSpanMessage.getText();
    }

    public void successGoBackToHome() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", successRedirectLink);
    }

    public void errorGoBackToHome() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", failRedirectLink);
    }
}
