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
    private static final String SENT_MESSAGE = "I am Groot!";
    private static final String SENT_MESSAGE_TITLE = "Final battle";

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
        TimeUnit.SECONDS.sleep(5);
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
        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.sendKeys(EMAIL_PASSWORD);
        passwordInput.sendKeys(Keys.ENTER);
        TimeUnit.SECONDS.sleep(10);
        assertTrue(driver.getTitle().contains(EMAIL_NAME));
        LOGGER.info("Input password OK");
    }

    private void sendLetter() {
        openWriteWindow();
        writeMessage();
        WebElement sendButton = driver.findElement(By.cssSelector("table[role=group]>tbody>tr>td:first-child>div>div>div:first-child"));
        sendButton.click();
    }

    private void writeMessage() {
        WebElement shortInfoBlock = driver.findElement(By.xpath("//input[@name='composeid']/.."));
        WebElement receiverInput = shortInfoBlock.findElement(By.xpath("./div[1]//textarea[@name='to']"));
        WebElement topicInput = shortInfoBlock.findElement(By.xpath("./div[3]//input[@name='subjectbox']"));
        receiverInput.sendKeys(RECEIVER_EMAIL);
        topicInput.sendKeys(SENT_MESSAGE_TITLE);
        WebElement messageInput = shortInfoBlock.findElement(By.xpath("../table//div[@role='textbox']"));
        messageInput.sendKeys(SENT_MESSAGE);
    }
    private void openWriteWindow() {
        WebElement menu = driver.findElement(By.xpath("//div[@role='complementary']/.."));
        WebElement writeBlock = menu.findElement(By.xpath(".//div[@role='button']"));
        writeBlock.click();
    }

    private void checkSentLetter() throws InterruptedException {
        WebElement searchInput = driver.findElement(By.xpath("//form[@role='search']//input"));
        searchInput.sendKeys("in:sent");
        WebElement searchButton = searchInput.findElement(By.xpath("//form[@role='search']/button[4]"));
        searchButton.click();
        TimeUnit.SECONDS.sleep(10);
        WebElement sentMessage = driver.findElement(By.xpath("//div[@class='AO']//div[@role='main']//tbody/tr[1]"));
        assertTrue(sentMessage.getText().contains(SENT_MESSAGE_TITLE));
        LOGGER.info("Letter sent OK");
    }
}
