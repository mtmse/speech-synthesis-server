package se.mtm.speech.synthesis.status;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StatusPage {
    private WebDriver browser;

    public StatusPage() {
    }

    public StatusPage(WebDriver browser) {
        this.browser = browser;

        String page = browser.getCurrentUrl();
        browser.get(page);

        String expectedTitle = "Speech Synthesis Server";
        String actualTitle = browser.getTitle();

        assertThat(actualTitle, is(expectedTitle));
    }

    public Date getCurrentPageGenerationTime() throws Exception {
        WebElement generationDate = browser.findElement(By.id("generationDate"));

        String rawGenerationDate = generationDate.getText();

        return parseGenerationDate(rawGenerationDate);
    }

    Date parseGenerationDate(String rawGenerationDate) throws Exception {
        Pattern pattern = Pattern.compile("Page generated at (.*)");
        Matcher matcher = pattern.matcher(rawGenerationDate);
        if (matcher.matches()) {
            String generationDate = matcher.group(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // NOPMD
            return sdf.parse(generationDate);
        }

        return null;
    }
}
