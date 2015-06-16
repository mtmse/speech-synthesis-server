package se.mtm.speech.synthesis.status;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import se.mtm.speech.synthesis.Main;
import se.mtm.speech.synthesis.infrastructure.Configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertTrue;

public class AcceptanceTest {
    @Rule
    public final DropwizardAppRule<Configuration> application =
            new DropwizardAppRule<>(Main.class, ResourceHelpers.resourceFilePath("test-configuration.yaml"));
    private WebDriver browser;

    @Before
    public void setUp() {
        browser = new HtmlUnitDriver();
//        browser = new FirefoxDriver();
        String host = "localhost";
        int port = application.getLocalPort();

        String baseUrl = "http://" + host + ":" + port;

        browser.get(baseUrl);
    }

    @After
    public void tearDown() {
        browser.quit();
    }

    @Test
    public void watch_status_page() throws Exception {
        Date oldestAcceptable = getDate(-1);
        Date newestAcceptable = getDate(1);

        StatusPage page = new StatusPage(browser);

        Date generationDate = page.getCurrentPageGenerationTime();

        assertTrue("Expected the generation date to be quite recently but " +
                        "it was <" + generationDate + ">",
                generationDate.after(oldestAcceptable));

        assertTrue("Expected the generation date to be quite recently but " +
                        "it was <" + newestAcceptable + ">",
                generationDate.before(newestAcceptable));
    }

    private Date getDate(int minute) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.roll(Calendar.MINUTE, minute);

        return calendar.getTime();
    }
}
