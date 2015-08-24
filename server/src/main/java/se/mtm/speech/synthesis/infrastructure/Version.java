package se.mtm.speech.synthesis.infrastructure;

import com.jcabi.manifests.Manifests;

public class Version {
    public String getVersion() {
        try {
            return Manifests.read("Version");
        } catch (Exception e) {
            return "version not found in jar file";
        }
    }

    public String getBuildTime() {
        try {
            return Manifests.read("Build-Date");
        } catch (Exception e) {
            return "build time not found in jar file";
        }
    }
}
