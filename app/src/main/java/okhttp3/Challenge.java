package okhttp3;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class Challenge {
    private final String realm;
    private final String scheme;

    public Challenge(String scheme, String realm) {
        if (scheme == null) {
            throw new NullPointerException("scheme == null");
        }
        if (realm == null) {
            throw new NullPointerException("realm == null");
        }
        this.scheme = scheme;
        this.realm = realm;
    }

    public String scheme() {
        return this.scheme;
    }

    public String realm() {
        return this.realm;
    }

    public boolean equals(@Nullable Object other) {
        return (other instanceof Challenge) && ((Challenge) other).scheme.equals(this.scheme) && ((Challenge) other).realm.equals(this.realm);
    }

    public int hashCode() {
        int result = this.realm.hashCode() + 899;
        return (result * 31) + this.scheme.hashCode();
    }

    public String toString() {
        return this.scheme + " realm=\"" + this.realm + "\"";
    }
}
