package com.example.tests;
import com.example.base.base_test;
import com.example.data.ExcelDataProvider;
//import com.example.pages.reservation;
import com.example.pages.reservation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//import dev.failsafe.internal.util.Assert;
import java.util.Locale;

import org.testng.Assert;
import org.testng.annotations.Test;
public class reservation_test extends base_test {
   
  
  public reservation_test(){

  }

  /*************************************************************************************************************** */
  @Test(dataProvider = "ExcelData", dataProviderClass = ExcelDataProvider.class)
  public void test_printed_hotel_name(String destination, String key, String check_in, String check_out,String bed,String room) {
    if (driver == null) {
      System.out.println("Driver is NULL! Test cannot proceed.");
      Assert.fail("Driver is not initialized.");
  }
    reservation res=new reservation(driver);
   
    //String bed="twin";
    //String room="2";
    res.action(destination,key,check_in,check_out);
    res.confirmation(room,bed);
   
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
      WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[normalize-space()='Tolip Hotel Alexandria']")));

        // Assert that the hotel name is displayed
        Assert.assertTrue(element.isDisplayed(), "Element is not displayed.");
        Assert.assertEquals(element.getText(), key, "Hotel name does not match!"); 

  }
  /********************************************************************************************************************** */
 
  @Test(dataProvider = "ExcelData", dataProviderClass = ExcelDataProvider.class)
  public void test_printed_date(String destination, String key, String check_in, String check_out,String bed,String room) {
    if (driver == null) {
      System.out.println("Driver is NULL! Test cannot proceed.");
      Assert.fail("Driver is not initialized.");
  }
    reservation res=new reservation(driver);
   
    //String bed="twin";
    //String room="2";
    res.action(destination,key,check_in,check_out);
    res.confirmation(room,bed);
   
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
      WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[normalize-space()='Tolip Hotel Alexandria']")));

    // Convert entered date to LocalDate
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
        LocalDate checkInDate = LocalDate.parse(check_in, inputFormatter);
        LocalDate checkOutDate = LocalDate.parse(check_out, inputFormatter);

        // Convert to expected format: Thu, May 22, 2025
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy", Locale.ENGLISH);
        String expectedCheckIn = checkInDate.format(outputFormatter);
        String expectedCheckOut = checkOutDate.format(outputFormatter);

        // Locate elements
        WebElement checkInDateElement = driver.findElement(By.xpath("//h3[contains(text(),'Check-in')]/following-sibling::div" ));
        WebElement checkOutDateElement = driver.findElement(By.xpath("//h3[contains(text(),'Check-out')]/following-sibling::div" ));
        // Get actual text from webpage
        String actualCheckIn = checkInDateElement.getText();
        String actualCheckOut = checkOutDateElement.getText();

        // Assertions
        Assert.assertEquals(actualCheckIn, expectedCheckIn, "Check-in date does not match!");
        Assert.assertEquals(actualCheckOut, expectedCheckOut, "Check-out date does not match!");
  }

}
