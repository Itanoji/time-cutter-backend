package com.itanoji.time.cutter;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;


public class SeleniumTests {
    private static WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    @BeforeAll
    public static void setDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
    @Test
    public void testRegistration() throws InterruptedException {
        driver.get("http://localhost/");
        driver.manage().window().setSize(new Dimension(1544, 764));
        driver.findElement(By.cssSelector(".right-5 > button")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".right-5 > button"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".underline")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".underline"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).sendKeys("lkjh@gmail.com");
        driver.findElement(By.cssSelector(".mb-4:nth-child(1)")).click();
        driver.findElement(By.id("email")).click();
        {
            WebElement element = driver.findElement(By.id("email"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        driver.findElement(By.id("login")).click();
        driver.findElement(By.id("login")).sendKeys("lkjh12345");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("lkjh12345");
        driver.findElement(By.cssSelector(".justify-center:nth-child(4) > .cursor-pointer")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.findElement( By.cssSelector(".text-red-600, .text-green-600"));
    }

    @Test
    public void testLogin() throws Exception {
        driver.get("http://localhost/");
        driver.manage().window().setSize(new Dimension(1544, 764));
        driver.findElement(By.cssSelector(".right-5 > button")).click();
        driver.findElement(By.id("login")).click();
        driver.findElement(By.id("login")).sendKeys("lkjh12345");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("lkjh12345");
        driver.findElement(By.cssSelector(".justify-center:nth-child(3) > .cursor-pointer")).click();
        driver.findElement(By.cssSelector(".right-5 > button")).click();
        driver.findElement(By.cssSelector(".right-5 > button"));
    }
}
