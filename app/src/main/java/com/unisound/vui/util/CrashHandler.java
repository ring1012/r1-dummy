package com.unisound.vui.util;

import android.os.Debug;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String OOM = "java.lang.OutOfMemoryError";
    public static final String TAG = "CrashHandler";
    private static CrashHandler instance;
    private String crashTime;
    private ExecutorService threadPool;
    public static final String CRASH_FILE = "/unisound/vui/crash/";
    private static final String ROOT_PATH = Environment.getExternalStorageDirectory() + CRASH_FILE;
    public static final String HPROF_FILE_PATH = ROOT_PATH + "hprof/";
    private Map<String, String> infos = new HashMap();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private class LogDumper extends Thread {
        private String cmds;
        private Process logcatProc;
        private String mPID;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        private FileOutputStream out;

        public LogDumper() throws IOException {
            this.cmds = null;
            this.out = null;
            try {
                String str = "trace-" + CrashHandler.this.crashTime + ".log";
                if (Environment.getExternalStorageState().equals("mounted")) {
                    File file = new File(CrashHandler.ROOT_PATH);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    this.mPID = String.valueOf(Process.myPid());
                    File file2 = new File(CrashHandler.ROOT_PATH + str);
                    if (!file2.exists()) {
                        file2.createNewFile();
                    }
                    this.out = new FileOutputStream(file2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            this.cmds = "logcat -vtime | grep " + this.mPID;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws IOException {
            try {
                try {
                    this.logcatProc = Runtime.getRuntime().exec(this.cmds);
                    this.mReader = new BufferedReader(new InputStreamReader(this.logcatProc.getInputStream()), 1024);
                    while (true) {
                        String line = this.mReader.readLine();
                        if (line == null || !this.mRunning) {
                            break;
                        }
                        if (line.length() != 0 && this.out != null && line.contains(this.mPID)) {
                            this.out.write((line + "\n").getBytes());
                        }
                    }
                    if (this.logcatProc != null) {
                        this.logcatProc.destroy();
                        this.logcatProc = null;
                    }
                    if (this.mReader != null) {
                        try {
                            this.mReader.close();
                            this.mReader = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (this.out != null) {
                        try {
                            this.out.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        this.out = null;
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    if (this.logcatProc != null) {
                        this.logcatProc.destroy();
                        this.logcatProc = null;
                    }
                    if (this.mReader != null) {
                        try {
                            this.mReader.close();
                            this.mReader = null;
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    if (this.out != null) {
                        try {
                            this.out.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                        this.out = null;
                    }
                }
            } catch (Throwable th) {
                if (this.logcatProc != null) {
                    this.logcatProc.destroy();
                    this.logcatProc = null;
                }
                if (this.mReader != null) {
                    try {
                        this.mReader.close();
                        this.mReader = null;
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                }
                if (this.out == null) {
                    throw th;
                }
                try {
                    this.out.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
                this.out = null;
                throw th;
            }
        }

        @Override // java.lang.Thread
        public synchronized void start() {
            super.start();
            new Timer().schedule(new TimerTask() { // from class: com.unisound.vui.util.CrashHandler.LogDumper.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    LogDumper.this.mRunning = false;
                    Process.killProcess(Process.myPid());
                }
            }, 2000L);
        }
    }

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        this.threadPool.execute(new Runnable() { // from class: com.unisound.vui.util.CrashHandler.1
            @Override // java.lang.Runnable
            public void run() throws IOException {
                CrashHandler.this.saveCrashInfo(ex);
                if (CrashHandler.isOOM(ex)) {
                    try {
                        new File(CrashHandler.HPROF_FILE_PATH).mkdirs();
                        Debug.dumpHprofData(CrashHandler.HPROF_FILE_PATH + (CrashHandler.this.crashTime + ".hprof"));
                    } catch (Exception e) {
                        LogMgr.e(CrashHandler.TAG, "couldn’t dump hprof %s", e);
                    }
                }
            }
        });
        return true;
    }

    public static boolean isOOM(Throwable throwable) {
        LogMgr.d(TAG, "getName : %s", throwable.getClass().getName());
        if (OOM.equals(throwable.getClass().getName())) {
            return true;
        }
        Throwable cause = throwable.getCause();
        if (cause != null) {
            return isOOM(cause);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String saveCrashInfo(Throwable ex) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : this.infos.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        stringBuffer.append(stringWriter.toString());
        try {
            String str = "crash-" + this.crashTime + ".log";
            if (!Environment.getExternalStorageState().equals("mounted")) {
                return str;
            }
            File file = new File(ROOT_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(ROOT_PATH + str);
            fileOutputStream.write(stringBuffer.toString().getBytes());
            fileOutputStream.close();
            return str;
        } catch (Exception e) {
            LogMgr.e(TAG, "an error occured while writing file:" + e);
            return null;
        }
    }

    public void init() {
        this.threadPool = Executors.newSingleThreadExecutor();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, ex + " - uncaughtException - ");
        this.crashTime = this.formatter.format(new Date());
        handleException(ex);
        new LogDumper().start();
    }
}
