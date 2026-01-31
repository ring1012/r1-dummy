package nlucreator;

import java.util.ArrayList;
import nluparser.scheme.Intent;
import nluparser.scheme.NLU;
import nluparser.scheme.Result;
import nluparser.scheme.SName;
import nluparser.scheme.Semantic;
import nluparser.scheme.SettingCommonIntent;
import nluparser.scheme.SettingExtIntent;
import nluparser.scheme.SettingIntent;

/* loaded from: classes.dex */
public class SettingNluCreator {
    private static final int CMD_DECREASE = 2;
    private static final int CMD_INCREASE = 1;

    private static NLU<Intent, Result> createNlu(int type) {
        NLU<Intent, Result> nlu = new NLU<>();
        nlu.setService(SName.SETTING_COMMON);
        SettingExtIntent settingExtIntent = new SettingExtIntent();
        ArrayList arrayList = new ArrayList();
        SettingIntent settingIntent = new SettingIntent();
        settingIntent.setOperands(SettingCommonIntent.Operands_Common.ATTR_VOLUME);
        switch (type) {
            case 1:
                nlu.setText("增大音量");
                settingIntent.setOperator("ACT_INCREASE");
                break;
            case 2:
                nlu.setText("减小音量");
                settingIntent.setOperator("ACT_DECREASE");
                break;
        }
        arrayList.add(settingIntent);
        settingExtIntent.setOperations(arrayList);
        Semantic<I> semantic = new Semantic<>();
        semantic.setIntent(settingExtIntent);
        nlu.setSemantic(semantic);
        return nlu;
    }

    public static NLU<Intent, Result> createVolumeDecreaseNlu() {
        return createNlu(2);
    }

    public static NLU<Intent, Result> createVolumeIncreaseNlu() {
        return createNlu(1);
    }
}
