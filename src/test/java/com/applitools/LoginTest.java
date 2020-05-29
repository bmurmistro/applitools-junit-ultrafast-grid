package com.applitools;

import com.applitools.eyes.selenium.fluent.Target;
import org.junit.Test;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest
    extends BaseTest
{
  @Test
  public void testLoginBefore() {
    open("http://demo.applitools.com/loginBefore.html");

    eyesWatcher.eyesCheck(Target.window().fully());
  }

  @Test
  public void testLoginAfter() {
    open("http://demo.applitools.com/loginAfter.html");

    eyesWatcher.eyesCheck(Target.window().fully());
  }

  @Test
  public void testDoNothing() {
    open("http://demo.applitools.com/loginBefore.html");

    // Do nothing
  }
}
