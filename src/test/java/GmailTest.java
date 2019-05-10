import com.igor.driver.DriverManager;
import com.igor.model.Spoiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
    private static final String SENT_MESSAGE = "I am Groot! " + Spoiler.getSpoiler();
    private static final String SENT_MESSAGE_TITLE = "Final battle";

    private WebDriver driver = DriverManager.getDriver();
    private WebDriverWait driverWait = DriverManager.getDriverWait();

    @BeforeClass
    public void setImplicitWaitToDriver() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public void quitDriver() {
        driver.quit();
    }

    @Test
    public void sendEmailTest() {
        driver.get(INITIAL_URL);
        logIn();
        sendLetter();
        DriverManager.waitWhilePageLoad(5);
        checkSentLetter();
    }

    private void logIn() {
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

    private void writePassword() {
        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.sendKeys(EMAIL_PASSWORD);
        passwordInput.sendKeys(Keys.ENTER);
        DriverManager.waitWhilePageLoad(10);
//        assertTrue(driver.getTitle().contains(EMAIL_NAME));
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

    private void checkSentLetter(){
        WebElement searchInput = driver.findElement(By.xpath("//form[@role='search']//input"));
        searchInput.sendKeys("in:sent");
        WebElement searchButton = searchInput.findElement(By.xpath("//form[@role='search']/button[4]"));
        searchButton.click();
        driverWait.until(ExpectedConditions.urlContains("sent"));
        WebElement sentMessage = driver.findElement(By.xpath("//div[@class='AO']//div[@role='main']//tbody"));
        WebElement sentMessageText = sentMessage.findElement(By.xpath("./tr[1]"));
        WebElement sentMessageDate = sentMessageText.findElement(By.xpath("./td[last()-1]/span"));
        LOGGER.info("Time, when letter was sent: " + sentMessageDate.getText());
        assertTrue(sentMessageText.getText().contains(SENT_MESSAGE_TITLE) && sentMessage.getText().contains(SENT_MESSAGE));
        LOGGER.info("Letter sent OK");
    }
}
