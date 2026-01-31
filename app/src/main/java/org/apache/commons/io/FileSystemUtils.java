package org.apache.commons.io;

import cn.yunzhisheng.common.PinyinConverter;
import com.unisound.vui.priority.PriorityMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/* loaded from: classes.dex */
public class FileSystemUtils {
    private static final String DF;
    private static final int INIT_PROBLEM = -1;
    private static final FileSystemUtils INSTANCE = new FileSystemUtils();
    private static final int OS;
    private static final int OTHER = 0;
    private static final int POSIX_UNIX = 3;
    private static final int UNIX = 2;
    private static final int WINDOWS = 1;

    static {
        int os;
        String osName;
        String dfPath = "df";
        try {
            osName = System.getProperty("os.name");
        } catch (Exception e) {
            os = -1;
        }
        if (osName == null) {
            throw new IOException("os.name not found");
        }
        String osName2 = osName.toLowerCase(Locale.ENGLISH);
        if (osName2.indexOf("windows") != -1) {
            os = 1;
        } else if (osName2.indexOf("linux") != -1 || osName2.indexOf("mpe/ix") != -1 || osName2.indexOf("freebsd") != -1 || osName2.indexOf("irix") != -1 || osName2.indexOf("digital unix") != -1 || osName2.indexOf("unix") != -1 || osName2.indexOf("mac os x") != -1) {
            os = 2;
        } else if (osName2.indexOf("sun os") != -1 || osName2.indexOf("sunos") != -1 || osName2.indexOf("solaris") != -1) {
            os = 3;
            dfPath = "/usr/xpg4/bin/df";
        } else if (osName2.indexOf("hp-ux") != -1 || osName2.indexOf("aix") != -1) {
            os = 3;
        } else {
            os = 0;
        }
        OS = os;
        DF = dfPath;
    }

    @Deprecated
    public static long freeSpace(String path) throws IOException {
        return INSTANCE.freeSpaceOS(path, OS, false, -1L);
    }

    public static long freeSpaceKb(String path) throws IOException {
        return freeSpaceKb(path, -1L);
    }

    public static long freeSpaceKb(String path, long timeout) throws IOException {
        return INSTANCE.freeSpaceOS(path, OS, true, timeout);
    }

    public static long freeSpaceKb() throws IOException {
        return freeSpaceKb(-1L);
    }

    public static long freeSpaceKb(long timeout) throws IOException {
        return freeSpaceKb(new File(".").getAbsolutePath(), timeout);
    }

    long freeSpaceOS(String path, int os, boolean kb, long timeout) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be empty");
        }
        switch (os) {
            case 0:
                throw new IllegalStateException("Unsupported operating system");
            case 1:
                return kb ? freeSpaceWindows(path, timeout) / FileUtils.ONE_KB : freeSpaceWindows(path, timeout);
            case 2:
                return freeSpaceUnix(path, kb, false, timeout);
            case 3:
                return freeSpaceUnix(path, kb, true, timeout);
            default:
                throw new IllegalStateException("Exception caught when determining operating system");
        }
    }

    long freeSpaceWindows(String path, long timeout) throws Throwable {
        String path2 = FilenameUtils.normalize(path, false);
        if (path2.length() > 0 && path2.charAt(0) != '\"') {
            path2 = "\"" + path2 + "\"";
        }
        String[] cmdAttribs = {"cmd.exe", "/C", "dir /a /-c " + path2};
        List<String> lines = performCommand(cmdAttribs, PriorityMap.PRIORITY_MAX, timeout);
        for (int i = lines.size() - 1; i >= 0; i--) {
            String line = lines.get(i);
            if (line.length() > 0) {
                return parseDir(line, path2);
            }
        }
        throw new IOException("Command line 'dir /-c' did not return any info for path '" + path2 + "'");
    }

    long parseDir(String line, String path) throws IOException {
        int bytesStart = 0;
        int bytesEnd = 0;
        int j = line.length() - 1;
        while (true) {
            if (j < 0) {
                break;
            }
            if (Character.isDigit(line.charAt(j))) {
                bytesEnd = j + 1;
                break;
            }
            j--;
        }
        while (true) {
            if (j < 0) {
                break;
            }
            char c = line.charAt(j);
            if (!Character.isDigit(c) && c != ',' && c != '.') {
                bytesStart = j + 1;
                break;
            }
            j--;
        }
        if (j < 0) {
            throw new IOException("Command line 'dir /-c' did not return valid info for path '" + path + "'");
        }
        StringBuilder buf = new StringBuilder(line.substring(bytesStart, bytesEnd));
        int k = 0;
        while (k < buf.length()) {
            if (buf.charAt(k) == ',' || buf.charAt(k) == '.') {
                buf.deleteCharAt(k);
                k--;
            }
            k++;
        }
        return parseBytes(buf.toString(), path);
    }

    long freeSpaceUnix(String path, boolean kb, boolean posix, long timeout) throws Throwable {
        if (path.length() == 0) {
            throw new IllegalArgumentException("Path must not be empty");
        }
        String flags = kb ? "-k" : "-";
        if (posix) {
            flags = flags + "P";
        }
        String[] cmdAttribs = flags.length() > 1 ? new String[]{DF, flags, path} : new String[]{DF, path};
        List<String> lines = performCommand(cmdAttribs, 3, timeout);
        if (lines.size() < 2) {
            throw new IOException("Command line '" + DF + "' did not return info as expected for path '" + path + "'- response was " + lines);
        }
        String line2 = lines.get(1);
        StringTokenizer tok = new StringTokenizer(line2, PinyinConverter.PINYIN_SEPARATOR);
        if (tok.countTokens() < 4) {
            if (tok.countTokens() == 1 && lines.size() >= 3) {
                String line3 = lines.get(2);
                tok = new StringTokenizer(line3, PinyinConverter.PINYIN_SEPARATOR);
            } else {
                throw new IOException("Command line '" + DF + "' did not return data as expected for path '" + path + "'- check path is valid");
            }
        } else {
            tok.nextToken();
        }
        tok.nextToken();
        tok.nextToken();
        String freeSpace = tok.nextToken();
        return parseBytes(freeSpace, path);
    }

    long parseBytes(String freeSpace, String path) throws NumberFormatException, IOException {
        try {
            long bytes = Long.parseLong(freeSpace);
            if (bytes < 0) {
                throw new IOException("Command line '" + DF + "' did not find free space in response for path '" + path + "'- check path is valid");
            }
            return bytes;
        } catch (NumberFormatException ex) {
            throw new IOExceptionWithCause("Command line '" + DF + "' did not return numeric data as expected for path '" + path + "'- check path is valid", ex);
        }
    }

    List<String> performCommand(String[] cmdAttribs, int max, long timeout) throws Throwable {
        List<String> lines = new ArrayList<>(20);
        Process proc = null;
        InputStream in = null;
        OutputStream out = null;
        InputStream err = null;
        BufferedReader inr = null;
        try {
            try {
                Thread monitor = ThreadMonitor.start(timeout);
                proc = openProcess(cmdAttribs);
                in = proc.getInputStream();
                out = proc.getOutputStream();
                err = proc.getErrorStream();
                BufferedReader inr2 = new BufferedReader(new InputStreamReader(in));
                try {
                    for (String line = inr2.readLine(); line != null && lines.size() < max; line = inr2.readLine()) {
                        lines.add(line.toLowerCase(Locale.ENGLISH).trim());
                    }
                    proc.waitFor();
                    ThreadMonitor.stop(monitor);
                    if (proc.exitValue() != 0) {
                        throw new IOException("Command line returned OS error code '" + proc.exitValue() + "' for command " + Arrays.asList(cmdAttribs));
                    }
                    if (lines.isEmpty()) {
                        throw new IOException("Command line did not return any info for command " + Arrays.asList(cmdAttribs));
                    }
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                    IOUtils.closeQuietly(err);
                    IOUtils.closeQuietly((Reader) inr2);
                    if (proc != null) {
                        proc.destroy();
                    }
                    return lines;
                } catch (InterruptedException e) {
                    ex = e;
                    throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(cmdAttribs) + " timeout=" + timeout, ex);
                } catch (Throwable th) {
                    th = th;
                    inr = inr2;
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                    IOUtils.closeQuietly(err);
                    IOUtils.closeQuietly((Reader) inr);
                    if (proc != null) {
                        proc.destroy();
                    }
                    throw th;
                }
            } catch (InterruptedException e2) {
                ex = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    Process openProcess(String[] cmdAttribs) throws IOException {
        return Runtime.getRuntime().exec(cmdAttribs);
    }
}
