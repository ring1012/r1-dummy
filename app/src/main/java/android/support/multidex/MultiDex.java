package android.support.multidex;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

/* loaded from: classes.dex */
public final class MultiDex {
    private static final int MAX_SUPPORTED_SDK_VERSION = 20;
    private static final int MIN_SDK_VERSION = 4;
    static final String TAG = "MultiDex";
    private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
    private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;
    private static final String OLD_SECONDARY_FOLDER_NAME = "secondary-dexes";
    private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator + OLD_SECONDARY_FOLDER_NAME;
    private static final Set<String> installedApk = new HashSet();
    private static final boolean IS_VM_MULTIDEX_CAPABLE = isVMMultidexCapable(System.getProperty("java.vm.version"));

    private MultiDex() {
    }

    public static void install(Context context) {
        Log.i(TAG, "install");
        if (IS_VM_MULTIDEX_CAPABLE) {
            Log.i(TAG, "VM has multidex support, MultiDex support library is disabled.");
            return;
        }
        if (Build.VERSION.SDK_INT < 4) {
            throw new RuntimeException("Multi dex installation failed. SDK " + Build.VERSION.SDK_INT + " is unsupported. Min SDK version is 4.");
        }
        try {
            ApplicationInfo applicationInfo = getApplicationInfo(context);
            if (applicationInfo != null) {
                synchronized (installedApk) {
                    String apkPath = applicationInfo.sourceDir;
                    if (!installedApk.contains(apkPath)) {
                        installedApk.add(apkPath);
                        if (Build.VERSION.SDK_INT > 20) {
                            Log.w(TAG, "MultiDex is not guaranteed to work in SDK version " + Build.VERSION.SDK_INT + ": SDK version higher than 20 should be backed by runtime with built-in multidex capabilty but it's not the case here: java.vm.version=\"" + System.getProperty("java.vm.version") + "\"");
                        }
                        try {
                            ClassLoader loader = context.getClassLoader();
                            if (loader == null) {
                                Log.e(TAG, "Context class loader is null. Must be running in test mode. Skip patching.");
                                return;
                            }
                            try {
                                clearOldDexDir(context);
                            } catch (Throwable t) {
                                Log.w(TAG, "Something went wrong when trying to clear old MultiDex extraction, continuing without cleaning.", t);
                            }
                            File dexDir = new File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME);
                            List<File> files = MultiDexExtractor.load(context, applicationInfo, dexDir, false);
                            if (checkValidZipFiles(files)) {
                                installSecondaryDexes(loader, dexDir, files);
                            } else {
                                Log.w(TAG, "Files were not valid zip files.  Forcing a reload.");
                                List<File> files2 = MultiDexExtractor.load(context, applicationInfo, dexDir, true);
                                if (checkValidZipFiles(files2)) {
                                    installSecondaryDexes(loader, dexDir, files2);
                                } else {
                                    throw new RuntimeException("Zip files were not valid.");
                                }
                            }
                            Log.i(TAG, "install done");
                        } catch (RuntimeException e) {
                            Log.w(TAG, "Failure while trying to obtain Context class loader. Must be running in test mode. Skip patching.", e);
                        }
                    }
                }
            }
        } catch (Exception e2) {
            Log.e(TAG, "Multidex installation failure", e2);
            throw new RuntimeException("Multi dex installation failed (" + e2.getMessage() + ").");
        }
    }

    private static ApplicationInfo getApplicationInfo(Context context) throws PackageManager.NameNotFoundException {
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            if (pm == null || packageName == null) {
                return null;
            }
            return pm.getApplicationInfo(packageName, 128);
        } catch (RuntimeException e) {
            Log.w(TAG, "Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching.", e);
            return null;
        }
    }

    static boolean isVMMultidexCapable(String versionString) throws NumberFormatException {
        boolean isMultidexCapable = false;
        if (versionString != null) {
            Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
            if (matcher.matches()) {
                try {
                    int major = Integer.parseInt(matcher.group(1));
                    int minor = Integer.parseInt(matcher.group(2));
                    isMultidexCapable = major > 2 || (major == 2 && minor >= 1);
                } catch (NumberFormatException e) {
                }
            }
        }
        Log.i(TAG, "VM with version " + versionString + (isMultidexCapable ? " has multidex support" : " does not have multidex support"));
        return isMultidexCapable;
    }

    private static void installSecondaryDexes(ClassLoader loader, File dexDir, List<File> files) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, IOException, IllegalArgumentException, InvocationTargetException {
        if (!files.isEmpty()) {
            if (Build.VERSION.SDK_INT < 19) {
                if (Build.VERSION.SDK_INT >= 14) {
                    V14.install(loader, files, dexDir);
                    return;
                } else {
                    V4.install(loader, files);
                    return;
                }
            }
            V19.install(loader, files, dexDir);
        }
    }

    private static boolean checkValidZipFiles(List<File> files) {
        for (File file : files) {
            if (!MultiDexExtractor.verifyZipFile(file)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Field findField(Object instance, String name) throws NoSuchFieldException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
            }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Method findMethod(Object instance, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Method method = clazz.getDeclaredMethod(name, parameterTypes);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            } catch (NoSuchMethodException e) {
            }
        }
        throw new NoSuchMethodException("Method " + name + " with parameters " + Arrays.asList(parameterTypes) + " not found in " + instance.getClass());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void expandFieldArray(Object instance, String fieldName, Object[] extraElements) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        Field jlrField = findField(instance, fieldName);
        Object[] original = (Object[]) jlrField.get(instance);
        Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(), original.length + extraElements.length);
        System.arraycopy(original, 0, combined, 0, original.length);
        System.arraycopy(extraElements, 0, combined, original.length, extraElements.length);
        jlrField.set(instance, combined);
    }

    private static void clearOldDexDir(Context context) throws Exception {
        File dexDir = new File(context.getFilesDir(), OLD_SECONDARY_FOLDER_NAME);
        if (dexDir.isDirectory()) {
            Log.i(TAG, "Clearing old secondary dex dir (" + dexDir.getPath() + ").");
            File[] files = dexDir.listFiles();
            if (files == null) {
                Log.w(TAG, "Failed to list secondary dex dir content (" + dexDir.getPath() + ").");
                return;
            }
            for (File oldFile : files) {
                Log.i(TAG, "Trying to delete old file " + oldFile.getPath() + " of size " + oldFile.length());
                if (!oldFile.delete()) {
                    Log.w(TAG, "Failed to delete old file " + oldFile.getPath());
                } else {
                    Log.i(TAG, "Deleted old file " + oldFile.getPath());
                }
            }
            if (!dexDir.delete()) {
                Log.w(TAG, "Failed to delete secondary dex dir " + dexDir.getPath());
            } else {
                Log.i(TAG, "Deleted old secondary dex dir " + dexDir.getPath());
            }
        }
    }

    private static final class V19 {
        private V19() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void install(ClassLoader loader, List<File> additionalClassPathEntries, File optimizedDirectory) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
            IOException[] dexElementsSuppressedExceptions;
            Field pathListField = MultiDex.findField(loader, "pathList");
            Object dexPathList = pathListField.get(loader);
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            MultiDex.expandFieldArray(dexPathList, "dexElements", makeDexElements(dexPathList, new ArrayList(additionalClassPathEntries), optimizedDirectory, suppressedExceptions));
            if (suppressedExceptions.size() > 0) {
                Iterator i$ = suppressedExceptions.iterator();
                while (i$.hasNext()) {
                    IOException e = i$.next();
                    Log.w(MultiDex.TAG, "Exception in makeDexElement", e);
                }
                Field suppressedExceptionsField = MultiDex.findField(loader, "dexElementsSuppressedExceptions");
                IOException[] dexElementsSuppressedExceptions2 = (IOException[]) suppressedExceptionsField.get(loader);
                if (dexElementsSuppressedExceptions2 == null) {
                    dexElementsSuppressedExceptions = (IOException[]) suppressedExceptions.toArray(new IOException[suppressedExceptions.size()]);
                } else {
                    IOException[] combined = new IOException[suppressedExceptions.size() + dexElementsSuppressedExceptions2.length];
                    suppressedExceptions.toArray(combined);
                    System.arraycopy(dexElementsSuppressedExceptions2, 0, combined, suppressedExceptions.size(), dexElementsSuppressedExceptions2.length);
                    dexElementsSuppressedExceptions = combined;
                }
                suppressedExceptionsField.set(loader, dexElementsSuppressedExceptions);
            }
        }

        private static Object[] makeDexElements(Object dexPathList, ArrayList<File> files, File optimizedDirectory, ArrayList<IOException> suppressedExceptions) throws IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
            Method makeDexElements = MultiDex.findMethod(dexPathList, "makeDexElements", ArrayList.class, File.class, ArrayList.class);
            return (Object[]) makeDexElements.invoke(dexPathList, files, optimizedDirectory, suppressedExceptions);
        }
    }

    private static final class V14 {
        private V14() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void install(ClassLoader loader, List<File> additionalClassPathEntries, File optimizedDirectory) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
            Field pathListField = MultiDex.findField(loader, "pathList");
            Object dexPathList = pathListField.get(loader);
            MultiDex.expandFieldArray(dexPathList, "dexElements", makeDexElements(dexPathList, new ArrayList(additionalClassPathEntries), optimizedDirectory));
        }

        private static Object[] makeDexElements(Object dexPathList, ArrayList<File> files, File optimizedDirectory) throws IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
            Method makeDexElements = MultiDex.findMethod(dexPathList, "makeDexElements", ArrayList.class, File.class);
            return (Object[]) makeDexElements.invoke(dexPathList, files, optimizedDirectory);
        }
    }

    private static final class V4 {
        private V4() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void install(ClassLoader loader, List<File> additionalClassPathEntries) throws IllegalAccessException, NoSuchFieldException, IOException, IllegalArgumentException {
            int extraSize = additionalClassPathEntries.size();
            Field pathField = MultiDex.findField(loader, "path");
            StringBuilder path = new StringBuilder((String) pathField.get(loader));
            String[] extraPaths = new String[extraSize];
            File[] extraFiles = new File[extraSize];
            ZipFile[] extraZips = new ZipFile[extraSize];
            DexFile[] extraDexs = new DexFile[extraSize];
            ListIterator<File> iterator = additionalClassPathEntries.listIterator();
            while (iterator.hasNext()) {
                File additionalEntry = iterator.next();
                String entryPath = additionalEntry.getAbsolutePath();
                path.append(':').append(entryPath);
                int index = iterator.previousIndex();
                extraPaths[index] = entryPath;
                extraFiles[index] = additionalEntry;
                extraZips[index] = new ZipFile(additionalEntry);
                extraDexs[index] = DexFile.loadDex(entryPath, entryPath + ".dex", 0);
            }
            pathField.set(loader, path.toString());
            MultiDex.expandFieldArray(loader, "mPaths", extraPaths);
            MultiDex.expandFieldArray(loader, "mFiles", extraFiles);
            MultiDex.expandFieldArray(loader, "mZips", extraZips);
            MultiDex.expandFieldArray(loader, "mDexs", extraDexs);
        }
    }
}
