package com.applitools;

import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginTest
    extends BaseTest
{
  @Test
  public void testLogin() {
    open("http://demo.applitools.com/loginBefore.html");

    eyesWatcher.eyesCheck();
  }
}
