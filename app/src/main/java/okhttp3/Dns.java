package okhttp3;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public interface Dns {
    public static final Dns SYSTEM = new Dns() { // from class: okhttp3.Dns.1
        @Override // okhttp3.Dns
        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
            if (hostname == null) {
                throw new UnknownHostException("hostname == null");
            }
            return Arrays.asList(InetAddress.getAllByName(hostname));
        }
    };

    List<InetAddress> lookup(String str) throws UnknownHostException;
}
