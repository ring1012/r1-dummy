package a.a.a;

/* loaded from: classes.dex */
class b {
    private static String a(String str) {
        char cCharAt;
        String lowerCase = str.toLowerCase();
        if (!lowerCase.matches("[a-z]*[1-5]?")) {
            return lowerCase;
        }
        if (!lowerCase.matches("[a-z]*[1-5]")) {
            return lowerCase.replaceAll("v", "ü");
        }
        int numericValue = Character.getNumericValue(lowerCase.charAt(lowerCase.length() - 1));
        int iIndexOf = lowerCase.indexOf(97);
        int iIndexOf2 = lowerCase.indexOf(101);
        int iIndexOf3 = lowerCase.indexOf("ou");
        if (-1 == iIndexOf) {
            if (-1 == iIndexOf2) {
                if (-1 == iIndexOf3) {
                    iIndexOf = lowerCase.length() - 1;
                    while (true) {
                        if (iIndexOf < 0) {
                            iIndexOf = -1;
                            cCharAt = '$';
                            break;
                        }
                        if (String.valueOf(lowerCase.charAt(iIndexOf)).matches("[aeiouv]")) {
                            cCharAt = lowerCase.charAt(iIndexOf);
                            break;
                        }
                        iIndexOf--;
                    }
                } else {
                    cCharAt = "ou".charAt(0);
                    iIndexOf = iIndexOf3;
                }
            } else {
                iIndexOf = iIndexOf2;
                cCharAt = 'e';
            }
        } else {
            cCharAt = 'a';
        }
        if ('$' == cCharAt || -1 == iIndexOf) {
            return lowerCase;
        }
        char cCharAt2 = "āáăàaēéĕèeīíĭìiōóŏòoūúŭùuǖǘǚǜü".charAt(("aeiouv".indexOf(cCharAt) * 5) + (numericValue - 1));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(lowerCase.substring(0, iIndexOf).replaceAll("v", "ü"));
        stringBuffer.append(cCharAt2);
        stringBuffer.append(lowerCase.substring(iIndexOf + 1, lowerCase.length() - 1).replaceAll("v", "ü"));
        return stringBuffer.toString();
    }

    static String a(String str, a.a.a.a.b bVar) throws a.a.a.a.a.a {
        if (a.a.a.a.c.c == bVar.c() && (a.a.a.a.d.b == bVar.d() || a.a.a.a.d.f5a == bVar.d())) {
            throw new a.a.a.a.a.a("tone marks cannot be added to v or u:");
        }
        if (a.a.a.a.c.b == bVar.c()) {
            str = str.replaceAll("[1-5]", "");
        } else if (a.a.a.a.c.c == bVar.c()) {
            str = a(str.replaceAll("u:", "v"));
        }
        if (a.a.a.a.d.b == bVar.d()) {
            str = str.replaceAll("u:", "v");
        } else if (a.a.a.a.d.c == bVar.d()) {
            str = str.replaceAll("u:", "ü");
        }
        return a.a.a.a.a.f1a == bVar.b() ? str.toUpperCase() : str;
    }
}
