package edu.udacity.java.nano;


import edu.udacity.java.nano.chat.WebSocketChatServer;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.test.web.servlet.MockMvc;
import org.openqa.selenium.WebDriver;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.is;


import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebSocketChatApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebSocketChatApplicationTest {

    private WebDriver user1;
    private WebDriver user2;



    @LocalServerPort
    private Long localPort;



    @Autowired
    private WebSocketChatServer chatServer;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        user1 = new ChromeDriver();
        user2 = new ChromeDriver();
    }

    @After
    public void tearDown() {
        user1.quit();
        user2.quit();
    }

    @Test
    public void test1Login() throws Exception {
        System.out.println(this.localPort);
        user1.get("http://localhost:" + this.localPort + "/");
        user1.findElement(By.id("username")).click();
        user1.findElement(By.id("username")).sendKeys("Kaustav");
        user1.findElement(By.linkText("Login")).click();
    }

    @Test
    public void test2Logout() throws Exception {

        user1.get("http://localhost:" + this.localPort + "/");

        user1.findElement(By.id("username")).click();
        user1.findElement(By.id("username")).sendKeys("Kaustav");
        user1.findElement(By.linkText("Login")).click();
        assertEquals(user1.findElement(By.id("username")).getText(), "Kaustav");
        user1.findElement(By.cssSelector(".mdui-btn > .mdui-icon")).click();
        assertEquals(user1.findElement(By.cssSelector("h3")).getText(), "Chat Room");

    }

    @Test
    public void test3Join() {

        //1st user
        user1.get("http://localhost:" + this.localPort + "/");
        user1.findElement(By.id("username")).click();
        user1.findElement(By.id("username")).sendKeys("Kaustav");
        user1.findElement(By.linkText("Login")).click();
        assertEquals(user1.findElement(By.id("username")).getText(), "Kaustav");

        // 2nd User
        user2.manage().deleteAllCookies();
        user2.get("http://localhost:" + this.localPort + "/");
        user2.manage().window().setSize(new Dimension(1671, 1128));
        user2.findElement(By.id("username")).click();
        user2.findElement(By.id("username")).sendKeys("Mridu");
        user2.findElement(By.linkText("Login")).click();
        assertEquals(user2.findElement(By.id("username")).getText(), "Mridu");

    }

    @Test
    public void test4Send() throws Exception {
        user1.manage().deleteAllCookies();
        user1.get("http://localhost:" + this.localPort + "/");
        user1.findElement(By.id("username")).click();
        user1.findElement(By.id("username")).sendKeys("Kaustav");
        user1.findElement(By.linkText("Login")).click();
        assertEquals(user1.findElement(By.id("username")).getText(), "Kaustav");
        user1.findElement(By.id("msg")).click();
        user1.findElement(By.id("msg")).sendKeys("Hello World");
        user1.findElement(By.id("msg")).sendKeys(Keys.ENTER);

        // Check if sent message was printed
        assertEquals(user1.findElement(By.cssSelector(".mdui-card:nth-child(2) .mdui-card-content")).getText(), "Kaustav：Hello World");

    }

}
