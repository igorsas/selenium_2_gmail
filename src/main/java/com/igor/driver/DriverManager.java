package com.igor.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DriverManager {
    private static final WebDriver driver;
    private static WebDriverWait driverWait;

    static {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        driver = new ChromeDriver(options);
    }

    private DriverManager(){}

    public static WebDriver getDriver(){
        return driver;
    }

    public static WebDriverWait getDriverWait() {
        if (Objects.isNull(driverWait)) {
            driverWait = new WebDriverWait(driver, 10);
        }
        return driverWait;
    }

    public static void waitWhilePageLoad(int seconds){
        driver.manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
    }
}
