package com.phicomm.speaker.device.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.phicomm.speaker.device.R;
import com.unisound.ant.device.DeviceCenterHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainTestActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.bt_alter_mode)
    Button btAlterMode;

    @BindView(R.id.bt_collect_music)
    Button btCollectMusic;

    @BindView(R.id.bt_enter_asr)
    Button btEnterAsr;

    @BindView(R.id.bt_get_unread)
    Button btGetUnread;

    @BindView(R.id.bt_history)
    Button btHistory;

    @BindView(R.id.bt_night_mode)
    Button btNightMode;
    private boolean isAlter = false;

    private Unbinder unbind;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_test_view);
        this.unbind = ButterKnife.bind(this);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if(this.unbind != null) {
            this.unbind.unbind();
        }
    }

    @OnClick({R.id.bt_alter_mode, R.id.bt_collect_music, R.id.bt_enter_asr, R.id.bt_night_mode, R.id.bt_get_unread, R.id.bt_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_alter_mode /* 2131361820 */:
                changeAlterMode();
                break;
            case R.id.bt_night_mode /* 2131361823 */:
                DeviceCenterHandler.getButtonControler().enterNightMode();
                break;
        }
    }

    private void changeAlterMode() {
        if (DeviceCenterHandler.getButtonControler() != null) {
            if (this.isAlter) {
                this.btAlterMode.setText("关闭警戒模式");
                DeviceCenterHandler.getButtonControler().enterAlertMode(true);
                this.isAlter = false;
            } else {
                this.btAlterMode.setText("打开警戒模式");
                DeviceCenterHandler.getButtonControler().enterAlertMode(false);
                this.isAlter = true;
            }
        }
    }
}
