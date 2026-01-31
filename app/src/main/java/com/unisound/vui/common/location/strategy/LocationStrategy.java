package com.unisound.vui.common.location.strategy;

import com.unisound.vui.common.location.action.LocateInterface;
import com.unisound.vui.common.location.bean.LocationInfo;
import com.unisound.vui.common.location.listener.LocationListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public abstract class LocationStrategy implements LocateInterface, LocationListener {
    private static final int MAX_ERROR_LOCATION_TIMES = 10;
    private int currentErrorTimes = 0;
    protected boolean isStartLocation = false;
    private List<LocationListener> mLocationListenerList = new ArrayList();

    protected LocationStrategy() {
    }

    private void checkMaxErrorTimes(String locationErrorMessage) {
        if (this.currentErrorTimes > 10) {
            stopLocation();
            notifyLocationListenerListFail(this.mLocationListenerList, locationErrorMessage);
            this.currentErrorTimes = 0;
        }
    }

    private void notifyLocationListenerListFail(List<LocationListener> mLocationListenerList, String locationFailInfo) {
        Iterator<LocationListener> it = mLocationListenerList.iterator();
        while (it.hasNext()) {
            it.next().onLocationFail(locationFailInfo);
        }
    }

    private void notifyLocationListenerListSuccess(List<LocationListener> mLocationListenerList, LocationInfo locationInfo) {
        Iterator<LocationListener> it = mLocationListenerList.iterator();
        while (it.hasNext()) {
            it.next().onLocationSuccess(locationInfo);
        }
    }

    @Override // com.unisound.vui.common.location.action.LocateInterface
    public void onDestory() {
        stopLocation();
        unRegisterAllLocationListener();
    }

    @Override // com.unisound.vui.common.location.listener.LocationListener
    public void onLocationFail(String locationErrorMessage) {
        this.currentErrorTimes++;
        checkMaxErrorTimes(locationErrorMessage);
    }

    @Override // com.unisound.vui.common.location.listener.LocationListener
    public void onLocationSuccess(LocationInfo locationInfo) {
        this.currentErrorTimes = 0;
        stopLocation();
        notifyLocationListenerListSuccess(this.mLocationListenerList, locationInfo);
    }

    @Override // com.unisound.vui.common.location.action.LocateInterface
    public void registerLocationLinstener(LocationListener locationListener) {
        if (this.mLocationListenerList.contains(locationListener)) {
            return;
        }
        this.mLocationListenerList.add(locationListener);
    }

    @Override // com.unisound.vui.common.location.action.LocateInterface
    public void startLocation() {
        this.isStartLocation = true;
    }

    @Override // com.unisound.vui.common.location.action.LocateInterface
    public void stopLocation() {
        this.isStartLocation = false;
    }

    @Override // com.unisound.vui.common.location.action.LocateInterface
    public void unRegisterAllLocationListener() {
        this.mLocationListenerList.clear();
    }

    @Override // com.unisound.vui.common.location.action.LocateInterface
    public void unRegisterLocationListener(LocationListener locationListener) {
        if (this.mLocationListenerList.contains(locationListener)) {
            this.mLocationListenerList.remove(locationListener);
        }
    }
}
