import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.util.List;

public class Main {

    private static final String URL = "https://www.my188.com/vi-vn/asia";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/dattrinh/Documents/creative-workspace/drivers/chromedriver");
        WebDriver driver = new ChromeDriver();
        Functions func = new Functions(driver);
        String handle = driver.getWindowHandle();
        driver.switchTo().window(handle);
        driver.get(URL);
        func.login();
        func.switchFrame();

        // find match by name
        String name = "Liefering";
        String matchID = func.find_if_of_match(name);
        func.get_current_score_of_match(matchID);
        boolean isLocking = func.check_match_locking(matchID);
        if (!isLocking) {
//            func.bet_FT(matchID, "0-0",2, "-0/0.5", "0.9");     // 1 = home, 2 = away
            func.bet_half_time(matchID, "0-0", 2, "-0/0.5", "0.90");
        }
        driver.quit();
    }







}
