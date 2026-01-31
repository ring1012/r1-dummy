package com.unisound.vui.common.location.strategy.baidu;

import android.content.Context;
import com.baidu.location.b;
import com.baidu.location.c;
import com.baidu.location.g;
import com.baidu.location.h;
import com.unisound.vui.common.location.strategy.LocationStrategy;

/* loaded from: classes.dex */
public class BaiduLocationStrategy extends LocationStrategy {
    private g baiduLocationClient;
    private b bdAbstractLocationListener = new b() { // from class: com.unisound.vui.common.location.strategy.baidu.BaiduLocationStrategy.1
        @Override // com.baidu.location.b
        public void onReceiveLocation(c bdLocation) {
            if (BaiduLocationStrategy.this.isLocateSuccess(bdLocation)) {
                BaiduLocationStrategy.this.onLocationSuccess(BaiduLocationInfoParser.parser(bdLocation));
            } else {
                BaiduLocationStrategy.this.onLocationFail(BaiduLocationStrategy.this.getLocationFailInfo(bdLocation));
            }
        }
    };

    public BaiduLocationStrategy(Context context) {
        this.baiduLocationClient = new g(context, getDefaultOptions());
        this.baiduLocationClient.a(this.bdAbstractLocationListener);
    }

    private h getDefaultOptions() {
        h hVar = new h();
        hVar.a(h.a.Battery_Saving);
        hVar.a(true);
        hVar.b(false);
        hVar.a(1000);
        return hVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getLocationFailInfo(c bdLocation) {
        return bdLocation == null ? "bdLocation is null !!" : bdLocation.i();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLocateSuccess(c bdLocation) {
        return bdLocation != null && bdLocation.h() == 161;
    }

    @Override // com.unisound.vui.common.location.strategy.LocationStrategy, com.unisound.vui.common.location.action.LocateInterface
    public void startLocation() {
        if (!this.isStartLocation) {
            this.baiduLocationClient.a();
        }
        super.startLocation();
    }

    @Override // com.unisound.vui.common.location.strategy.LocationStrategy, com.unisound.vui.common.location.action.LocateInterface
    public void stopLocation() {
        if (this.isStartLocation) {
            this.baiduLocationClient.b();
        }
        super.stopLocation();
    }
}
