package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "nav-files-tab")
    private WebElement navFilesTab;

    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement navCredentialsTab;

    //TAB Notes

    @FindBy(xpath = "//table[@id='notesTable']/tbody/tr[last()]/th[last()]")
    private WebElement lastNoteTitle;

    @FindBy(xpath = "//table[@id='notesTable']/tbody/tr[last()]/td[last()]")
    private WebElement lastNoteDescription;

    @FindBy(id = "add-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "edit-note")
    private WebElement editNoteButton;

    @FindBy(id = "delete-note")
    private WebElement deleteNoteButton;

    @FindBy(id = "note-title")
    private WebElement modalNoteTitleInput;

    @FindBy(id = "note-description")
    private WebElement modalNoteDescriptionInput;

    @FindBy(id = "noteSubmit")
    private WebElement modalNoteSubmitButton;

    //TAB Credentials

    @FindBy(xpath = "//table[@id='credentialTable']/tbody/tr[last()]/th[last()]")
    private WebElement lastCredentialURL;

    @FindBy(xpath = "//table[@id='credentialTable']/tbody/tr[last()]//td[position() = 2]")
    private WebElement lastCredentialUsername;

    @FindBy(xpath = "//table[@id='credentialTable']/tbody/tr[last()]//td[last()]")
    private WebElement lastCredentialPassword;

    @FindBy(id = "add-credential-button")
    private WebElement addCredentialButton;

    @FindBy(id = "edit-credential")
    private WebElement editCredentialButton;

    @FindBy(id = "delete-credential")
    private WebElement deleteCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement modalCredentialURLInput;

    @FindBy(id = "credential-username")
    private WebElement modalCredentialUsernameInput;

    @FindBy(id = "credential-password")
    private WebElement modalCredentialPasswordInput;

    @FindBy(id = "credentialSubmit")
    private WebElement modalCredentialSubmitButton;

    WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutButton);
    }

    // Methods for TAB Files

    public void goToFilesTab() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", navFilesTab);
    }

    // Methods for TAB Notes

    public void goToNoteTab() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", navNotesTab);
    }

    public void clickAddNoteButton() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addNoteButton);
    }

    public void fillNoteModal(String title, String description) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + title + "';", modalNoteTitleInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description+ "';", modalNoteDescriptionInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", modalNoteSubmitButton);
    }

    public void clickEditNoteButton() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editNoteButton);
    }

    public void clickDeleteNoteButton() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteNoteButton);
    }

    //Methods for TAB Credentials

    public void goToCredentialsTab() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", navCredentialsTab);
    }

    public void clickAddCredentialButton() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addCredentialButton);
    }

    public void fillCredentialModal(String url, String username, String password) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + url + "';", modalCredentialURLInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username+ "';", modalCredentialUsernameInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password+ "';", modalCredentialPasswordInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", modalCredentialSubmitButton);
    }

    public void clickEditCredentialButton() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editCredentialButton);
    }

    public void clickDeleteCredentialButton() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteCredentialButton);
    }

    // Get the password in the Modal Window that must be unencrypted.
    public String getModalPasswordInput() {
        return ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", modalCredentialPasswordInput).toString();
    }

    // Get the note title in the note list
    public String getLastNoteTitle() {
        return  lastNoteTitle.getAttribute("innerHTML");
    }

    // Get the note description in the note list
    public String getLastNoteDescription() {
        return  lastNoteDescription.getAttribute("innerHTML");
    }

    // Get the credential url in the credential list
    public String getLastCredentialURL() {
        return  lastCredentialURL.getAttribute("innerHTML");
    }

    // Get the credential username in the credential list
    public String getLastCredentialUsername() {
        return  lastCredentialUsername.getAttribute("innerHTML");
    }

    // Get the credential password in the credential list that must be encrypted
    public String getLastCredentialPassword() {
        return  lastCredentialPassword.getAttribute("innerHTML");
    }
}
