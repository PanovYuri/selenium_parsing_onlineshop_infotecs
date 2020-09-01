package main;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Main {

    private static final String URL = "https://mistoreufa.ru/index.php?route=product/isearch&sort=p.price&order=ASC&search=%s";
    private static WebDriver driver;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Нет аргументов");
            return;
        }

        // Set main value
        System.setProperty("webdriver.gecko.driver", "/home/panov/coding/data/geckodriver");
        driver = new FirefoxDriver();
        driver.get(String.format(URL, args[0]));
        WebElement product = driver.findElement(By.className("caption"));

        String titleProduct = product
                .findElement(By.tagName("a"))
                .getText();
        WebElement priceProduct = product
                .findElement(By.className("price"));

        // Check new price block
        List<WebElement> arrPriceNew = priceProduct.findElements(By.className("price-new"));
        String priceString = arrPriceNew.size() != 0 ?
                arrPriceNew.get(0).getText() :
                priceProduct.getText().split("\n")[0];
        System.out.println(titleProduct + ": " + priceString);

        // Scroll for visible top products
        WebElement productBlock = driver.findElement(By.className("product-block"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", productBlock);

        // Make screenshot
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(srcFile.toPath(), Path.of("src/screenshots/top_products.jpg"), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Сохранение скриншота завершилась успешно");
        } catch (IOException e) {
            System.err.println("Проблемы с записью файла: " + e.getLocalizedMessage());
        }
    }
}
