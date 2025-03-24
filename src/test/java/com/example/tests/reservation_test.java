package com.example.tests;
import com.example.base.base_test;
//import com.example.data.ExcelDataProvider;
//import com.example.pages.reservation;
import com.example.pages.reservation;

//import dev.failsafe.internal.util.Assert;

import org.testng.Assert;
import org.testng.annotations.Test;
public class reservation_test extends base_test {
   
  
  public reservation_test(){

  }
  /*************************************************************************************************************** */
  @Test
  //(dataProvider = "ExcelData", dataProviderClass = ExcelDataProvider.class)(String check_in, String check_out)
  public void testValidReservation() {
    if (driver == null) {
      System.out.println("Driver is NULL! Test cannot proceed.");
      Assert.fail("Driver is not initialized.");
  }
    reservation res=new reservation(driver);
    String dest="Alexandria";
    String desired="Tolip Hotel Alexandria";
    String check_in="22/May/2025";
    String check_out="25/May/2025";
    res.action(dest,check_in,check_out);
    res.select_hotel(desired);
    Assert.assertTrue(driver.getCurrentUrl().contains("search"), "Reservation failed!");
     /// Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"), "Login failed!");
   
     Assert.assertTrue(true);

  }
  /********************************************************************************************************************** */
/* 
  @Test
  public void testInvalidLogin() {
    //reservation res=new reservation(driver);
    //res.action();


      //String error = loginPage.getErrorMessage();
     // Assert.assertEquals(error, "Invalid username or password", "Error message is incorrect!");
     Assert.assertTrue(true);
  }*/
}
