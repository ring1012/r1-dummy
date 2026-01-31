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
import nluparser.scheme.HealthInfoIntent;
import nluparser.scheme.Intent;
import nluparser.scheme.LocalSearchIntent;
import nluparser.scheme.MapIntent;
import nluparser.scheme.MusicIntent;
import nluparser.scheme.MusicResult;
import nluparser.scheme.NLU;
import nluparser.scheme.NewsIntent;
import nluparser.scheme.NewsResult;
import nluparser.scheme.NoteIntent;
import nluparser.scheme.ReminderIntent;
import nluparser.scheme.Result;
import nluparser.scheme.SCode;
import nluparser.scheme.SMSIntent;
import nluparser.scheme.SName;
import nluparser.scheme.SettingExtIntent;
import nluparser.scheme.SettingIntent;
import nluparser.scheme.StockIntent;
import nluparser.scheme.StockResult;
import nluparser.scheme.TrafficControlIntent;
import nluparser.scheme.TrafficControlResult;
import nluparser.scheme.TrafficIntent;
import nluparser.scheme.WeatherIntent;
import nluparser.scheme.WeatherResult;
import nluparser.scheme.YPCallIntent;

/* loaded from: classes.dex */
interface d {
    public static final Type b = new TypeToken<NLU<MusicIntent, MusicResult>>() { // from class: nluparser.d.1
    }.getType();
    public static final Type c = new TypeToken<NLU<AudioIntent, AudioResult>>() { // from class: nluparser.d.12
    }.getType();
    public static final Type d = new TypeToken<NLU<AlarmIntent, Result.NullResult>>() { // from class: nluparser.d.22
    }.getType();
    public static final Type e = new TypeToken<NLU<SettingIntent, Result.NullResult>>() { // from class: nluparser.d.23
    }.getType();
    public static final Type f = new TypeToken<NLU<SettingExtIntent, Result.NullResult>>() { // from class: nluparser.d.24
    }.getType();
    public static final Type g = new TypeToken<NLU<HealthInfoIntent, Result.NullResult>>() { // from class: nluparser.d.25
    }.getType();
    public static final Type h = new TypeToken<NLU<ReminderIntent, Result.NullResult>>() { // from class: nluparser.d.26
    }.getType();
    public static final Type i = new TypeToken<NLU<StockIntent, StockResult>>() { // from class: nluparser.d.27
    }.getType();
    public static final Type j = new TypeToken<NLU<WeatherIntent, WeatherResult>>() { // from class: nluparser.d.28
    }.getType();
    public static final Type k = new TypeToken<NLU<APPIntent, Result.NullResult>>() { // from class: nluparser.d.2
    }.getType();
    public static final Type l = new TypeToken<NLU<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.d.3
    }.getType();
    public static final Type m = new TypeToken<NLU<CallIntent, Result.NullResult>>() { // from class: nluparser.d.4
    }.getType();
    public static final Type n = new TypeToken<NLU<ContactIntent, Result.NullResult>>() { // from class: nluparser.d.5
    }.getType();
    public static final Type o = new TypeToken<NLU<SMSIntent, Result.NullResult>>() { // from class: nluparser.d.6
    }.getType();
    public static final Type p = new TypeToken<NLU<FlightIntent, Result.NullResult>>() { // from class: nluparser.d.7
    }.getType();
    public static final Type q = new TypeToken<NLU<LocalSearchIntent, Result.NullResult>>() { // from class: nluparser.d.8
    }.getType();
    public static final Type r = new TypeToken<NLU<MapIntent, Result.NullResult>>() { // from class: nluparser.d.9
    }.getType();
    public static final Type s = new TypeToken<NLU<TrafficIntent, Result.NullResult>>() { // from class: nluparser.d.10
    }.getType();
    public static final Type t = new TypeToken<NLU<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.d.11
    }.getType();
    public static final Type u = new TypeToken<NLU<TrafficControlIntent, TrafficControlResult>>() { // from class: nluparser.d.13
    }.getType();
    public static final Type v = new TypeToken<NLU<BroadcastIntent, BroadcastResult>>() { // from class: nluparser.d.14
    }.getType();
    public static final Type w = new TypeToken<NLU<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.d.15
    }.getType();
    public static final Type x = new TypeToken<NLU<YPCallIntent, Result.NullResult>>() { // from class: nluparser.d.16
    }.getType();
    public static final Type y = new TypeToken<NLU<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.d.17
    }.getType();
    public static final Type z = new TypeToken<NLU<CookBookIntent, Result.NullResult>>() { // from class: nluparser.d.18
    }.getType();
    public static final Type A = new TypeToken<NLU<Intent.NullIntent, Result.NullResult>>() { // from class: nluparser.d.19
    }.getType();
    public static final Type B = new TypeToken<NLU<NoteIntent, Result.NullResult>>() { // from class: nluparser.d.20
    }.getType();
    public static final Type C = new TypeToken<NLU<NewsIntent, NewsResult>>() { // from class: nluparser.d.21
    }.getType();
    public static final Type D = NLU.class;

    public static final class a implements d {
        @Override // nluparser.d
        public Map<String, Type> a() {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(SName.MUSIC, b);
            linkedHashMap.put(SName.AUDIO, c);
            linkedHashMap.put(SName.SETTING, f);
            linkedHashMap.put(SName.SETTING_COMMON, f);
            linkedHashMap.put(SName.SETTING_MP, f);
            linkedHashMap.put(SName.SETTING_AIR, f);
            linkedHashMap.put(SName.HEALTH_INFO, g);
            linkedHashMap.put(SName.ALARM, d);
            linkedHashMap.put(SName.REMINDER, h);
            linkedHashMap.put(SName.NOTE, B);
            linkedHashMap.put(SName.STOCK, i);
            linkedHashMap.put(SName.WEATHER, j);
            linkedHashMap.put(SName.APP, k);
            linkedHashMap.put(SName.CHAT, D);
            linkedHashMap.put(SCode.ANSWER, l);
            linkedHashMap.put(SName.CALL, m);
            linkedHashMap.put(SName.CONTACT, n);
            linkedHashMap.put(SName.SMS, o);
            linkedHashMap.put(SName.FLIGHT, p);
            linkedHashMap.put(SName.LOCALSEARCH, q);
            linkedHashMap.put(SName.MAP, r);
            linkedHashMap.put(SName.TRAFFIC, s);
            linkedHashMap.put(SName.WIFI_CONNECT, t);
            linkedHashMap.put(SName.BROADCAST, v);
            linkedHashMap.put(SName.TRAFFIC_CONTROL, u);
            linkedHashMap.put(SName.WAKEUP_WORD, w);
            linkedHashMap.put(SName.YP_CALL, x);
            linkedHashMap.put(SName.SETTING_MAP, y);
            linkedHashMap.put(SName.COOKBOOK, z);
            linkedHashMap.put(SName.ERROR_REPORT, A);
            linkedHashMap.put(SName.NEWS, C);
            return linkedHashMap;
        }
    }

    Map<String, Type> a();
}
