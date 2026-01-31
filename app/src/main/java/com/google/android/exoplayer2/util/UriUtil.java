package com.google.android.exoplayer2.util;

import android.net.Uri;
import android.text.TextUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public final class UriUtil {
    private static final int FRAGMENT = 3;
    private static final int INDEX_COUNT = 4;
    private static final int PATH = 1;
    private static final int QUERY = 2;
    private static final int SCHEME_COLON = 0;

    private UriUtil() {
    }

    public static Uri resolveToUri(String baseUri, String referenceUri) {
        return Uri.parse(resolve(baseUri, referenceUri));
    }

    public static String resolve(String baseUri, String referenceUri) {
        StringBuilder uri = new StringBuilder();
        if (baseUri == null) {
            baseUri = "";
        }
        if (referenceUri == null) {
            referenceUri = "";
        }
        int[] refIndices = getUriIndices(referenceUri);
        if (refIndices[0] != -1) {
            uri.append(referenceUri);
            removeDotSegments(uri, refIndices[1], refIndices[2]);
            return uri.toString();
        }
        int[] baseIndices = getUriIndices(baseUri);
        if (refIndices[3] == 0) {
            return uri.append((CharSequence) baseUri, 0, baseIndices[3]).append(referenceUri).toString();
        }
        if (refIndices[2] == 0) {
            return uri.append((CharSequence) baseUri, 0, baseIndices[2]).append(referenceUri).toString();
        }
        if (refIndices[1] != 0) {
            int baseLimit = baseIndices[0] + 1;
            uri.append((CharSequence) baseUri, 0, baseLimit).append(referenceUri);
            return removeDotSegments(uri, refIndices[1] + baseLimit, refIndices[2] + baseLimit);
        }
        if (referenceUri.charAt(refIndices[1]) == '/') {
            uri.append((CharSequence) baseUri, 0, baseIndices[1]).append(referenceUri);
            return removeDotSegments(uri, baseIndices[1], baseIndices[1] + refIndices[2]);
        }
        if (baseIndices[0] + 2 < baseIndices[1] && baseIndices[1] == baseIndices[2]) {
            uri.append((CharSequence) baseUri, 0, baseIndices[1]).append(IOUtils.DIR_SEPARATOR_UNIX).append(referenceUri);
            return removeDotSegments(uri, baseIndices[1], baseIndices[1] + refIndices[2] + 1);
        }
        int lastSlashIndex = baseUri.lastIndexOf(47, baseIndices[2] - 1);
        int baseLimit2 = lastSlashIndex == -1 ? baseIndices[1] : lastSlashIndex + 1;
        uri.append((CharSequence) baseUri, 0, baseLimit2).append(referenceUri);
        return removeDotSegments(uri, baseIndices[1], refIndices[2] + baseLimit2);
    }

    private static String removeDotSegments(StringBuilder uri, int offset, int limit) {
        int nextSegmentStart;
        if (offset >= limit) {
            return uri.toString();
        }
        if (uri.charAt(offset) == '/') {
            offset++;
        }
        int segmentStart = offset;
        int i = offset;
        while (i <= limit) {
            if (i == limit) {
                nextSegmentStart = i;
            } else if (uri.charAt(i) == '/') {
                nextSegmentStart = i + 1;
            } else {
                i++;
            }
            if (i == segmentStart + 1 && uri.charAt(segmentStart) == '.') {
                uri.delete(segmentStart, nextSegmentStart);
                limit -= nextSegmentStart - segmentStart;
                i = segmentStart;
            } else if (i == segmentStart + 2 && uri.charAt(segmentStart) == '.' && uri.charAt(segmentStart + 1) == '.') {
                int prevSegmentStart = uri.lastIndexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR, segmentStart - 2) + 1;
                int removeFrom = prevSegmentStart > offset ? prevSegmentStart : offset;
                uri.delete(removeFrom, nextSegmentStart);
                limit -= nextSegmentStart - removeFrom;
                segmentStart = prevSegmentStart;
                i = prevSegmentStart;
            } else {
                i++;
                segmentStart = i;
            }
        }
        return uri.toString();
    }

    private static int[] getUriIndices(String uriString) {
        int pathIndex;
        int[] indices = new int[4];
        if (TextUtils.isEmpty(uriString)) {
            indices[0] = -1;
        } else {
            int length = uriString.length();
            int fragmentIndex = uriString.indexOf(35);
            if (fragmentIndex == -1) {
                fragmentIndex = length;
            }
            int queryIndex = uriString.indexOf(63);
            if (queryIndex == -1 || queryIndex > fragmentIndex) {
                queryIndex = fragmentIndex;
            }
            int schemeIndexLimit = uriString.indexOf(47);
            if (schemeIndexLimit == -1 || schemeIndexLimit > queryIndex) {
                schemeIndexLimit = queryIndex;
            }
            int schemeIndex = uriString.indexOf(58);
            if (schemeIndex > schemeIndexLimit) {
                schemeIndex = -1;
            }
            boolean hasAuthority = schemeIndex + 2 < queryIndex && uriString.charAt(schemeIndex + 1) == '/' && uriString.charAt(schemeIndex + 2) == '/';
            if (hasAuthority) {
                pathIndex = uriString.indexOf(47, schemeIndex + 3);
                if (pathIndex == -1 || pathIndex > queryIndex) {
                    pathIndex = queryIndex;
                }
            } else {
                pathIndex = schemeIndex + 1;
            }
            indices[0] = schemeIndex;
            indices[1] = pathIndex;
            indices[2] = queryIndex;
            indices[3] = fragmentIndex;
        }
        return indices;
    }
}
