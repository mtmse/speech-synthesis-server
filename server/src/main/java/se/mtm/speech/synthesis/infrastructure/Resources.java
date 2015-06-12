package se.mtm.speech.synthesis.infrastructure;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Resources {
    private final Sigar sigar;

    public Resources() {
        prepareNativeLibs("tmp");
        sigar = new Sigar();
    }

    Resources(Sigar sigar) {
        this.sigar = sigar;
    }

    public static void prepareNativeLibs(String directory) {
        try {
            makeLibrariesKnown(directory);
            copyLibrariesToDisk(directory);
        } catch (IOException e) {
            throw new FilibusterException(e.getMessage(), e);
        }
    }

    private static void makeLibrariesKnown(String directory) {
        System.setProperty("java.library.path", directory);
    }

    private static void copyLibrariesToDisk(String directory) throws IOException {
        ClassLoader classLoader = Resources.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("sigar");
        List<String> files = IOUtils.readLines(resourceAsStream, Charsets.UTF_8);

        File targetDir = new File(directory);
        //noinspection ResultOfMethodCallIgnored
        targetDir.mkdirs();

        for (String fileName : files) {
            InputStream fileStream = classLoader.getResourceAsStream("sigar/" + fileName);
            File targetFile = new File(directory + "/" + fileName);
            if (!targetFile.exists()) {
                FileUtils.copyInputStreamToFile(fileStream, targetFile);
            }
        }
    }

    /**
     * @return the remaining memory in gigabytes
     */
    public int getAvailableMemory() {
        Mem mem;
        try {
            mem = sigar.getMem();
        } catch (SigarException e) {
            throw new FilibusterException(e.getMessage(), e);
        }
        return (int) (mem.getActualFree() / FileUtils.ONE_GB);
    }
}
