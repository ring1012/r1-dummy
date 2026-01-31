package nluparser;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import nluparser.scheme.APPIntent;
import nluparser.scheme.AlarmIntent;
import nluparser.scheme.AudioIntent;
import nluparser.scheme.AudioResult;
import nluparser.scheme.BroadcastIntent;
import nluparser.scheme.BroadcastResult;
import nluparser.scheme.CallIntent;
import nluparser.scheme.ContactIntent;
import nluparser.scheme.CookBookIntent;
import nluparser.scheme.FlightIntent;
import nluparser.scheme.GlobalCmdIntent;
import nluparser.scheme.HealthInfoIntent;
import nluparser.scheme.Intent;
import nluparser.scheme.LocalSearchIntent;
import nluparser.scheme.MapIntent;
import nluparser.scheme.Mixture;
import nluparser.scheme.MusicIntent;
import nluparser.scheme.MusicResult;
import nluparser.scheme.NewsIntent;
import nluparser.scheme.NewsResult;
import nluparser.scheme.NoteIntent;
import nluparser.scheme.ReminderIntent;
import nluparser.scheme.Result;
import nluparser.scheme.SCode;
import nluparser.scheme.SMSIntent;
import nluparser.scheme.SName;
import nluparser.scheme.SceneModeIntent;
import nluparser.scheme.SettingExtIntent;
import nluparser.scheme.SettingIntent;
import nluparser.scheme.StockIntent;
import nluparser.scheme.StockResult;
import nluparser.scheme.TrafficControlIntent;
import nluparser.scheme.TrafficControlResult;
import nluparser.scheme.TrafficIntent;
import nluparser.scheme.TranslateIntent;
import nluparser.scheme.TranslateResult;
import nluparser.scheme.WeatherIntent;
import nluparser.scheme.WeatherResult;
import nluparser.scheme.WechatIntent;
import nluparser.scheme.WechatResult;
import nluparser.scheme.YPCallIntent;

/* loaded from: classes.dex */
interface c {

    /* renamed from: a, reason: collision with root package name */
    public static final Type f457a = new TypeToken<Mixture<MusicIntent, MusicResult>>() { // from class: nluparser.c.1
    }.getType();
    public static final Type b = new TypeToken<Mixture<AudioIntent, AudioResult>>() { // from class: nluparser.c.12
    }.getType();
    public static final Type c = new TypeToken<Mixture<AlarmIntent, Result.NullResult>>() { // from class: nluparser.c.23
    }.getType();
    public static final Type d = new TypeToken<Mixture<SettingIntent, Result.NullResult>>() { // from class: nluparser.c.28
    }.getType();
    public static final Type e = new TypeToken<Mixture<SettingExtIntent, Result.NullResult>>() { // from class: nluparser.c.29
    }.getType();
    public static final Type f = new TypeToken<Mixture<HealthInfoIntent, Result.NullResult>>() { // from class: nluparser.c.30
    }.getType();
    public static final Type g = new TypeToken<Mixture<ReminderIntent, Result.NullResult>>() { // from class: nluparser.c.31
    }.getType();
    public static final Type h = new TypeToken<Mixture<StockIntent, StockResult>>() { // from class: nluparser.c.32
    }.getType();
    public static final Type i = new TypeToken<Mixture<WeatherIntent, WeatherResult>>() { // from class: nluparser.c.33
    }.getType();
    public static final Type j = new TypeToken<Mixture<APPIntent, Result.NullResult>>() { // from class: nluparser.c.2
    }.getType();
    public static final Type k = new TypeToken<Mixture<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.c.3
    }.getType();
    public static final Type l = new TypeToken<Mixture<CallIntent, Result.NullResult>>() { // from class: nluparser.c.4
    }.getType();
    public static final Type m = new TypeToken<Mixture<ContactIntent, Result.NullResult>>() { // from class: nluparser.c.5
    }.getType();
    public static final Type n = new TypeToken<Mixture<SMSIntent, Result.NullResult>>() { // from class: nluparser.c.6
    }.getType();
    public static final Type o = new TypeToken<Mixture<FlightIntent, Result.NullResult>>() { // from class: nluparser.c.7
    }.getType();
    public static final Type p = new TypeToken<Mixture<LocalSearchIntent, Result.NullResult>>() { // from class: nluparser.c.8
    }.getType();
    public static final Type q = new TypeToken<Mixture<MapIntent, Result.NullResult>>() { // from class: nluparser.c.9
    }.getType();
    public static final Type r = new TypeToken<Mixture<TrafficIntent, Result.NullResult>>() { // from class: nluparser.c.10
    }.getType();
    public static final Type s = new TypeToken<Mixture<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.c.11
    }.getType();
    public static final Type t = new TypeToken<Mixture<TrafficControlIntent, TrafficControlResult>>() { // from class: nluparser.c.13
    }.getType();
    public static final Type u = new TypeToken<Mixture<BroadcastIntent, BroadcastResult>>() { // from class: nluparser.c.14
    }.getType();
    public static final Type v = new TypeToken<Mixture<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.c.15
    }.getType();
    public static final Type w = new TypeToken<Mixture<WechatIntent, WechatResult>>() { // from class: nluparser.c.16
    }.getType();
    public static final Type x = new TypeToken<Mixture<YPCallIntent, Result.NullResult>>() { // from class: nluparser.c.17
    }.getType();
    public static final Type y = new TypeToken<Mixture<TranslateIntent, TranslateResult>>() { // from class: nluparser.c.18
    }.getType();
    public static final Type z = new TypeToken<Mixture<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.c.19
    }.getType();
    public static final Type A = new TypeToken<Mixture<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.c.20
    }.getType();
    public static final Type B = new TypeToken<Mixture<SceneModeIntent, Result.NullResult>>() { // from class: nluparser.c.21
    }.getType();
    public static final Type C = new TypeToken<Mixture<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.c.22
    }.getType();
    public static final Type D = new TypeToken<Mixture<CookBookIntent, Result.NullResult>>() { // from class: nluparser.c.24
    }.getType();
    public static final Type E = new TypeToken<Mixture<GlobalCmdIntent, Result.NullResult>>() { // from class: nluparser.c.25
    }.getType();
    public static final Type F = new TypeToken<Mixture<NoteIntent, Result.NullResult>>() { // from class: nluparser.c.26
    }.getType();
    public static final Type G = new TypeToken<Mixture<NewsIntent, NewsResult>>() { // from class: nluparser.c.27
    }.getType();
    public static final Type H = Mixture.class;

    public static final class a implements c {
        public Map<String, Type> a() {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(SName.MUSIC, f457a);
            linkedHashMap.put(SName.AUDIO, b);
            linkedHashMap.put(SName.SETTING, e);
            linkedHashMap.put(SName.SETTING_COMMON, e);
            linkedHashMap.put(SName.SETTING_MP, e);
            linkedHashMap.put(SName.SETTING_AIR, e);
            linkedHashMap.put(SName.HEALTH_INFO, f);
            linkedHashMap.put(SName.ALARM, c);
            linkedHashMap.put(SName.REMINDER, g);
            linkedHashMap.put(SName.STOCK, h);
            linkedHashMap.put(SName.WEATHER, i);
            linkedHashMap.put(SName.APP, j);
            linkedHashMap.put(SName.CHAT, H);
            linkedHashMap.put(SCode.ANSWER, k);
            linkedHashMap.put(SName.CALL, l);
            linkedHashMap.put(SName.CONTACT, m);
            linkedHashMap.put(SName.SMS, n);
            linkedHashMap.put(SName.FLIGHT, o);
            linkedHashMap.put(SName.LOCALSEARCH, p);
            linkedHashMap.put(SName.MAP, q);
            linkedHashMap.put(SName.TRAFFIC, r);
            linkedHashMap.put(SName.WIFI_CONNECT, s);
            linkedHashMap.put(SName.BROADCAST, u);
            linkedHashMap.put(SName.TRAFFIC_CONTROL, t);
            linkedHashMap.put(SName.WAKEUP_WORD, v);
            linkedHashMap.put(SName.WECHAT, w);
            linkedHashMap.put(SName.YP_CALL, x);
            linkedHashMap.put(SName.TRANSLATION, y);
            linkedHashMap.put(SName.SETTING_MAP, z);
            linkedHashMap.put(SName.ERROR_REPORT, A);
            linkedHashMap.put(SName.SCENE_MODE, B);
            linkedHashMap.put(SName.CHAT_ILLEGAL, C);
            linkedHashMap.put(SName.COOKBOOK, D);
            linkedHashMap.put(SName.GLOBAL_CMD, E);
            linkedHashMap.put(SName.NOTE, F);
            linkedHashMap.put(SName.NEWS, G);
            return linkedHashMap;
        }
    }
}
