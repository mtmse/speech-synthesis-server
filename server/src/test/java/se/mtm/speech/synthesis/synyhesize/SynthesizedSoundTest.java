package se.mtm.speech.synthesis.synyhesize;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SynthesizedSoundTest {
    @Test
    public void create_valid_sound() {
        byte[] validSound = new byte[17];
        SynthesizedSound synthesizedSound = new SynthesizedSound(validSound);

        assertFalse("A valid synthesizedSound should not timeout", synthesizedSound.isTimeout());

        byte[] actualSound = synthesizedSound.getSound();
        assertThat(actualSound, is(validSound));
    }

    @Test
    public void create_a_timeout_sound() {
        SynthesizedSound synthesizedSound = new SynthesizedSound();

        assertTrue("A invalid synthesizedSound should timeout", synthesizedSound.isTimeout());

        byte[] actualSound = synthesizedSound.getSound();
        assertThat(actualSound.length, is(0));
    }
}
