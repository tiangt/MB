package com.whzl.mengbi.ui.activity;

import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.whzl.mengbi.R;
import com.whzl.mengbi.util.ToastUtils;

/**
 * @author nobody
 * @date 2018/12/21
 */
public class DrawLayoutControl {
    private Activity activity;
    private LinearLayout drawLayout;

    public DrawLayoutControl(Activity liveDisplayActivity, LinearLayout drawLayout) {
        this.activity = liveDisplayActivity;
        this.drawLayout = drawLayout;
    }

    public void init() {
        Switch switchVoice = drawLayout.findViewById(R.id.switch_voice_draw_layout_live);
        switchVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToastUtils.showToast("ss");
                }
            }
        });
    }
}
