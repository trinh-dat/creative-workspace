import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.util.List;

public class Main {

    static WebDriver driver;
    static WebDriverWait wait;
    static final String URL = "https://www.my188.com/vi-vn/asia";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/dattrinh/Documents/creative-workspace/drivers/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 60 * 90);
        String handle = driver.getWindowHandle();
        driver.switchTo().window(handle);
        driver.get(URL);
        login();
        switchFrame();


        String id_of_match = find_if_of_match("Shakhtar Donetsk");
        get_current_score_of_match(id_of_match);
        boolean isLocking = check_match_locking(id_of_match);
        if (!isLocking) {
            bet_if_have_wanted_score(id_of_match, "0-1",2, "-0.5/1");     // 1 = home, 2 = away
            // TODO: Implement method bet_half_time
//            bet_half_time(id_of_match, "0-0", "0/0.5", 2);
        }



        driver.quit();
    }

    private static void bet_half_time(String id_of_match, String match_score, String odd, int team_side) {

    }

    private static boolean check_match_locking(String id_of_match) {
        // span[@class='odds locked']


        String current_score = driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[1]/div[1]")).getText();
        if (current_score != null) {
            return false;
        }
        return true;
    }

    private static void wait_for_condition(String id_of_match, String match_score, int teamSide, String odd) {
        System.out.println("Waiting for wanted bet...");
        wait_for_correct_score(id_of_match, match_score);
        wait_for_odd(id_of_match, teamSide, odd);
        System.out.println("Wanted bet is available now");
    }

    private static void wait_for_odd(String id_of_match, int teamSide, String odd) {
        if (teamSide == 1) {    // home
            wait.until(ExpectedConditions.attributeToBe(driver.findElement(By.xpath("//table[@id='\" + id_of_match + \"']/tbody[2]/tr[1]/td[5]/span/span[2]")), "hdp", odd));
        } else {    // away
            wait.until(ExpectedConditions.attributeToBe(driver.findElement(By.xpath("//table[@id='\" + id_of_match + \"']/tbody[2]/tr[2]/td[4]/span/span[2]")), "hdp", odd));
        }

        wait.until(ExpectedConditions.attributeToBe(driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[1]/div[1]")), "hdp", odd));
    }

    private static void wait_for_correct_score(String id_of_match, String match_score) {
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[1]/div[1]")), match_score));
            System.out.println("Match score is: " + match_score);
        } catch (Exception e) {
            System.out.println("There is no wanted score");
        }


    }

    private static String get_current_score_of_match(String id_of_match) {
        String score = driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[1]/div[1]")).getText();
        System.out.println("current score : " + score);
        return score;
    }

    private static void bet_if_have_wanted_score(String id_of_match, String match_score, int teamSide, String odd) {  // 1: home , 2: away
        wait_for_condition(id_of_match, match_score, teamSide, odd);     // ex condition = "5-5"
        select_Odd(id_of_match, "FT", teamSide);
        bet(1000000);   // money lines: 1000000, 500000

    }

    private static void select_Odd(String id_of_match, String half_or_full, int teamSide) {
        if (half_or_full.equals("HT")) {
            if (teamSide == 1) {    // home
                driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[10]/span/span[2]")).click();
            } else {    // away
                driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[2]/td[9]/span/span[2]")).click();
            }
        } else if(half_or_full.equals("FT")) {
            if (teamSide == 1) {    // home
                driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[5]/span/span[2]")).click();
            } else {    // away
                driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[2]/td[4]/span/span[2]")).click();
            }
        }
        sleep(500);
    }

    private static void bet(int money) {
        driver.findElement(By.xpath("//div[@val='" + money + "']")).click();
        sleep(200);
        driver.findElement(By.xpath("//div[text()='Chấp nhận thay đổi' or text()='Đặt Cược']")).click();
        System.out.println("Bet successfully");
    }

    private static void wait_for_condition_by_element(WebElement match, String conditions) {    // format: 2-0, 2-1
        System.out.println("Waiting for wanted score...");
        WebDriverWait wait = new WebDriverWait(driver, 60 * 90);
        wait.until(ExpectedConditions.textToBePresentInElement(match.findElement(By.xpath("tbody[2]/tr[1]/td[1]/div[1]")), conditions));
//        System.out.println("Wanted score is available now");
    }

    private static String get_current_score_of_match_by_element(WebElement match) {
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

    private static String find_if_of_match(String name) {
        String id_of_match = "";
//        WebElement element = driver.findElement(By.xpath("//div[@id='cnr-odds']//div[@class='standard-view inplay']//table[tbody][not(@pid)]//tbody[2]/tr[1]/td[2]/span[contains(text(), '" + name + "')]"));
        try {
            id_of_match = driver.findElement(By.xpath("//div[@id='cnr-odds']//span[contains(text(), '" + name + "')]/ancestor::table[not(@pid)]")).getAttribute("id");
            System.out.println("Match ID: " + id_of_match);
        } catch (Exception e) {
            System.out.println("Match is not available");
        }
        return id_of_match;
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
