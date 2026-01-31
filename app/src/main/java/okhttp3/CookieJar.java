package okhttp3;

import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public interface CookieJar {
    public static final CookieJar NO_COOKIES = new CookieJar() { // from class: okhttp3.CookieJar.1
        @Override // okhttp3.CookieJar
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        }

        @Override // okhttp3.CookieJar
        public List<Cookie> loadForRequest(HttpUrl url) {
            return Collections.emptyList();
        }
    };

    List<Cookie> loadForRequest(HttpUrl httpUrl);

    void saveFromResponse(HttpUrl httpUrl, List<Cookie> list);
}
