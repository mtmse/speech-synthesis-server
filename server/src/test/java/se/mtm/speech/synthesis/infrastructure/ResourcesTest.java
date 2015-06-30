package se.mtm.speech.synthesis.infrastructure;

import org.apache.commons.io.FileUtils;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourcesTest {

    @Test
    public void get_available_memory() throws Exception {
        Resources.prepareNativeLibs("tmp");
        Sigar sigar = mock(Sigar.class);
        Mem mem = mock(Mem.class);
        when(sigar.getMem()).thenReturn(mem);
        long expected = 5881163776l;
        when(mem.getFree()).thenReturn(expected);

        Resources resources = new Resources(sigar);

        int expectedGigabyte = (int) (expected / FileUtils.ONE_GB);
        int actual = resources.getAvailableMemory();

        assertThat(actual, is(expectedGigabyte));
    }
}
