package logreport;

import nluparser.scheme.NLU;

/* loaded from: classes.dex */
public class FullLog {
    private NLU nlu;
    private String ttsData;

    public FullLog(NLU nlu, String ttsData) {
        this.nlu = nlu;
        this.ttsData = ttsData;
    }

    public NLU getNlu() {
        return this.nlu;
    }

    public String getTtsData() {
        return this.ttsData;
    }

    public void setNlu(NLU nlu) {
        this.nlu = nlu;
    }

    public void setTtsData(String ttsData) {
        this.ttsData = ttsData;
    }
}
