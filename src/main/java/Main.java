import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.util.List;

public class Main {

    static WebDriver driver;
    static final String URL = "https://www.my188.com/vi-vn/asia";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/dattrinh/Documents/creative-workspace/drivers/chromedriver");
        driver = new ChromeDriver();
        String handle = driver.getWindowHandle();
        driver.switchTo().window(handle);
        driver.get(URL);
        login();
        switchFrame();



        WebElement match = find_specific_match("Guangzhou");
        String current_score = get_current_score_of_match(match);
        wait_for_condition(match, "2-2");
        bet_if_have_wanted_score(match, 2);     // 1 = home, 2 = away





        driver.quit();
    }

    private static void bet_if_have_wanted_score(WebElement match, int teamSide) {  // 1: home , 2: away
        if (teamSide == 1) {    // home
            match.findElement(By.xpath("tbody[2]/tr[1]/td[5]/span/span[2]")).click();
        } else {    // away
            match.findElement(By.xpath("tbody[2]/tr[2]/td[4]/span/span[2]")).click();
        }
        sleep(500);
        driver.findElement(By.xpath("//div[@val='1000000']")).click();
        sleep(200);
        driver.findElement(By.xpath("//div[text()='Chấp nhận thay đổi' or text()='Đặt Cược']")).click();
    }

    private static void wait_for_condition(WebElement match, String conditions) {    // format: 2-0, 2-1
        System.out.println("Waiting for wanted score...");
        WebDriverWait wait = new WebDriverWait(driver, 60 * 90);
        wait.until(ExpectedConditions.textToBePresentInElement(match.findElement(By.xpath("tbody[2]/tr[1]/td[1]/div[1]")), conditions));
//        System.out.println("Wanted score is available now");
    }

    private static String get_current_score_of_match(WebElement match) {
        String score = match.findElement(By.xpath("tbody[2]/tr[1]/td[1]/div[1]")).getText();
        System.out.println("current score : " + score);
        return score;
    }

    private static WebElement find_specific_match(String name) {
//        WebElement element = driver.findElement(By.xpath("//div[@id='cnr-odds']//div[@class='standard-view inplay']//table[tbody][not(@pid)]//tbody[2]/tr[1]/td[2]/span[contains(text(), '" + name + "')]"));
        try {
            return driver.findElement(By.xpath("//div[@id='cnr-odds']//span[contains(text(), '" + name + "')]/ancestor::table[not(@pid)]"));
        } catch (Exception e) {
            System.out.println("Match is not available");
            return null;
        }
    }




    private static List<WebElement> get_matches_in_game() {
        List<WebElement> list = driver.findElements(By.xpath("//div[@id='cnr-odds']//div[@class='standard-view inplay']//table[tbody][not(@pid)]"));
        System.out.println("all bets in game : " + list.size());
        return list;
    }

    // including special bet: corner, extra time
    private static List<WebElement> get_all_bets_ingame() {
        List<WebElement> list = driver.findElements(By.xpath("//div[@id='cnr-odds']//div[@class='standard-view inplay']//table[tbody][@pid]"));
        System.out.println("all bets in game : " + list.size());
        return list;
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
