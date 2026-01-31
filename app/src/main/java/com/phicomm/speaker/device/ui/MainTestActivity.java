package com.phicomm.speaker.device.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.phicomm.speaker.device.R;
import com.phicomm.speaker.device.R2;
import com.unisound.ant.device.DeviceCenterHandler;

public class MainTestActivity extends Activity {
    @Bind({R2.id.bt_alter_mode})
    Button btAlterMode;
    @Bind({R2.id.bt_collect_music})
    Button btCollectMusic;
    @Bind({R2.id.bt_enter_asr})
    Button btEnterAsr;
    @Bind({R2.id.bt_get_unread})
    Button btGetUnread;
    @Bind({R2.id.bt_history})
    Button btHistory;
    @Bind({R2.id.bt_night_mode})
    Button btNightMode;
    private boolean isAlter = false;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_test_view);
        ButterKnife.bind(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R2.id.bt_alter_mode, R2.id.bt_collect_music, R2.id.bt_enter_asr, R2.id.bt_night_mode, R2.id.bt_get_unread, R2.id.bt_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R2.id.bt_alter_mode:
                changeAlterMode();
                return;
            case R2.id.bt_collect_music:
            case R2.id.bt_enter_asr:
            default:
                return;
            case R2.id.bt_night_mode:
                DeviceCenterHandler.getButtonControler().enterNightMode();
                return;
        }
    }

    private void changeAlterMode() {
        if (DeviceCenterHandler.getButtonControler() == null) {
            return;
        }
        if (this.isAlter) {
            this.btAlterMode.setText("关闭警戒模式");
            DeviceCenterHandler.getButtonControler().enterAlertMode(true);
            this.isAlter = false;
            return;
        }
        this.btAlterMode.setText("打开警戒模式");
        DeviceCenterHandler.getButtonControler().enterAlertMode(false);
        this.isAlter = true;
    }
}
