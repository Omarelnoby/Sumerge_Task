package com.example.pages;
//import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

//import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
public class reservation {
    private WebDriver  driver;
    private WebDriverWait wait;
    WebElement destination;
    WebElement date_picker;
    WebElement search_button;
  public reservation(WebDriver drive){
    this.driver=drive;
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    destination=driver.findElement(By.className("eb46370fe1"));
    destination=driver.findElement(By.className("eb46370fe1"));
    date_picker=driver.findElement(By.className("a8887b152e"));
    search_button=driver.findElement(By.xpath("//button[@type='submit']"));
  } 

  public void action(String destination,String check_in,String check_out) {
    
    //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    date_picker.click();
    //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    select_date(check_in);
    select_date(check_out);
String key="Alexandria";
    //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    select_destination(destination, key);

    //room_capacity.sendKeys();
   // driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

    search_button.click();
  } 
  /********************************************************* */
  public void select_hotel(String key){
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Find the hotel card that contains the specified hotel name
    String hotelXPath = "//div[@data-testid='title' and contains(text(), '" + key + "')]/ancestor::div[@data-testid='property-card']";

    // Find the "See Availability" button inside the hotel card
    String buttonXPath = hotelXPath + "//div[contains(@class, 'e4adce92df')]";

    // Wait for the button to be clickable
    WebElement seeAvailabilityButton = driver.findElement(By.xpath(buttonXPath));

    // Scroll into view and click
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", seeAvailabilityButton);
    seeAvailabilityButton.click();
  }
  /**************************************** */
  public void confirmation(){
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Find the "Reserve" button
    WebElement reserveButton = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//span[@class='bui-button__text js-reservation-button__text']"))
    );

    // Scroll into view & Click
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", reserveButton);
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", reserveButton);
   
  }
  /*** ******************************************************************************************** */
 
  @SuppressWarnings("unused")
  public void select_date(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
    LocalDate targetDate = LocalDate.parse(date, formatter);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    while (true) {
        // Get displayed months & years
        List<WebElement> displayedMonths = driver.findElements(By.xpath("//h3[@aria-live='polite']"));

        if (displayedMonths.size() < 2) {
            throw new RuntimeException("Date picker months not found.");
        }

        String leftMonthYear = displayedMonths.get(0).getText();
        String rightMonthYear = displayedMonths.get(1).getText();

        if (matchesDisplayedMonths(leftMonthYear, rightMonthYear, targetDate)) {
            break;  
        }

        try {
            WebElement nextMonthButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='Next month']")
            ));

            // Scroll into view & Click
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextMonthButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextMonthButton);
        } catch (ElementClickInterceptedException e) {
            System.out.println("Next button click intercepted. Retrying with JavaScript.");
            WebElement nextMonthButton = driver.findElement(By.xpath("//button[@aria-label='Next month']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextMonthButton);
        }
    }

    // Select the day
    formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
    targetDate = LocalDate.parse(date, formatter);

    String targetDateString = targetDate.toString(); // Converts to "YYYY-MM-DD" format

    WebDriverWait waits = new WebDriverWait(driver, Duration.ofSeconds(10));

    // Ensure the calendar is visible
    waits.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//table[contains(@class, 'eb03f3f27f')]")
    ));

    // Find and click the correct date
    WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//span[@data-date='" + targetDateString + "']")
    ));

    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dayElement);
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dayElement);
}
 /******************************************************************************** */
private boolean matchesDisplayedMonths(String leftMonthYear, String rightMonthYear, LocalDate targetDate) {
    DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMMM yyyy");
    String targetMonthYear = targetDate.format(displayFormat);

    return leftMonthYear.equalsIgnoreCase(targetMonthYear) ;
}
  /********************************************************************/
  //@SuppressWarnings("unused")
  private void select_destination(String key,String desired){
    destination.sendKeys(key);
   
        WebElement hotelOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(),'Tolip Hotel Alexandria')]")
            ));
            hotelOption.click();

  }
  /************************************************************* */
}
