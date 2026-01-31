package com.unisound.vui.common.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.unisound.b.f;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: classes.dex */
public class NetUtil {
    private static final String TAG = "NetUtil";
    private static NetUtil mNetUtil;
    private ConnectivityManager mConnectivityManager;
    private Context mContext;
    private NetworkInfo mNetworkInfo;

    private NetUtil(Context context) {
        this.mContext = context.getApplicationContext();
        this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
    }

    public static NetUtil getInstante(Context context) {
        if (mNetUtil == null) {
            mNetUtil = new NetUtil(context);
        }
        return mNetUtil;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getLocalIpAddress() throws java.net.SocketException {
        /*
            r1 = 0
            java.util.Enumeration r2 = java.net.NetworkInterface.getNetworkInterfaces()     // Catch: java.net.SocketException -> L63
        L5:
            boolean r0 = r2.hasMoreElements()     // Catch: java.net.SocketException -> L87
            if (r0 == 0) goto L49
            java.lang.Object r0 = r2.nextElement()     // Catch: java.net.SocketException -> L87
            java.net.NetworkInterface r0 = (java.net.NetworkInterface) r0     // Catch: java.net.SocketException -> L87
            java.util.Enumeration r3 = r0.getInetAddresses()     // Catch: java.net.SocketException -> L87
        L15:
            boolean r0 = r3.hasMoreElements()     // Catch: java.net.SocketException -> L87
            if (r0 == 0) goto L5
            java.lang.Object r0 = r3.nextElement()     // Catch: java.net.SocketException -> L87
            java.net.InetAddress r0 = (java.net.InetAddress) r0     // Catch: java.net.SocketException -> L87
            boolean r4 = r0.isLoopbackAddress()     // Catch: java.net.SocketException -> L87
            if (r4 != 0) goto L46
            java.lang.String r0 = r0.getHostAddress()     // Catch: java.net.SocketException -> L87
            java.lang.String r0 = r0.toString()     // Catch: java.net.SocketException -> L87
            boolean r4 = org.apache.http.conn.util.InetAddressUtils.isIPv4Address(r0)     // Catch: java.net.SocketException -> L87
            if (r4 == 0) goto L46
            java.lang.String r4 = "::1"
            boolean r4 = r0.equals(r4)     // Catch: java.net.SocketException -> L87
            if (r4 != 0) goto L46
            java.lang.String r4 = "192"
            boolean r1 = r0.startsWith(r4)     // Catch: java.net.SocketException -> L87
            if (r1 == 0) goto L47
        L45:
            return r0
        L46:
            r0 = r1
        L47:
            r1 = r0
            goto L15
        L49:
            r0 = r1
        L4a:
            java.lang.String r1 = "NetUtil"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "===>>get current ipAddress:"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r0)
            java.lang.String r2 = r2.toString()
            com.unisound.vui.util.LogMgr.d(r1, r2)
            goto L45
        L63:
            r0 = move-exception
            r5 = r0
            r0 = r1
            r1 = r5
        L67:
            r1.printStackTrace()
            java.lang.String r2 = "NetUtil"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "===>>get current ipAddress:"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r1 = r1.toString()
            com.unisound.vui.util.LogMgr.e(r2, r1)
            goto L4a
        L87:
            r0 = move-exception
            r5 = r0
            r0 = r1
            r1 = r5
            goto L67
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.vui.common.network.NetUtil.getLocalIpAddress():java.lang.String");
    }

    public static String getNetWorkType(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            String typeName = activeNetworkInfo.getTypeName();
            if (typeName.equalsIgnoreCase("WIFI") || typeName.equalsIgnoreCase("MOBILE")) {
                return typeName;
            }
        }
        return "UNKNOW";
    }

    public static String getOutNetIp() throws IOException {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://www.cmyip.com/").openConnection();
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, f.b));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        inputStream.close();
                        return sb.toString();
                    }
                    sb.append(line + "\n");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return "";
    }

    public static boolean isMobileConnected(Context context) {
        NetworkInfo networkInfo;
        if (context == null || (networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(0)) == null) {
            return false;
        }
        return networkInfo.isAvailable();
    }

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetworkInfo;
        if (context == null || (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null) {
            return false;
        }
        return activeNetworkInfo.isConnected();
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo networkInfo;
        if (context == null || (networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1)) == null) {
            return false;
        }
        return networkInfo.isAvailable();
    }

    public boolean getConnectState() {
        if (this.mConnectivityManager != null) {
            this.mNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
            if (this.mNetworkInfo != null) {
                return this.mNetworkInfo.isConnected();
            }
        }
        return false;
    }

    public void release() {
        this.mContext = null;
        this.mConnectivityManager = null;
        this.mNetworkInfo = null;
    }
}
