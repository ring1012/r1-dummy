package com.unisound.ant.device.mqtt;

import cn.yunzhisheng.asr.JniUscClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/* loaded from: classes.dex */
public class HttpRequestUtil {
    public static String sendGet(String url) {
        return sendGet(url, 30000, null);
    }

    public static String sendGet(String url, int timeout) {
        return sendGet(url, timeout, null);
    }

    public static String sendGet(String url, Map<String, String> httpHeaders) {
        return sendGet(url, 30000, httpHeaders);
    }

    public static String sendGet(String url, int timeout, Map<String, String> httpHeaders) throws Throwable {
        if (url == null || url.trim().length() == 0) {
            return null;
        }
        BufferedReader in = null;
        HttpURLConnection conn = null;
        StringBuilder result = new StringBuilder();
        try {
            try {
                URL realUrl = new URL(url);
                conn = (HttpURLConnection) realUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", JniUscClient.s);
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout);
                if (httpHeaders != null && httpHeaders.size() > 0) {
                    for (String key : httpHeaders.keySet()) {
                        String value = httpHeaders.get(key);
                        if (key != null && key.trim().length() != 0 && value != null && value.trim().length() != 0) {
                            conn.addRequestProperty(key, value);
                        }
                    }
                }
                conn.connect();
                BufferedReader in2 = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                while (true) {
                    try {
                        String line = in2.readLine();
                        if (line == null) {
                            break;
                        }
                        result.append(line);
                    } catch (Exception e) {
                        e = e;
                        in = in2;
                        e.printStackTrace();
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                        return result.toString();
                    } catch (Throwable th) {
                        th = th;
                        in = in2;
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e22) {
                                e22.printStackTrace();
                                throw th;
                            }
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                        throw th;
                    }
                }
                if (in2 != null) {
                    try {
                        in2.close();
                    } catch (Exception e23) {
                        e23.printStackTrace();
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e3) {
            e = e3;
        }
        return result.toString();
    }

    public static String sendPost(String url, String param) throws Exception {
        return sendPost(url, param, 30000, null);
    }

    public static String sendPost(String url, String param, int timeout) throws Exception {
        return sendPost(url, param, timeout, null);
    }

    public static String sendPost(String url, String param, Map<String, String> httpHeaders) throws Exception {
        return sendPost(url, param, 30000, httpHeaders);
    }

    public static String sendPost(String url, String param, int timeout, Map<String, String> httpHeaders) throws Exception {
        if (url == null || url.trim().length() == 0) {
            return null;
        }
        PrintWriter out = null;
        BufferedReader in = null;
        HttpURLConnection conn = null;
        StringBuilder result = new StringBuilder();
        try {
            try {
                URL realUrl = new URL(url);
                conn = (HttpURLConnection) realUrl.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", JniUscClient.s);
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout);
                if (httpHeaders != null && httpHeaders.size() > 0) {
                    for (String key : httpHeaders.keySet()) {
                        String value = httpHeaders.get(key);
                        if (key != null && key.trim().length() != 0 && value != null && value.trim().length() != 0) {
                            conn.addRequestProperty(key, value);
                        }
                    }
                }
                conn.setDoOutput(true);
                conn.setDoInput(true);
                PrintWriter out2 = new PrintWriter(conn.getOutputStream());
                try {
                    out2.print(param);
                    out2.flush();
                    BufferedReader in2 = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    while (true) {
                        try {
                            String line = in2.readLine();
                            if (line == null) {
                                break;
                            }
                            result.append(line);
                        } catch (Exception e) {
                            e = e;
                            throw new Exception("发送POST请求过程中出错：" + e);
                        } catch (Throwable th) {
                            th = th;
                            in = in2;
                            out = out2;
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                    throw th;
                                }
                            }
                            if (in != null) {
                                in.close();
                            }
                            if (conn != null) {
                                conn.disconnect();
                            }
                            throw th;
                        }
                    }
                    if (out2 != null) {
                        try {
                            out2.close();
                        } catch (IOException ex2) {
                            ex2.printStackTrace();
                        }
                    }
                    if (in2 != null) {
                        in2.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                    return result.toString();
                } catch (Exception e2) {
                    e = e2;
                } catch (Throwable th2) {
                    th = th2;
                    out = out2;
                }
            } catch (Exception e3) {
                e = e3;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }
}
