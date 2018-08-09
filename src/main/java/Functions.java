import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Functions {

    private WebDriver driver;
    private WebDriverWait wait;

    Functions(WebDriver driver) {
        this.driver = driver;
         wait = new WebDriverWait(driver, 60 * 90);
    }

    public void bet_half_time(String id_of_match, String match_score, int team_side, String hdp, String odd) {
        wait_for_condition(id_of_match, match_score, team_side, hdp, odd);     // ex condition = "5-5"
        select_Odd(id_of_match, "HT", team_side);
        bet(1000000);   // money lines: 1000000, 500000
    }

    private void wait_for_condition(String id_of_match, String match_score, int teamSide, String hdp, String odd) {
        System.out.println("Waiting for wanted bet...");
        wait_for_correct_score(id_of_match, match_score);
        wait_for_odd_FT(id_of_match, teamSide, hdp, odd);
//        wait_for_odd_HT(id_of_match, teamSide, odd);
        System.out.println("Wanted bet is available now");
    }

    public void wait_for_odd_HT(String id_of_match, int teamSide, String odd) {
        if (teamSide == 1) {    // home
            wait.until(ExpectedConditions.attributeToBe(driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[10]/span/span[2]")), "hdp", odd));
        } else {    // away
            wait.until(ExpectedConditions.attributeToBe(driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[2]/td[9]/span/span[2]")), "hdp", odd));
        }
    }

    private void wait_for_odd_FT(String id_of_match, int teamSide, String hdp, String odd) {
        if (teamSide == 1) {    // home
            wait.until(ExpectedConditions.attributeToBe(driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[5]/span/span[2]")), "hdp", hdp));
        } else {    // away
            By locator = By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[2]/td[4]/span/span[2]");
            wait.until(ExpectedConditions.attributeToBe(locator, "hdp", hdp));
        }
    }

    private void wait_for_correct_score(String id_of_match, String match_score) {
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[1]/div[1]")), match_score));
            System.out.println("Match score is: " + match_score);
        } catch (Exception e) {
            System.out.println("There is no wanted score");
        }
    }

    void get_current_score_of_match(String id_of_match) {
        String score = driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[1]/div[1]")).getText();
        System.out.println("current score : " + score);
    }

    void bet_FT(String id_of_match, String match_score, int teamSide, String hdp, String odd) {  // 1: home , 2: away
        wait_for_condition(id_of_match, match_score, teamSide, hdp, odd);     // ex condition = "5-5"
        select_Odd(id_of_match, "FT", teamSide);
        bet(1000000);   // money lines: 1000000, 500000
    }

    private void select_Odd(String id_of_match, String half_or_full, int teamSide) {
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

    private void bet(int money) {
        driver.findElement(By.xpath("//div[@val='" + money + "']")).click();
        sleep(200);
        driver.findElement(By.xpath("//div[text()='Chấp nhận thay đổi' or text()='Đặt Cược']")).click();
        System.out.println("Bet successfully");
    }

    public void wait_for_condition_by_element(WebElement match, String conditions) {    // format: 2-0, 2-1
        System.out.println("Waiting for wanted score...");
        WebDriverWait wait = new WebDriverWait(driver, 60 * 90);
        wait.until(ExpectedConditions.textToBePresentInElement(match.findElement(By.xpath("tbody[2]/tr[1]/td[1]/div[1]")), conditions));
//        System.out.println("Wanted score is available now");
    }

    public String get_current_score_of_match_by_element(WebElement match) {
        String score = match.findElement(By.xpath("tbody[2]/tr[1]/td[1]/div[1]")).getText();
        System.out.println("current score : " + score);
        return score;
    }

    public WebElement find_specific_match(String name) {
//        WebElement element = driver.findElement(By.xpath("//div[@id='cnr-odds']//div[@class='standard-view inplay']//table[tbody][not(@pid)]//tbody[2]/tr[1]/td[2]/span[contains(text(), '" + name + "')]"));
        try {
            return driver.findElement(By.xpath("//div[@id='cnr-odds']//span[contains(text(), '" + name + "')]/ancestor::table[not(@pid)]"));
        } catch (Exception e) {
            System.out.println("Match is not available");
            return null;
        }
    }

    String find_if_of_match(String name) {
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

    public List<WebElement> get_matches_in_game() {
        List<WebElement> list = driver.findElements(By.xpath("//div[@id='cnr-odds']//div[@class='standard-view inplay']//table[tbody][not(@pid)]"));
        System.out.println("all bets in game : " + list.size());
        return list;
    }

    // including special bet: corner, extra time
    public List<WebElement> get_all_bets_ingame() {
        List<WebElement> list = driver.findElements(By.xpath("//div[@id='cnr-odds']//div[@class='standard-view inplay']//table[tbody][@pid]"));
        System.out.println("all bets in game : " + list.size());
        return list;
    }

    void switchFrame() {
        driver.switchTo().frame(0);     // class = rsiframe
//        driver.switchTo().frame("bnr-odds");
    }

    void login() {
        driver.findElement(By.xpath("//*[@class='input-username']/input")).sendKeys("heouuu9");
        driver.findElement(By.xpath("//*[@class='input-password']/input")).sendKeys("forever123");
        driver.findElement(By.xpath("//*[@type='submit']")).click();
        sleep(5000);
        System.out.println("Login successfully");
    }

    private void sleep(int millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    boolean check_match_locking(String id_of_match) {
        // span[@class='odds locked']
        String current_score = driver.findElement(By.xpath("//table[@id='" + id_of_match + "']/tbody[2]/tr[1]/td[1]/div[1]")).getText();
        if (current_score != null) {
            return false;
        }
        return true;
    }

}
