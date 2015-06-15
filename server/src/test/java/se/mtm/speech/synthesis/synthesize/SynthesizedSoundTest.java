package se.mtm.speech.synthesis.synthesize;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SynthesizedSoundTest {
    @Test
    public void create_valid_sound() {
        String key = "17";
        byte[] validSound = new byte[17];
        SynthesizedSound synthesizedSound = new SynthesizedSound(key, validSound);

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

    @Test
    public void same_hash_for_equal_instances() {
        String key = "17";
        byte[] sound = "The brown...".getBytes();
        SynthesizedSound first = new SynthesizedSound(key, sound);
        SynthesizedSound second = new SynthesizedSound(key, sound);

        assertThat(first.hashCode(), is(second.hashCode()));
    }

    @Test
    public void different_objects_are_not_same() {
        String key = "17";
        byte[] sound = "The brown...".getBytes();
        SynthesizedSound first = new SynthesizedSound(key, sound);
        String second = "different";

        //noinspection EqualsBetweenInconvertibleTypes
        assertFalse(first.equals(second));
    }
}
