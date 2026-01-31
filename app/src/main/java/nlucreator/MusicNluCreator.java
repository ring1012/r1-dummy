package nlucreator;

import java.util.ArrayList;
import nluparser.scheme.Intent;
import nluparser.scheme.NLU;
import nluparser.scheme.Operator;
import nluparser.scheme.Result;
import nluparser.scheme.SName;
import nluparser.scheme.Semantic;
import nluparser.scheme.SettingExtIntent;
import nluparser.scheme.SettingIntent;

/* loaded from: classes.dex */
public class MusicNluCreator {
    private static final int CMD_CLOSE = 5;
    private static final int CMD_NEXT = 4;
    private static final int CMD_PAUSE = 2;
    private static final int CMD_PLAY = 1;
    private static final int CMD_PREV = 3;
    private static final int CMD_STOP = 6;

    public static NLU<Intent, Result> createMusicCloseNlu() {
        return createNlu(5);
    }

    public static NLU<Intent, Result> createMusicNextNlu() {
        return createNlu(4);
    }

    public static NLU<Intent, Result> createMusicPauseNlu() {
        return createNlu(2);
    }

    public static NLU<Intent, Result> createMusicPlayNlu() {
        return createNlu(1);
    }

    public static NLU<Intent, Result> createMusicPrevNlu() {
        return createNlu(3);
    }

    public static NLU<Intent, Result> createMusicStopNlu() {
        return createNlu(6);
    }

    private static NLU<Intent, Result> createNlu(int type) {
        NLU<Intent, Result> nlu = new NLU<>();
        nlu.setService(SName.SETTING_MP);
        SettingExtIntent settingExtIntent = new SettingExtIntent();
        ArrayList arrayList = new ArrayList();
        SettingIntent settingIntent = new SettingIntent();
        switch (type) {
            case 1:
                nlu.setText("播放");
                settingIntent.setOperator("ACT_PLAY");
                break;
            case 2:
                nlu.setText("暂停");
                settingIntent.setOperator(Operator.ACT_PAUSE);
                break;
            case 3:
                nlu.setText("上一首");
                settingIntent.setOperator(Operator.ACT_PREV);
                break;
            case 4:
                nlu.setText("下一首");
                settingIntent.setOperator(Operator.ACT_NEXT);
                break;
            case 5:
                nlu.setText("退出播放");
                settingIntent.setOperator("ACT_CLOSE");
                break;
            case 6:
                nlu.setText("停止");
                settingIntent.setOperator(Operator.ACT_STOP);
                break;
        }
        arrayList.add(settingIntent);
        settingExtIntent.setOperations(arrayList);
        Semantic<I> semantic = new Semantic<>();
        semantic.setIntent(settingExtIntent);
        nlu.setSemantic(semantic);
        return nlu;
    }
}
