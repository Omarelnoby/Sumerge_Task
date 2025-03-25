package com.example.pages;
//import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

//import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

  public void action(String destination,String key,String check_in,String check_out) {
    
    //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    date_picker.click();
    //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    select_date(check_in);
    select_date(check_out);
    //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    select_destination(destination,key);

    //room_capacity.sendKeys();
   // driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

    search_button.click();
    select_hotel(key);  

  } 
  /********************************************************* */
  public void select_hotel(String key){

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    JavascriptExecutor js = (JavascriptExecutor) driver;

    // Define the XPath for the hotel card dynamically
    String hotelXPath = "//div[@data-testid='title' and contains(text(), '" + key + "')]/ancestor::div[@data-testid='property-card']";
    //String buttonXPath = hotelXPath + "//div[contains(@class, 'e4adce92df')]";

    String buttonXPath="//div[@class='c82435a4b8 a178069f51 a6ae3c2b40 a18aeea94d d794b7a0f7 f53e278e95 c6710787a4 ce662653f9 bbefc5a07c']//span[@class='e4adce92df'][normalize-space()='See availability']";
        // Wait for the hotel element to be visible
        WebElement hotelCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(hotelXPath)));

        // Scroll to the hotel card
        js.executeScript("arguments[0].scrollIntoView(true);", hotelCard);

        // Wait for the "See Availability" button to be clickable
        WebElement seeAvailabilityButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
        // Click the button
        js.executeScript("arguments[0].click();", seeAvailabilityButton);
        //switch to the new window
         
        String originalWindow = driver.getWindowHandle();

        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

          // Get all window handles
          Set<String> windowHandles = driver.getWindowHandles();
          for (String handle : windowHandles) {
              if (!handle.equals(originalWindow)) {
                  driver.switchTo().window(handle); // Switch to new tab
                  break;
              }
          }
    
  }
  /**************************************** */
  public void confirmation( String amount_rooms,String bed_type){
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    JavascriptExecutor js = (JavascriptExecutor) driver;
    WebElement lastElement = driver.findElement(By.xpath("//legend[@class='bui-f-font-heading bui-spacer--medium']"));
    js.executeScript("arguments[0].scrollIntoView(true);", lastElement);
    
    List<WebElement> dropdowns = driver.findElements(By.xpath("//select[starts-with(@id, 'hprt_nos_select_')]"));

// Select the first dropdown in the list

    WebElement firstDropdown = dropdowns.get(0);
    
    // Scroll to the element (optional)
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstDropdown);
    js.executeScript("arguments[0].scrollIntoView(true);", lastElement);

    // Click the dropdown
    //firstDropdown.click();

    // Select an option (e.g., selecting the first option)
    Select select = new Select(firstDropdown);

    //Select select = new Select(room_amount);
    select.selectByValue(amount_rooms);
    //select type of bed 
    //WebElement bedlabel=driver.findElement(By.cssSelector("input[value='1'][name='bedPreference_78883120']"));
    //if(bedlabel.isDisplayed()){
      WebElement bedOption;
    if (bed_type.toLowerCase().contains("twin")) {
      bedOption = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='1'][name='bedPreference_78883120']"))); // Second option (Twin)
    } else if (bed_type.toLowerCase().contains("queen")) {
        bedOption = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='2'][name='bedPreference_78883120']"))); // Second option (Twin)
    } else {
        throw new IllegalArgumentException("Invalid bed preference: " + bed_type);
    }
    bedOption.click();
    //}
    
    js.executeScript("arguments[0].scrollIntoView(true);", lastElement);

    // Find the "Reserve" button
        WebElement reserveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='bui-button__text js-reservation-button__text']")));
    
    //js.executeScript("arguments[0].scrollIntoView(true);", reserveButton);


      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", reserveButton);
   
  }
  /*** ******************************************************************************************** */
 public void select_date(String date) {
  // Define formatters
  DateTimeFormatter excelFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
  DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d");

  // Parse the input date
  LocalDate targetDate;
  try {
      targetDate = LocalDate.parse(date, excelFormatter);
  } catch (Exception e) {
      throw new RuntimeException("Error parsing date: " + date, e);
  }

  // Extract month-year format for the date picker
  String targetMonthYear = targetDate.format(monthYearFormatter); // Example: "September 2025"
  String targetDay = targetDate.format(dayFormatter); // Example: "11"

  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

  while (true) {
      // Get the displayed months
      List<WebElement> displayedMonths = driver.findElements(By.xpath("//h3[@aria-live='polite']"));

      if (displayedMonths.size() < 2) {
          throw new RuntimeException("Date picker months not found.");
      }

      String leftMonthYear = displayedMonths.get(0).getText();

      if (leftMonthYear.equalsIgnoreCase(targetMonthYear) ) {
          break;  // Month is displayed, exit loop
      }

      // Click "Next" button to navigate months
      WebElement nextMonthButton = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//button[@aria-label='Next month']")
      ));
      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextMonthButton);
  }

  // Select the day
  WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(
      By.xpath("//span[text()='" + targetDay + "']") // Adjust based on actual HTML structure
  ));
  //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dayElement);
  ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dayElement);
}

  /********************************************************************/
  //@SuppressWarnings("unused")
  private void select_destination(String dest,String key){
    destination.sendKeys(dest);
   
        WebElement hotelOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(),'" + key + "')]")
            ));
            hotelOption.click();

  }
  /************************************************************* */
}
