import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class Main {

    static WebDriver driver;

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/dattrinh/Documents/creative-workspace/drivers/chromedriver");
        driver = new ChromeDriver();
//        String handle = driver.getWindowHandle();
//        driver.switchTo().window(handle);
        driver.get("https://www.my188.com/vi-vn/asia");
//        login();
        switchFrame();


        List<WebElement> listBets = driver.findElements(By.xpath("//div[@id='cnr-odds']//table[tbody][not(@pid)]"));
        System.out.println(listBets.size());



        driver.quit();
    }

    private static void switchFrame() {
        driver.switchTo().frame(0);     // class = rsiframe
//        driver.switchTo().frame("bnr-odds");
    }

    private static void login() {
        driver.findElement(By.xpath("//*[@class='input-username']/input")).sendKeys("heouuu9");
        driver.findElement(By.xpath("//*[@class='input-password']/input")).sendKeys("forever123");
        driver.findElement(By.xpath("//*[@type='submit']")).click();
        sleep(5000);
        System.out.println("Login successfully");
    }

    private static void sleep(int millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
