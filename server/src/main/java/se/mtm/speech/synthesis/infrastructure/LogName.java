package se.mtm.speech.synthesis.infrastructure;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public final class LogName {
    private static String logExtension = "log";

    private LogName() {
        // Utility classes should be final and have a private constructor...
    }

    public static String getLogFileName(String logHome) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);
        String today = sdf.format(new Date());
        String leading = "filibuster-" + today + "-";

        File logDir = new File(logHome);

        createMissingLogDir(logDir);

        Collection<File> files = getAllLogFiles(logDir);

        String number;
        if (files.isEmpty()) {
            number = padNumberString(1);
        } else {
            number = getNextNumber(leading, files);
        }


        return leading + number + "." + logExtension;
    }

    private static Collection<File> getAllLogFiles(File logDir) {
        String[] extensions = {logExtension};

        return FileUtils.listFiles(logDir, extensions, false);
    }

    private static String getNextNumber(String leading, Collection<File> files) {
        String lastLogFile = getLastLogFile(leading, files);

        if (noFilibusterLogFound(lastLogFile)) {
            return padNumberString(1);
        }

        int nextLog = getNextNumber(leading, lastLogFile);

        return padNumberString(nextLog);
    }

    private static boolean noFilibusterLogFound(String lastLogFile) {
        return lastLogFile.isEmpty();
    }

    private static String getLastLogFile(String leading, Collection<File> files) {
        List<String> logsToday = new LinkedList<>();

        for (File candidate : files) {
            String name = candidate.getName();
            if (name.startsWith(leading)) {
                logsToday.add(name);
            }
        }
        Collections.sort(logsToday);

        if (logsToday.isEmpty()) {
            return "";
        }

        return logsToday.get(logsToday.size() - 1);
    }

    private static int getNextNumber(String leading, String lastLog) {
        String logName = lastLog.substring(leading.length());
        String logNumber = logName.substring(0, logExtension.length() + 1);

        return Integer.parseInt(logNumber) + 1;
    }

    private static String padNumberString(int nextLogNumber) {
        String number;
        number = "" + nextLogNumber;
        int wantedLength = 4;

        while (number.length() < wantedLength) {
            number = "0" + number;
        }

        return number;
    }

    private static void createMissingLogDir(File logDir) {
        if (!logDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            logDir.mkdirs();
        }
    }

}
