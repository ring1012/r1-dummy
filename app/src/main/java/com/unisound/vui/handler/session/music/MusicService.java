package com.unisound.vui.handler.session.music;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.unisound.ant.device.DeviceCenterHandler;
import com.unisound.ant.device.listener.MusicStatusListener;
import com.unisound.vui.handler.session.music.PlayController;
import com.unisound.vui.handler.session.music.kuwo.MusicPlayMode;
import com.unisound.vui.handler.session.music.listener.MusicListenerWapper;
import com.unisound.vui.handler.session.music.player.MusicPlayer;
import com.unisound.vui.handler.session.music.playitem.PlayItem;
import com.unisound.vui.util.LogMgr;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class MusicService extends Service implements MusicPlayer.Callback, MusicPlayer.Renderer {
    private static final int DEFAULTTIME = 10000;
    static final byte MUSIC_LIST_LOOP = 1;
    public static final String MUSIC_LIST_LOOP_NAME = "listLoop";
    static final byte MUSIC_LIST_ORDER = 8;
    public static final String MUSIC_LIST_ORDER_NAME = "listOrder";
    static final byte MUSIC_LIST_SHUFFLED = 4;
    public static final String MUSIC_LIST_SHUFFLED_NAME = "listShuffled";
    static final byte MUSIC_SINGLE_LOOP = 2;
    public static final String MUSIC_SINGLE_LOOP_NAME = "singleLoop";
    private static final String TAG = "MusicService";
    private static Map<Byte, String> playModes = new HashMap();
    private PlayItem.ItemType currentItemType;
    private int currentPosition;
    private byte flags = 1;
    private MusicStatusListener mMusicStatusListener;
    private Iterator<MusicListenerWapper> musicListenerIterator;
    private CopyOnWriteArrayList<MusicListenerWapper> musicListeners;
    private CommonPlayer musicPlayer;
    private int offsetTime;
    private int playBackState;

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.musicListeners = new CopyOnWriteArrayList<>();
        playModes.put((byte) 1, "listLoop");
        playModes.put((byte) 2, "singleLoop");
        playModes.put((byte) 4, "listShuffled");
        playModes.put((byte) 8, "listOrder");
        setStateListener(DeviceCenterHandler.getDeviceCenterMgr());
    }

    public void setStateListener(MusicStatusListener listener) {
        this.mMusicStatusListener = listener;
    }

    CommonPlayer getMusicPlayer() {
        return this.musicPlayer;
    }

    boolean isPrepared() {
        LogMgr.d(TAG, "--->>isPrepared " + this.musicPlayer.isPrepared());
        return this.musicPlayer.isPrepared();
    }

    PlayItem play() {
        this.musicPlayer.play();
        return null;
    }

    PlayItem play(int currentPage, int totalPage) {
        this.musicPlayer.play(currentPage, totalPage);
        return null;
    }

    PlayItem play(String asrResult) {
        this.musicPlayer.play(asrResult);
        return null;
    }

    void pause() {
        LogMgr.d(TAG, "--->>pause");
        this.musicPlayer.pause();
    }

    void resume() {
        this.musicPlayer.resume();
    }

    void stop() {
        LogMgr.d(TAG, "--->>stop");
        this.musicPlayer.stop();
    }

    void playPrev() {
        this.musicPlayer.playPrev();
    }

    void playNext() {
        LogMgr.d(TAG, "--->>stop");
        this.musicPlayer.playNext();
    }

    void setPlaylist(List<PlayItem> itemList) {
        if (itemList != null) {
            this.currentPosition = 0;
        }
        this.musicPlayer.setPlayList(itemList);
        this.musicListenerIterator = this.musicListeners.iterator();
        while (this.musicListenerIterator.hasNext()) {
            this.musicListenerIterator.next().fireMusicListChanged(itemList);
        }
    }

    void appendPlaylist(int page, int totalPage, List<PlayItem> playItemList) {
        this.musicPlayer.appendPlayList(page, totalPage, playItemList);
        this.musicListenerIterator = this.musicListeners.iterator();
        while (this.musicListenerIterator.hasNext()) {
            this.musicListenerIterator.next().fireMusicListChanged(playItemList);
        }
    }

    long getDuration() {
        return this.musicPlayer.getDuration();
    }

    boolean playWhenReady() {
        return this.musicPlayer.getPlayWhenReady();
    }

    void fastForward() {
        long fastForwardTime = this.musicPlayer.getCurrentPosition() + 10000;
        if (this.offsetTime != 0) {
            fastForwardTime = this.offsetTime;
        }
        this.musicPlayer.seekTo(fastForwardTime);
        this.offsetTime = 0;
    }

    void skipToNext(boolean playWhenReady) {
        onMusicChanged(true, playWhenReady);
        LogMgr.d(TAG, "skipToNext musicIndex = %d ", Integer.valueOf(this.currentPosition));
    }

    void skipToPrevious(boolean playWhenReady) {
        onMusicChanged(false, playWhenReady);
        LogMgr.d(TAG, "skipToPrevious musicIndex = %d ", Integer.valueOf(this.currentPosition));
    }

    void skipToQueueItem(int index, boolean playWhenReady) {
        LogMgr.d(TAG, "skipToQueueItem musicIndex = %d ", Integer.valueOf(index));
        play();
    }

    void rewind() {
        long toPostion = this.musicPlayer.getCurrentPosition() - 10000;
        if (this.offsetTime != 0) {
            toPostion = this.offsetTime;
        }
        if (toPostion < 0) {
            this.musicPlayer.seekTo(0L);
        } else {
            this.musicPlayer.seekTo(toPostion);
        }
        this.offsetTime = 0;
    }

    void collectItem(boolean isCollected) {
        PlayItem currentItem = getCurrentItem();
        if (currentItem != null) {
            currentItem.setCollected(isCollected);
        }
    }

    boolean isCollected() {
        PlayItem currentItem = getCurrentItem();
        if (currentItem != null) {
            return currentItem.isCollected();
        }
        return false;
    }

    void setRepeating(boolean repeating) {
        LogMgr.d(TAG, "setRepeating to %b", Boolean.valueOf(repeating));
        setFlag(2, repeating);
        this.musicPlayer.setCurrentPlayMode(MusicPlayMode.MODE_SINGLE_CIRCLE);
    }

    boolean isRepeating() {
        LogMgr.d(TAG, "getRepeating to %b", Boolean.valueOf(getFlag(2)));
        return getFlag(2);
    }

    void setShuffled(boolean shuffled) {
        LogMgr.d(TAG, "setShuffled from %b to %b ", Boolean.valueOf(isShuffled()), Boolean.valueOf(shuffled));
        setFlag(4, shuffled);
        this.musicPlayer.setCurrentPlayMode(MusicPlayMode.MODE_ALL_RANDOM);
    }

    boolean isShuffled() {
        LogMgr.d(TAG, "isShuffled  %b ", Boolean.valueOf(getFlag(4)));
        return getFlag(4);
    }

    void setListLoop(boolean listLoop) {
        LogMgr.d(TAG, "setListLoop to %b", Boolean.valueOf(listLoop));
        setFlag(1, listLoop);
        this.musicPlayer.setCurrentPlayMode(MusicPlayMode.MODE_ALL_CIRCLE);
    }

    boolean isListLoop() {
        LogMgr.d(TAG, "isListLoop %b", Boolean.valueOf(getFlag(1)));
        return getFlag(1);
    }

    void setListOrder(boolean listOrder) {
        LogMgr.d(TAG, "setListOrder ot %b", Boolean.valueOf(listOrder));
        setFlag(8, listOrder);
        this.musicPlayer.setCurrentPlayMode(MusicPlayMode.MODE_ALL_ORDER);
    }

    boolean isListOrder() {
        LogMgr.d(TAG, "isListOrder %b", Boolean.valueOf(getFlag(8)));
        return getFlag(8);
    }

    List<PlayItem> getItemList() {
        return this.musicPlayer.getItemList();
    }

    PlayItem getCurrentItem() {
        return this.musicPlayer.getCurrentItem();
    }

    void registerMusicListener(MusicListenerWapper lis) {
        if (lis != null && !this.musicListeners.contains(lis)) {
            this.musicListeners.add(lis);
        }
    }

    void unRegisterListener(MusicListenerWapper lis) {
        if (this.musicListeners.contains(lis)) {
            this.musicListeners.remove(lis);
        }
    }

    int getPlaybackStatus() {
        return this.playBackState;
    }

    int getBufferPercent() {
        return this.musicPlayer.getBufferPercent();
    }

    byte getPlayMode() {
        return this.flags;
    }

    public String getPlayMode(byte key) {
        return playModes.get(Byte.valueOf(key));
    }

    void setBeginIndex(int index) {
        this.currentPosition = index;
    }

    void setOffsetTime(int offsetTime) {
        this.offsetTime = offsetTime;
    }

    private boolean getFlag(int flag) {
        return (this.flags & flag) != 0;
    }

    private void setFlag(int flag, boolean value) {
        this.flags = (byte) (this.flags & 0);
        if (value) {
            this.flags = (byte) (this.flags | flag);
        } else {
            this.flags = (byte) (this.flags & (flag ^ (-1)));
        }
        this.musicListenerIterator = this.musicListeners.iterator();
        while (this.musicListenerIterator.hasNext()) {
            String modeString = getPlayMode(this.flags);
            this.musicListenerIterator.next().firePlayModeChanged(modeString);
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0002. Please report as an issue. */
    private void onMusicChanged(boolean isNext, boolean playWhenReady) {
        switch (this.flags) {
        }
        stop();
        play();
        Iterator<MusicListenerWapper> it = this.musicListeners.iterator();
        while (it.hasNext()) {
            MusicListenerWapper lister = it.next();
            lister.fireMusicChange();
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer.Callback
    public void onPlayStateChanged(int state) {
        LogMgr.d(TAG, "onPlayStateChanged state=" + state);
        this.playBackState = state;
        this.mMusicStatusListener.onMusicStatusChanged(state);
        this.musicListenerIterator = this.musicListeners.iterator();
        while (this.musicListenerIterator.hasNext()) {
            MusicListenerWapper musicListener = this.musicListenerIterator.next();
            musicListener.fireStatusChanged(state);
        }
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer.Callback
    public void onPlayerError(String errorMessage) {
        LogMgr.d(TAG, "onPlayerError : %s", errorMessage);
        this.musicListenerIterator = this.musicListeners.iterator();
        while (this.musicListenerIterator.hasNext()) {
            this.musicListenerIterator.next().fireError(errorMessage);
        }
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer.Callback
    public void onOperateCommandChange(int operate) {
        LogMgr.d(TAG, "onOperateCommandChange : %d", Integer.valueOf(operate));
        this.musicListenerIterator = this.musicListeners.iterator();
        while (this.musicListenerIterator.hasNext()) {
            this.musicListenerIterator.next().fireItemOperateCommand(operate);
        }
    }

    public void switchTo(String itemId) {
        int index = findIndexById(itemId);
        this.musicPlayer.play(index);
    }

    private int findIndexById(String itemId) {
        List<PlayItem> itemList = getItemList();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId().equals(itemId)) {
                return i;
            }
        }
        return -1;
    }

    public int getPlayStatus() {
        return this.musicPlayer.getPlayStatus();
    }

    public void addCollectMusic(String id) {
        this.musicPlayer.addCollectMusic(id);
    }

    public void deleteCollectMusic(String id) {
        this.musicPlayer.deleteCollectMusic(id);
    }

    public void batchDeleteCollectMusic(String ids) {
        this.musicPlayer.batchDeleteCollectMusic(ids);
    }

    public PlayController.ItemPlayMode getPlayListMode() {
        return this.musicPlayer.getCurrentPlayMode();
    }

    public void play(List<PlayItem> itemList, int index) {
        this.musicPlayer.play(itemList, index);
    }

    public void play(List<PlayItem> itemList, int index, int currentPage, int totalPage) {
        this.musicPlayer.play(itemList, index, currentPage, totalPage);
    }

    public void setDevicePlayingType(String devicePlayingType) {
        this.musicPlayer.setDevicePlayingType(devicePlayingType);
    }

    public String getDevicePlayingType() {
        return this.musicPlayer.getDevicePlayingType();
    }

    public class MusicBinder extends Binder {
        public MusicBinder() {
        }

        public MusicService getService() {
            return MusicService.this;
        }

        public void setMusicPlayer(CommonPlayer commonPlayer) {
            MusicService.this.musicPlayer = commonPlayer;
            MusicService.this.musicPlayer.registerCallback(MusicService.this);
            MusicService.this.musicPlayer.setRenderer(MusicService.this);
        }
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer.Renderer
    public MusicPlayer.Renderer.RendererType getRendererType() {
        switch (this.currentItemType) {
            case TYPE_MUSIC:
                MusicPlayer.Renderer.RendererType rendererType = MusicPlayer.Renderer.RendererType.TYPE_MUSIC;
                return rendererType;
            case TYPE_AUDIO:
                MusicPlayer.Renderer.RendererType rendererType2 = MusicPlayer.Renderer.RendererType.TYPE_AUDIO;
                return rendererType2;
            case TYPE_RADIO:
                MusicPlayer.Renderer.RendererType rendererType3 = MusicPlayer.Renderer.RendererType.TYPE_RADIO;
                return rendererType3;
            default:
                MusicPlayer.Renderer.RendererType rendererType4 = MusicPlayer.Renderer.RendererType.TYPE_MUSIC;
                return rendererType4;
        }
    }
}
