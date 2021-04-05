package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //Used to reset database before each Test
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;

	@Autowired
	private CredentialService credentialService;

	@Autowired
	private EncryptionService encryptionService;

	private LoginPage loginPage;
	private SignupPage signupPage;
	private ResultPage resultPage;
	private HomePage homePage;

	private String firstname = "Mario";
	private String lastname = "Silva";
	private String username = "marsil";
	private String password = "123";

	private String noteTitle = "New note";
	private String noteDescription = "New note description";

	private String credentialUrl = "https://udacity.com/auth";

	private final String URL = "http://localhost:";


	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@AfterAll
	public static void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	/**
	 * Method to perform the sing up and login for some tests
	 */
	public void performSingUpAndLogin() {
		driver.get(URL + this.port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.signup(firstname,lastname, username, password);
		loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

	/**
	 * Login and Signup Tests
	 */

	@Test
	public void getLoginPage() {
		driver.get(URL + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void tryToAccessHomeWithoutLoginShouldReturnToLoginPage() {
		driver.get(URL + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get(URL + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void goFromLoginToSignupPageWithSignupLink() {
		driver.get(URL + this.port + "/login");
		loginPage = new LoginPage(driver);
		loginPage.goToSignupPage();
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void signupANewUserShouldReturnToLoginAndShowSuccessMsg() {
		driver.get(URL + this.port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.signup(firstname,lastname, username, password);
		loginPage = new LoginPage(driver);
		Assertions.assertEquals("Login", driver.getTitle());
		Assertions.assertTrue(loginPage.isSignedUpDisplayed());
		Assertions.assertEquals("You successfully signed up.", loginPage.getSuccessMessage());
	}

	@Test
	public void signUpTheSameUserShouldShowAErrorMsg() {
		driver.get(URL + this.port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.signup(firstname,lastname, username, password);
		loginPage = new LoginPage(driver);
		loginPage.goToSignupPage();
		signupPage.signup(firstname,lastname, username, password);
		Assertions.assertTrue(signupPage.isErrorMessageDisplayed());
		Assertions.assertEquals("The username already exists.", signupPage.getErrorMessage());
	}

	@Test
	public void goFromSignupPageToLoginPageWithBackToLoginLink() {
		driver.get(URL + this.port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.backToLogin();
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void loginWithNoSignedUpUserShouldShowErrorMsg() {
		driver.get(URL + this.port + "/login");
		loginPage = new LoginPage(driver);
		loginPage.login(username, password);
		Assertions.assertEquals("Invalid username or password.", loginPage.getErrorMessage());
	}

	@Test
	public void LoginWithAValidUserShouldGoToHomePage() {
		performSingUpAndLogin();
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	public void logoutShouldReturnToLoginPageAndShowLogoutMessage() {
		performSingUpAndLogin();
		homePage = new HomePage(driver);
		homePage.logout();
		Assertions.assertEquals("Login", driver.getTitle());
		Assertions.assertTrue(loginPage.isLoggedOutDisplayed());
		Assertions.assertEquals("You have been logged out.", loginPage.getLogoutMessage());
		//Check if the home page is no longer accessible
		homePage = new HomePage(driver);
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			homePage.goToFilesTab();
		} );
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * Notes test
	 */

	public void performAddNewNote() {
		homePage = new HomePage(driver);
		homePage.goToNoteTab();
		homePage.clickAddNoteButton();
		homePage.fillNoteModal(noteTitle, noteDescription);
	}

	@Test
	public void testAddingANewNote() {
		performSingUpAndLogin();
		performAddNewNote();
		resultPage = new ResultPage(driver);
		Assertions.assertEquals("Note added successfully.", resultPage.getSuccessMessage());
		resultPage.successGoBackToHome();
		// Test if after click in success link, go to home page.
		Assertions.assertEquals("Home", driver.getTitle());
		homePage.goToNoteTab();
		//Test if the note Title and Note Description match.
		Assertions.assertEquals(noteTitle, homePage.getLastNoteTitle());
		Assertions.assertEquals(noteDescription, homePage.getLastNoteDescription());
	}

	@Test
	public void testEditingANote() {
		performSingUpAndLogin();
		performAddNewNote();
		resultPage = new ResultPage(driver);
		resultPage.successGoBackToHome();
		homePage.goToNoteTab();
		homePage.clickEditNoteButton();
		homePage.fillNoteModal("Edited title", "Edited description");
		resultPage = new ResultPage(driver);
		Assertions.assertEquals("Note updated successfully.", resultPage.getSuccessMessage());
		resultPage.successGoBackToHome();
		homePage.goToNoteTab();
		//Test if the note Title and Note Description match.
		Assertions.assertEquals("Edited title", homePage.getLastNoteTitle());
		Assertions.assertEquals("Edited description", homePage.getLastNoteDescription());
	}

	@Test
	public void testDeletingANote() {
		performSingUpAndLogin();
		performAddNewNote();
		resultPage = new ResultPage(driver);
		resultPage.successGoBackToHome();
		homePage.goToNoteTab();
		// Check if the note has been added
		Assertions.assertEquals(noteTitle, homePage.getLastNoteTitle());
		homePage.clickDeleteNoteButton();
		resultPage = new ResultPage(driver);
		// Check if the result page shows the correct message
		Assertions.assertEquals("The note was deleted successfully.", resultPage.getSuccessMessage());
		resultPage.successGoBackToHome();
		homePage.goToNoteTab();
		// Check if the note doesn't exists anymore (https://knowledge.udacity.com/questions/434432)
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			homePage.getLastNoteTitle();
		} );
	}

	/**
	 * Credentials Tests
	 */

	@Test
	public void performAddNewCredential() {
		performSingUpAndLogin();
		homePage = new HomePage(driver);
		homePage.goToCredentialsTab();
		homePage.clickAddCredentialButton();
		homePage.fillCredentialModal(credentialUrl, username, password);
	}

	@Test
	public void testAddingANewCredential() {
		performAddNewCredential();
		resultPage = new ResultPage(driver);
		Assertions.assertEquals("Credential added successfully.", resultPage.getSuccessMessage());
		resultPage.successGoBackToHome();
		// Test if after click in success link, go to home page.
		Assertions.assertEquals("Home", driver.getTitle());
		homePage.goToCredentialsTab();

		// Get the credential created, there must be only one credential added so the id must be 1
		Credential credential = credentialService.getCredentialById(1);

		//Test if the credential URL, Username and Password match. (password has to be encrypted)
		Assertions.assertEquals(credentialUrl, homePage.getLastCredentialURL());
		Assertions.assertEquals(username, homePage.getLastCredentialUsername());
		String encryptedPassword = encryptionService.encryptValue(password, credential.getKey());
		// check if the password is shown encrypted
		Assertions.assertEquals(encryptedPassword, homePage.getLastCredentialPassword());
	}

	@Test
	public void whenEditACredentialThePasswordShouldBeDecrypted() {
		performAddNewCredential();
		resultPage = new ResultPage(driver);
		resultPage.successGoBackToHome();
		homePage.goToCredentialsTab();
		homePage.clickEditCredentialButton();
		Assertions.assertEquals(password, homePage.getModalPasswordInput());
	}

	@Test
	public void testEditCredential() {
		String editedURL = "Edited URL";
		String editedUser = "Edited user";
		String editedPassword = "Edited password";
		performAddNewCredential();
		resultPage = new ResultPage(driver);
		resultPage.successGoBackToHome();
		homePage.goToCredentialsTab();
		homePage.clickEditCredentialButton();
		homePage.fillCredentialModal(editedURL, editedUser, editedPassword);
		resultPage = new ResultPage(driver);
		// Check if the success message is correct
		Assertions.assertEquals("Credential updated successfully.", resultPage.getSuccessMessage());
		resultPage.successGoBackToHome();
		homePage.goToCredentialsTab();
		// Check if the credential data have changed
		Assertions.assertEquals(editedURL, homePage.getLastCredentialURL());
		Assertions.assertEquals(editedUser, homePage.getLastCredentialUsername());
		// To check the password, click in Edit Credential to get the unencrypted password
		homePage.clickEditCredentialButton();
		Assertions.assertEquals(editedPassword, homePage.getModalPasswordInput());
	}

	@Test
	public void testDeleteCredential() {
		performAddNewCredential();
		resultPage = new ResultPage(driver);
		resultPage.successGoBackToHome();
		homePage.goToCredentialsTab();
		// Check if the credential has been added
		Assertions.assertEquals(credentialUrl, homePage.getLastCredentialURL());
		// Click on delete credential
		homePage.clickDeleteCredentialButton();
		// Check if the success message is correct
		resultPage = new ResultPage(driver);
		Assertions.assertEquals("The credential was deleted successfully.", resultPage.getSuccessMessage());
		resultPage.successGoBackToHome();
		homePage.goToCredentialsTab();
		// Check if the credential has been deleted
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			homePage.getLastCredentialURL();
		} );
	}

}
