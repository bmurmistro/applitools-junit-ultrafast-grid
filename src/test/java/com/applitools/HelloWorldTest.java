package com.applitools;

import com.applitools.eyes.selenium.fluent.Target;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class HelloWorldTest extends BaseTest
{
  @Test
  public void testHello() {
    open("https://applitools.com/helloworld");

    // Visual validation point #1.
    $(By.tagName("button")).click();
    eyesWatcher.eyesCheck("Hello!", Target.window());

    // Click the "Click me!" button.
    //$(By.tagName("button")).click();

    eyesWatcher.eyesCheck("Hello Thumbs up!", Target.window());
  }

  @Test
  public void testHello2() {
    open("https://applitools.com/helloworld");

    // Visual validation point #1.
    $(By.tagName("button")).click();
    eyesWatcher.eyesCheck("Hello!", Target.window());

    // Click the "Click me!" button.
    //$(By.tagName("button")).click();

    eyesWatcher.eyesCheck("Hello Thumbs up!", Target.window());
  }
}
