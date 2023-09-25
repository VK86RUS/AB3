package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CardSiteTest {
    private static WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
        driver = null;
    }

    @Test
    public void trueForm() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван Иванович");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79824133177");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualText = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        Assertions.assertEquals("  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actualText);
    }

    @Test
    void falseFormeName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivanov Ivan");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79876543210");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText());
    }

    @Test
    void nullFormePhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван Иванович");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        Assertions.assertEquals("Поле обязательно для заполнения",
                driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText());
    }

    @Test
    void falseFormePhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван Иванович");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+798241331777");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                driver.findElement((By.cssSelector("[data-test-id=phone].input_invalid .input__sub"))).getText());
    }
}