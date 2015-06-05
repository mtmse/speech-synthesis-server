package se.mtm.speech.synthesis.status;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StatusPage {
    private final WebDriver browser;

    public StatusPage(WebDriver browser) {
        this.browser = browser;

        String page = browser.getCurrentUrl() ;
        browser.get(page);

        String expectedTitle = "Speech synthesis status";
        String actualTitle = browser.getTitle();

        assertThat(actualTitle, is(expectedTitle));
    }

    public Date getCurrentPageGenerationTime() {
        WebElement generationDate = browser.findElement(By.id("generationDate"));

        String rawGenerationDate = generationDate.getText();

        return parseGenerationDate(rawGenerationDate);
    }

    Date parseGenerationDate(String rawGenerationDate) {
        // todo implement me...
        return new Date();
    }
}
