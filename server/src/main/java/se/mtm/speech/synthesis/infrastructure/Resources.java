package se.mtm.speech.synthesis.infrastructure;

import org.apache.commons.io.FileUtils;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class Resources {
    private static final Logger LOGGER = LoggerFactory.getLogger(Resources.class);
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
            String message = "Directory: <" + directory + "> message: " + e.getMessage();
            LOGGER.error(message, e);
            throw new FilibusterException(e.getMessage(), e);
        }
    }

    private static void makeLibrariesKnown(String directory) {
        System.setProperty("java.library.path", directory);
    }

    private static void copyLibrariesToDisk(String directory) throws IOException {
        List<String> libraries = new LinkedList<>();

        libraries.add(".sigar_shellrc");
        libraries.add("libsigar-amd64-freebsd-6.so");
        libraries.add("libsigar-amd64-linux.so");
        libraries.add("libsigar-amd64-solaris.so");
        libraries.add("libsigar-ia64-hpux-11.sl");
        libraries.add("libsigar-ia64-linux.so");
        libraries.add("libsigar-pa-hpux-11.sl");
        libraries.add("libsigar-ppc-aix-5.so");
        libraries.add("libsigar-ppc-linux.so");
        libraries.add("libsigar-ppc64-aix-5.so");
        libraries.add("libsigar-ppc64-linux.so");
        libraries.add("libsigar-s390x-linux.so");
        libraries.add("libsigar-sparc-solaris.so");
        libraries.add("libsigar-sparc64-solaris.so");
        libraries.add("libsigar-universal-macosx.dylib");
        libraries.add("libsigar-universal64-macosx.dylib");
        libraries.add("libsigar-x86-freebsd-5.so");
        libraries.add("libsigar-x86-freebsd-6.so");
        libraries.add("libsigar-x86-linux.so");
        libraries.add("libsigar-x86-solaris.so");
        libraries.add("sigar-amd64-winnt.dll");
        libraries.add("sigar-x86-winnt.dll");
        libraries.add("sigar-x86-winnt.lib");

        for (String library : libraries) {
            copyLibrary(library, directory);
        }
    }

    private static void copyLibrary(String library, String directory) throws IOException {
        URL inputUrl = Resources.class.getResource("/sigar/" + library);
        File dest = new File(directory + "/" + library);
        if (!dest.exists()) {
            FileUtils.copyURLToFile(inputUrl, dest);
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
