import com.igor.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class GmailTest {
    private static final Logger LOGGER = LogManager.getLogger(GmailTest.class);
    private static final String INITIAL_URL = "https://mail.google.com/";
    private static final String EMAIL_NAME = "groot.epam@gmail.com";
    private static final String EMAIL_PASSWORD = "iamgroot";
    private static final String RECEIVER_EMAIL = "paprika0015@gmail.com";
    private static final String SENT_MESSAGE = "I am Groot!" + Keys.ENTER + "Mr. Stark will die.";
    private WebDriver driver = DriverManager.getDriver();


    @BeforeClass
    public void setImplicitWaitToDriver() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }


    @AfterClass
    public void quitDriver() {
        driver.quit();
    }

    @Test
    public void sendEmailTest() throws InterruptedException {
        driver.get(INITIAL_URL);
        logIn();
        sendLetter();
        checkSentLetter();
    }

    private void logIn() throws InterruptedException {
        writeUsername();
        writePassword();
    }

    private void writeUsername() {
        WebElement usernameInput = driver.findElement(By.id("identifierId"));
        usernameInput.sendKeys(EMAIL_NAME);
        usernameInput.sendKeys(Keys.ENTER);
        WebElement profileIdentifier = driver.findElement(By.id("profileIdentifier"));
        assertEquals(profileIdentifier.getAttribute("innerHTML"), EMAIL_NAME);
        LOGGER.info("Input username OK");
    }

    private void writePassword() throws InterruptedException {
        WebElement passwordInput = driver.findElement(By.xpath("//input[@type='password' and @name='password']"));
        passwordInput.sendKeys(EMAIL_PASSWORD);
        passwordInput.sendKeys(Keys.ENTER);
        TimeUnit.SECONDS.sleep(10);
        assertTrue(driver.getTitle().contains(EMAIL_NAME));
        LOGGER.info("Input password OK");
    }

    private void sendLetter() {
        openWriteWindow();
        writeReceiver();
        writeTopic();
        writeMessage();
        WebElement sendButton = driver.findElement(By.id(":8m"));
        sendButton.click();
    }

    private void writeMessage() {
        WebElement massegeInput = driver.findElement(By.id(":a1"));
        massegeInput.sendKeys(SENT_MESSAGE);
    }

    private void writeTopic() {
        WebElement topicInput = driver.findElement(By.id(":8w"));
        topicInput.sendKeys("Final battle");
    }

    private void writeReceiver() {
        WebElement receiverInput = driver.findElement(By.id(":9e"));
        receiverInput.sendKeys(RECEIVER_EMAIL);
    }

    private void openWriteWindow() {
        WebElement writeBlock = driver.findElement(By.cssSelector("#\\:3q>div>div"));
        writeBlock.click();
    }

    private void checkSentLetter() {
        WebElement sentBlock = driver.findElement(By.xpath("//*[@id=':3k']/div/div[2]/span/a"));
        sentBlock.click();
        WebElement sentMessage = driver.findElement(By.xpath("//*[@id=':1od']/div/div/span/text()"));
        assertEquals(sentMessage.getText(), SENT_MESSAGE);
    }
}
