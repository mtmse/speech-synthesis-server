package se.mtm.speech.synthesis.version;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class VersionResourceTest {
    @Test
    public void get_version() {
        VersionResource resource = new VersionResource();

        assertNotNull(resource.getVersion());
        assertNotNull(resource.getVersion().getVersion());
        assertNotNull(resource.getVersion().getBuildTime());
    }
}
