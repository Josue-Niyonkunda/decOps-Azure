import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    static Playwright playwright;
    static Browser browser;

    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @BeforeEach
    void createPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://askomdch.com/account/");
    }

    @Test
    void testSuccessfulLogin() {

        page.locator("#username").fill("josueniyonkunda");
        page.locator("#password").fill("niyonkunda20");
        page.locator("[name='login']").click();

        String loginMessage = String.valueOf(page.getByRole(AriaRole.PARAGRAPH).nth(2));

//        assertTrue(loginMessage.contains("Hello josueniyonkunda"),
//                "Login message did not match expected text");
    }

    @Test
    void testRegisterExistingAccount() {

        page.locator("#reg_username").fill("josue");
        page.locator("[name='email']").fill("josue@gmail.com");
        page.locator("#reg_password").fill("niyonkunda20");

        page.locator("[name='register']").click();

        String registerMessage = page.locator(".woocommerce-error").textContent();

        assertTrue(
                registerMessage.contains("An account is already registered"),
                "Register error message did not contain expected text"
        );
    }

    @Test
    void testSuccessfulResetPassword() {

        page.locator("text=Lost your password?").click();

        page.locator(".woocommerce-Input.woocommerce-Input--text.input-text")
                .fill("josue");

        page.locator(".woocommerce-Button.button").click();

        String resetMessage = page.locator(".woocommerce-message").textContent();

        assertTrue(
                //trying

                resetMessage.contains("Password reset email has been sent."),
                "Password reset message did not match"
        );
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @AfterAll
    static void closeBrowser() {
       browser.close();
       playwright.close();
    }
}