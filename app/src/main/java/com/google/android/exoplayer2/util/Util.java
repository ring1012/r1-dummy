package com.google.android.exoplayer2.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import cn.yunzhisheng.asr.JniUscClient;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public final class Util {
    private static final int[] CRC32_BYTES_MSBF;
    public static final String DEVICE;
    public static final String DEVICE_DEBUG_INFO;
    private static final Pattern ESCAPED_CHARACTER_PATTERN;
    public static final String MANUFACTURER;
    public static final String MODEL;
    public static final int SDK_INT;
    private static final String TAG = "Util";
    private static final Pattern XS_DATE_TIME_PATTERN;
    private static final Pattern XS_DURATION_PATTERN;

    static {
        SDK_INT = (Build.VERSION.SDK_INT == 25 && Build.VERSION.CODENAME.charAt(0) == 'O') ? 26 : Build.VERSION.SDK_INT;
        DEVICE = Build.DEVICE;
        MANUFACTURER = Build.MANUFACTURER;
        MODEL = Build.MODEL;
        DEVICE_DEBUG_INFO = DEVICE + ", " + MODEL + ", " + MANUFACTURER + ", " + SDK_INT;
        XS_DATE_TIME_PATTERN = Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt](\\d\\d):(\\d\\d):(\\d\\d)([\\.,](\\d+))?([Zz]|((\\+|\\-)(\\d\\d):?(\\d\\d)))?");
        XS_DURATION_PATTERN = Pattern.compile("^(-)?P(([0-9]*)Y)?(([0-9]*)M)?(([0-9]*)D)?(T(([0-9]*)H)?(([0-9]*)M)?(([0-9.]*)S)?)?$");
        ESCAPED_CHARACTER_PATTERN = Pattern.compile("%([A-Fa-f0-9]{2})");
        CRC32_BYTES_MSBF = new int[]{0, 79764919, 159529838, 222504665, 319059676, 398814059, 445009330, 507990021, 638119352, 583659535, 797628118, 726387553, 890018660, 835552979, 1015980042, 944750013, 1276238704, 1221641927, 1167319070, 1095957929, 1595256236, 1540665371, 1452775106, 1381403509, 1780037320, 1859660671, 1671105958, 1733955601, 2031960084, 2111593891, 1889500026, 1952343757, -1742489888, -1662866601, -1851683442, -1788833735, -1960329156, -1880695413, -2103051438, -2040207643, -1104454824, -1159051537, -1213636554, -1284997759, -1389417084, -1444007885, -1532160278, -1603531939, -734892656, -789352409, -575645954, -646886583, -952755380, -1007220997, -827056094, -898286187, -231047128, -151282273, -71779514, -8804623, -515967244, -436212925, -390279782, -327299027, 881225847, 809987520, 1023691545, 969234094, 662832811, 591600412, 771767749, 717299826, 311336399, 374308984, 453813921, 533576470, 25881363, 88864420, 134795389, 214552010, 2023205639, 2086057648, 1897238633, 1976864222, 1804852699, 1867694188, 1645340341, 1724971778, 1587496639, 1516133128, 1461550545, 1406951526, 1302016099, 1230646740, 1142491917, 1087903418, -1398421865, -1469785312, -1524105735, -1578704818, -1079922613, -1151291908, -1239184603, -1293773166, -1968362705, -1905510760, -2094067647, -2014441994, -1716953613, -1654112188, -1876203875, -1796572374, -525066777, -462094256, -382327159, -302564546, -206542021, -143559028, -97365931, -17609246, -960696225, -1031934488, -817968335, -872425850, -709327229, -780559564, -600130067, -654598054, 1762451694, 1842216281, 1619975040, 1682949687, 2047383090, 2127137669, 1938468188, 2001449195, 1325665622, 1271206113, 1183200824, 1111960463, 1543535498, 1489069629, 1434599652, 1363369299, 622672798, 568075817, 748617968, 677256519, 907627842, 853037301, 1067152940, 995781531, 51762726, 131386257, 177728840, 240578815, 269590778, 349224269, 429104020, 491947555, -248556018, -168932423, -122852000, -60002089, -500490030, -420856475, -341238852, -278395381, -685261898, -739858943, -559578920, -630940305, -1004286614, -1058877219, -845023740, -916395085, -1119974018, -1174433591, -1262701040, -1333941337, -1371866206, -1426332139, -1481064244, -1552294533, -1690935098, -1611170447, -1833673816, -1770699233, -2009983462, -1930228819, -2119160460, -2056179517, 1569362073, 1498123566, 1409854455, 1355396672, 1317987909, 1246755826, 1192025387, 1137557660, 2072149281, 2135122070, 1912620623, 1992383480, 1753615357, 1816598090, 1627664531, 1707420964, 295390185, 358241886, 404320391, 483945776, 43990325, 106832002, 186451547, 266083308, 932423249, 861060070, 1041341759, 986742920, 613929101, 542559546, 756411363, 701822548, -978770311, -1050133554, -869589737, -924188512, -693284699, -764654318, -550540341, -605129092, -475935807, -413084042, -366743377, -287118056, -257573603, -194731862, -114850189, -35218492, -1984365303, -1921392450, -2143631769, -2063868976, -1698919467, -1635936670, -1824608069, -1744851700, -1347415887, -1418654458, -1506661409, -1561119128, -1129027987, -1200260134, -1254728445, -1309196108};
    }

    private Util() {
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while (true) {
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != -1) {
                outputStream.write(buffer, 0, bytesRead);
            } else {
                return outputStream.toByteArray();
            }
        }
    }

    @TargetApi(23)
    public static boolean maybeRequestReadExternalStoragePermission(Activity activity, Uri... uris) {
        if (SDK_INT < 23) {
            return false;
        }
        for (Uri uri : uris) {
            if (isLocalFileUri(uri)) {
                if (activity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) {
                    return false;
                }
                activity.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 0);
                return true;
            }
        }
        return false;
    }

    public static boolean isLocalFileUri(Uri uri) {
        String scheme = uri.getScheme();
        return TextUtils.isEmpty(scheme) || scheme.equals("file");
    }

    public static boolean areEqual(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public static boolean contains(Object[] items, Object item) {
        for (Object arrayItem : items) {
            if (areEqual(arrayItem, item)) {
                return true;
            }
        }
        return false;
    }

    public static ExecutorService newSingleThreadExecutor(final String threadName) {
        return Executors.newSingleThreadExecutor(new ThreadFactory() { // from class: com.google.android.exoplayer2.util.Util.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, threadName);
            }
        });
    }

    public static void closeQuietly(DataSource dataSource) {
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (IOException e) {
            }
        }
    }

    public static void closeQuietly(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static String normalizeLanguageCode(String language) {
        if (language == null) {
            return null;
        }
        return new Locale(language).getLanguage();
    }

    public static byte[] getUtf8Bytes(String value) {
        return value.getBytes(Charset.defaultCharset());
    }

    public static boolean isLinebreak(int c) {
        return c == 10 || c == 13;
    }

    public static String toLowerInvariant(String text) {
        if (text == null) {
            return null;
        }
        return text.toLowerCase(Locale.US);
    }

    public static int ceilDivide(int numerator, int denominator) {
        return ((numerator + denominator) - 1) / denominator;
    }

    public static long ceilDivide(long numerator, long denominator) {
        return ((numerator + denominator) - 1) / denominator;
    }

    public static int constrainValue(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static long constrainValue(long value, long min, long max) {
        return Math.max(min, Math.min(value, max));
    }

    public static float constrainValue(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public static int binarySearchFloor(int[] array, int value, boolean inclusive, boolean stayInBounds) {
        int index = Arrays.binarySearch(array, value);
        if (index < 0) {
            index = -(index + 2);
        } else {
            do {
                index--;
                if (index < 0) {
                    break;
                }
            } while (array[index] == value);
            if (inclusive) {
                index++;
            }
        }
        return stayInBounds ? Math.max(0, index) : index;
    }

    public static int binarySearchFloor(long[] array, long value, boolean inclusive, boolean stayInBounds) {
        int index = Arrays.binarySearch(array, value);
        if (index < 0) {
            index = -(index + 2);
        } else {
            do {
                index--;
                if (index < 0) {
                    break;
                }
            } while (array[index] == value);
            if (inclusive) {
                index++;
            }
        }
        return stayInBounds ? Math.max(0, index) : index;
    }

    public static int binarySearchCeil(long[] array, long value, boolean inclusive, boolean stayInBounds) {
        int index = Arrays.binarySearch(array, value);
        if (index < 0) {
            index ^= -1;
        } else {
            do {
                index++;
                if (index >= array.length) {
                    break;
                }
            } while (array[index] == value);
            if (inclusive) {
                index--;
            }
        }
        return stayInBounds ? Math.min(array.length - 1, index) : index;
    }

    public static <T> int binarySearchFloor(List<? extends Comparable<? super T>> list, T value, boolean inclusive, boolean stayInBounds) {
        int index = Collections.binarySearch(list, value);
        if (index < 0) {
            index = -(index + 2);
        } else {
            do {
                index--;
                if (index < 0) {
                    break;
                }
            } while (list.get(index).compareTo(value) == 0);
            if (inclusive) {
                index++;
            }
        }
        return stayInBounds ? Math.max(0, index) : index;
    }

    public static <T> int binarySearchCeil(List<? extends Comparable<? super T>> list, T value, boolean inclusive, boolean stayInBounds) {
        int index = Collections.binarySearch(list, value);
        if (index < 0) {
            index ^= -1;
        } else {
            int listSize = list.size();
            do {
                index++;
                if (index >= listSize) {
                    break;
                }
            } while (list.get(index).compareTo(value) == 0);
            if (inclusive) {
                index--;
            }
        }
        return stayInBounds ? Math.min(list.size() - 1, index) : index;
    }

    public static long parseXsDuration(String value) {
        Matcher matcher = XS_DURATION_PATTERN.matcher(value);
        if (matcher.matches()) {
            boolean negated = !TextUtils.isEmpty(matcher.group(1));
            String years = matcher.group(3);
            double durationSeconds = years != null ? Double.parseDouble(years) * 3.1556908E7d : 0.0d;
            String months = matcher.group(5);
            double durationSeconds2 = durationSeconds + (months != null ? Double.parseDouble(months) * 2629739.0d : 0.0d);
            String days = matcher.group(7);
            double durationSeconds3 = durationSeconds2 + (days != null ? Double.parseDouble(days) * 86400.0d : 0.0d);
            String hours = matcher.group(10);
            double durationSeconds4 = durationSeconds3 + (hours != null ? Double.parseDouble(hours) * 3600.0d : 0.0d);
            String minutes = matcher.group(12);
            double durationSeconds5 = durationSeconds4 + (minutes != null ? Double.parseDouble(minutes) * 60.0d : 0.0d);
            String seconds = matcher.group(14);
            long durationMillis = (long) (1000.0d * (durationSeconds5 + (seconds != null ? Double.parseDouble(seconds) : 0.0d)));
            return negated ? -durationMillis : durationMillis;
        }
        return (long) (Double.parseDouble(value) * 3600.0d * 1000.0d);
    }

    public static long parseXsDateTime(String value) throws ParserException {
        int timezoneShift;
        Matcher matcher = XS_DATE_TIME_PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new ParserException("Invalid date/time format: " + value);
        }
        if (matcher.group(9) == null || matcher.group(9).equalsIgnoreCase("Z")) {
            timezoneShift = 0;
        } else {
            timezoneShift = (Integer.parseInt(matcher.group(12)) * 60) + Integer.parseInt(matcher.group(13));
            if (matcher.group(11).equals("-")) {
                timezoneShift *= -1;
            }
        }
        Calendar dateTime = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        dateTime.clear();
        dateTime.set(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)));
        if (!TextUtils.isEmpty(matcher.group(8))) {
            BigDecimal bd = new BigDecimal("0." + matcher.group(8));
            dateTime.set(14, bd.movePointRight(3).intValue());
        }
        long time = dateTime.getTimeInMillis();
        if (timezoneShift != 0) {
            return time - (60000 * timezoneShift);
        }
        return time;
    }

    public static long scaleLargeTimestamp(long timestamp, long multiplier, long divisor) {
        if (divisor >= multiplier && divisor % multiplier == 0) {
            long divisionFactor = divisor / multiplier;
            return timestamp / divisionFactor;
        }
        if (divisor < multiplier && multiplier % divisor == 0) {
            long multiplicationFactor = multiplier / divisor;
            return timestamp * multiplicationFactor;
        }
        double multiplicationFactor2 = multiplier / divisor;
        return (long) (timestamp * multiplicationFactor2);
    }

    public static long[] scaleLargeTimestamps(List<Long> timestamps, long multiplier, long divisor) {
        long[] scaledTimestamps = new long[timestamps.size()];
        if (divisor >= multiplier && divisor % multiplier == 0) {
            long divisionFactor = divisor / multiplier;
            for (int i = 0; i < scaledTimestamps.length; i++) {
                scaledTimestamps[i] = timestamps.get(i).longValue() / divisionFactor;
            }
        } else if (divisor < multiplier && multiplier % divisor == 0) {
            long multiplicationFactor = multiplier / divisor;
            for (int i2 = 0; i2 < scaledTimestamps.length; i2++) {
                scaledTimestamps[i2] = timestamps.get(i2).longValue() * multiplicationFactor;
            }
        } else {
            double multiplicationFactor2 = multiplier / divisor;
            for (int i3 = 0; i3 < scaledTimestamps.length; i3++) {
                scaledTimestamps[i3] = (long) (timestamps.get(i3).longValue() * multiplicationFactor2);
            }
        }
        return scaledTimestamps;
    }

    public static void scaleLargeTimestampsInPlace(long[] timestamps, long multiplier, long divisor) {
        if (divisor >= multiplier && divisor % multiplier == 0) {
            long divisionFactor = divisor / multiplier;
            for (int i = 0; i < timestamps.length; i++) {
                timestamps[i] = timestamps[i] / divisionFactor;
            }
            return;
        }
        if (divisor < multiplier && multiplier % divisor == 0) {
            long multiplicationFactor = multiplier / divisor;
            for (int i2 = 0; i2 < timestamps.length; i2++) {
                timestamps[i2] = timestamps[i2] * multiplicationFactor;
            }
            return;
        }
        double multiplicationFactor2 = multiplier / divisor;
        for (int i3 = 0; i3 < timestamps.length; i3++) {
            timestamps[i3] = (long) (timestamps[i3] * multiplicationFactor2);
        }
    }

    public static int[] toArray(List<Integer> list) {
        if (list == null) {
            return null;
        }
        int length = list.size();
        int[] intArray = new int[length];
        for (int i = 0; i < length; i++) {
            intArray[i] = list.get(i).intValue();
        }
        return intArray;
    }

    public static DataSpec getRemainderDataSpec(DataSpec dataSpec, int bytesLoaded) {
        if (bytesLoaded != 0) {
            long remainingLength = dataSpec.length != -1 ? dataSpec.length - bytesLoaded : -1L;
            return new DataSpec(dataSpec.uri, dataSpec.position + bytesLoaded, remainingLength, dataSpec.key, dataSpec.flags);
        }
        return dataSpec;
    }

    public static int getIntegerCodeForString(String string) {
        int length = string.length();
        Assertions.checkArgument(length <= 4);
        int result = 0;
        for (int i = 0; i < length; i++) {
            result = (result << 8) | string.charAt(i);
        }
        return result;
    }

    public static byte[] getBytesFromHexString(String hexString) {
        byte[] data = new byte[hexString.length() / 2];
        for (int i = 0; i < data.length; i++) {
            int stringOffset = i * 2;
            data[i] = (byte) ((Character.digit(hexString.charAt(stringOffset), 16) << 4) + Character.digit(hexString.charAt(stringOffset + 1), 16));
        }
        return data;
    }

    public static String getCommaDelimitedSimpleClassNames(Object[] objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            stringBuilder.append(objects[i].getClass().getSimpleName());
            if (i < objects.length - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public static String getUserAgent(Context context, String applicationName) throws PackageManager.NameNotFoundException {
        String versionName;
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "?";
        }
        return applicationName + MqttTopic.TOPIC_LEVEL_SEPARATOR + versionName + " (Linux;Android " + Build.VERSION.RELEASE + ") " + ExoPlayerLibraryInfo.VERSION_SLASHY;
    }

    public static int getPcmEncoding(int bitDepth) {
        switch (bitDepth) {
            case 8:
                return 3;
            case 16:
                return 2;
            case 24:
                return Integer.MIN_VALUE;
            case 32:
                return 1073741824;
            default:
                return 0;
        }
    }

    public static int getPcmFrameSize(int pcmEncoding, int channelCount) {
        switch (pcmEncoding) {
            case Integer.MIN_VALUE:
                return channelCount * 3;
            case 2:
                return channelCount * 2;
            case 3:
                return channelCount;
            case 1073741824:
                return channelCount * 4;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int inferContentType(Uri uri) {
        String path = uri.getPath();
        if (path == null) {
            return 3;
        }
        return inferContentType(path);
    }

    public static int inferContentType(String fileName) {
        String fileName2 = fileName.toLowerCase();
        if (fileName2.endsWith(".mpd")) {
            return 0;
        }
        if (fileName2.endsWith(".m3u8")) {
            return 2;
        }
        if (fileName2.endsWith(".ism") || fileName2.endsWith(".isml") || fileName2.endsWith(".ism/manifest") || fileName2.endsWith(".isml/manifest")) {
            return 1;
        }
        return 3;
    }

    public static String getStringForTime(StringBuilder builder, Formatter formatter, long timeMs) {
        if (timeMs == C.TIME_UNSET) {
            timeMs = 0;
        }
        long totalSeconds = (500 + timeMs) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        builder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)).toString() : formatter.format("%02d:%02d", Long.valueOf(minutes), Long.valueOf(seconds)).toString();
    }

    public static int getDefaultBufferSize(int trackType) {
        switch (trackType) {
            case 0:
                return 16777216;
            case 1:
                return C.DEFAULT_AUDIO_BUFFER_SIZE;
            case 2:
                return C.DEFAULT_VIDEO_BUFFER_SIZE;
            case 3:
            case 4:
                return 131072;
            default:
                throw new IllegalStateException();
        }
    }

    public static String escapeFileName(String fileName) {
        int length = fileName.length();
        int charactersToEscapeCount = 0;
        for (int i = 0; i < length; i++) {
            if (shouldEscapeCharacter(fileName.charAt(i))) {
                charactersToEscapeCount++;
            }
        }
        if (charactersToEscapeCount != 0) {
            StringBuilder builder = new StringBuilder((charactersToEscapeCount * 2) + length);
            int i2 = 0;
            while (charactersToEscapeCount > 0) {
                int i3 = i2 + 1;
                char c = fileName.charAt(i2);
                if (shouldEscapeCharacter(c)) {
                    builder.append('%').append(Integer.toHexString(c));
                    charactersToEscapeCount--;
                } else {
                    builder.append(c);
                }
                i2 = i3;
            }
            if (i2 < length) {
                builder.append((CharSequence) fileName, i2, length);
            }
            return builder.toString();
        }
        return fileName;
    }

    private static boolean shouldEscapeCharacter(char c) {
        switch (c) {
            case '\"':
            case MotionEventCompat.AXIS_GENERIC_6 /* 37 */:
            case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
            case MotionEventCompat.AXIS_GENERIC_16 /* 47 */:
            case ':':
            case '<':
            case JniUscClient.ay /* 62 */:
            case '?':
            case '\\':
            case '|':
                return true;
            default:
                return false;
        }
    }

    public static String unescapeFileName(String fileName) {
        int length = fileName.length();
        int percentCharacterCount = 0;
        for (int i = 0; i < length; i++) {
            if (fileName.charAt(i) == '%') {
                percentCharacterCount++;
            }
        }
        if (percentCharacterCount != 0) {
            int expectedLength = length - (percentCharacterCount * 2);
            StringBuilder builder = new StringBuilder(expectedLength);
            Matcher matcher = ESCAPED_CHARACTER_PATTERN.matcher(fileName);
            int endOfLastMatch = 0;
            while (percentCharacterCount > 0 && matcher.find()) {
                char unescapedCharacter = (char) Integer.parseInt(matcher.group(1), 16);
                builder.append((CharSequence) fileName, endOfLastMatch, matcher.start()).append(unescapedCharacter);
                endOfLastMatch = matcher.end();
                percentCharacterCount--;
            }
            if (endOfLastMatch < length) {
                builder.append((CharSequence) fileName, endOfLastMatch, length);
            }
            if (builder.length() != expectedLength) {
                return null;
            }
            return builder.toString();
        }
        return fileName;
    }

    public static void sneakyThrow(Throwable t) throws Throwable {
        sneakyThrowInternal(t);
    }

    private static <T extends Throwable> void sneakyThrowInternal(Throwable t) throws Throwable {
        throw t;
    }

    public static void recursiveDelete(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                recursiveDelete(child);
            }
        }
        fileOrDirectory.delete();
    }

    public static File createTempDirectory(Context context, String prefix) throws IOException {
        File tempFile = File.createTempFile(prefix, null, context.getCacheDir());
        tempFile.delete();
        tempFile.mkdir();
        return tempFile;
    }

    public static int crc(byte[] bytes, int start, int end, int initialValue) {
        for (int i = start; i < end; i++) {
            initialValue = (initialValue << 8) ^ CRC32_BYTES_MSBF[((initialValue >>> 24) ^ (bytes[i] & 255)) & 255];
        }
        return initialValue;
    }

    public static Point getPhysicalDisplaySize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        return getPhysicalDisplaySize(context, windowManager.getDefaultDisplay());
    }

    public static Point getPhysicalDisplaySize(Context context, Display display) throws NoSuchMethodException, ClassNotFoundException, SecurityException, NumberFormatException {
        if (SDK_INT < 25 && display.getDisplayId() == 0) {
            if ("Sony".equals(MANUFACTURER) && MODEL.startsWith("BRAVIA") && context.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd")) {
                return new Point(3840, 2160);
            }
            if ("NVIDIA".equals(MANUFACTURER) && MODEL.contains("SHIELD")) {
                String sysDisplaySize = null;
                try {
                    Class<?> systemProperties = Class.forName("android.os.SystemProperties");
                    Method getMethod = systemProperties.getMethod("get", String.class);
                    sysDisplaySize = (String) getMethod.invoke(systemProperties, "sys.display-size");
                } catch (Exception e) {
                    Log.e(TAG, "Failed to read sys.display-size", e);
                }
                if (!TextUtils.isEmpty(sysDisplaySize)) {
                    try {
                        String[] sysDisplaySizeParts = sysDisplaySize.trim().split("x");
                        if (sysDisplaySizeParts.length == 2) {
                            int width = Integer.parseInt(sysDisplaySizeParts[0]);
                            int height = Integer.parseInt(sysDisplaySizeParts[1]);
                            if (width > 0 && height > 0) {
                                return new Point(width, height);
                            }
                        }
                    } catch (NumberFormatException e2) {
                    }
                    Log.e(TAG, "Invalid sys.display-size: " + sysDisplaySize);
                }
            }
        }
        Point displaySize = new Point();
        if (SDK_INT >= 23) {
            getDisplaySizeV23(display, displaySize);
            return displaySize;
        }
        if (SDK_INT >= 17) {
            getDisplaySizeV17(display, displaySize);
            return displaySize;
        }
        if (SDK_INT >= 16) {
            getDisplaySizeV16(display, displaySize);
            return displaySize;
        }
        getDisplaySizeV9(display, displaySize);
        return displaySize;
    }

    @TargetApi(23)
    private static void getDisplaySizeV23(Display display, Point outSize) {
        Display.Mode mode = display.getMode();
        outSize.x = mode.getPhysicalWidth();
        outSize.y = mode.getPhysicalHeight();
    }

    @TargetApi(17)
    private static void getDisplaySizeV17(Display display, Point outSize) {
        display.getRealSize(outSize);
    }

    @TargetApi(16)
    private static void getDisplaySizeV16(Display display, Point outSize) {
        display.getSize(outSize);
    }

    private static void getDisplaySizeV9(Display display, Point outSize) {
        outSize.x = display.getWidth();
        outSize.y = display.getHeight();
    }
}
