package se.mtm.speech.synthesis.synyhesize;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SpeechSynthesizerTest {
    private SpeechSynthesizer speechSynthesizer;

    @Before
    public void setUp() throws Exception {
        speechSynthesizer = new SpeechSynthesizer(1);
        speechSynthesizer.start();
    }

    @After
    public void tearDown() throws Exception {
        speechSynthesizer.stop();
    }

    @Test
    public void synthezise_paragraph() throws Exception { // NOPMD
        String key = "17";
        Paragraph paragraph = new Paragraph(key, "A sentence to be synthesized");

        boolean actualDelivery = speechSynthesizer.addParagraph(paragraph);

        while (speechSynthesizer.getParagraph(key) instanceof ParagraphNotReady) {
            Thread.sleep(1);
        }

        Paragraph actualParagraph = speechSynthesizer.getParagraph(key);

        assertTrue("Expected to be able to deliver a paragraph for synthetization", actualDelivery);
        assertThat(actualParagraph, is(paragraph));
    }

    @Test
    public void add_too_many_sentences_and_verify_that_que_is_full() {
        Paragraph paragraph = new Paragraph("17", "A sentence to be synthesized");

        boolean firstDelivery = speechSynthesizer.addParagraph(paragraph);
        boolean secondDelivery = speechSynthesizer.addParagraph(paragraph);

        assertTrue("The que can accept one item", firstDelivery);
        assertFalse("The que can accept one item", secondDelivery);
    }


    // todo add a paragraph that isn't returned in time. A timeout should not be handled by returning null. Return a "timeout pargraph" instead
    // todo add many paragraphs and verify the are consumed after a while
}
