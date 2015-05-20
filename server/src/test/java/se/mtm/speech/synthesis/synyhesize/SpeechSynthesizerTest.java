package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SpeechSynthesizerTest {

    @Test
    public void synthezise_paragraph() throws Exception { // NOPMD
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer();
        speechSynthesizer.start();

        String key = "17";
        Paragraph paragraph = new Paragraph(key, "A sentence to be synthesized");

        boolean actualDelivery = speechSynthesizer.addParagraph(paragraph);

        while (speechSynthesizer.getParagraph(key) instanceof ParagraphNotReady) {
            Thread.sleep(1);
        }

        Paragraph actualParagraph = speechSynthesizer.getParagraph(key);

        speechSynthesizer.stop();

        assertTrue("Expected to be able to deliver a paragraph for synthetization", actualDelivery);
        assertThat(actualParagraph, is(paragraph));
    }

    // todo add a paragraph that isn't returned in time. A timeout should not be handled by returning null. Return a "timeout pargraph" instead
    // todo too many paragraphs and verify the it isn't accepted
}
