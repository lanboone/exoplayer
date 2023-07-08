package com.google.android.exoplayer2.demo.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.demo.R;

public class NewSettingDialog extends Dialog implements DialogInterface.OnKeyListener, View.OnClickListener {

    private String TAG="NewSettingDialog";
    private int row = 0; //行,总共13行
    private TextView tv_sound_mode,tv_expansion_mode,tv_expansion_coefficient,tv_middle_range,tv_voice_add,tv_ctc_mode
            ,tv_virtual_style,tv_virtual_coefficient,tv_loudness_output_value;
    private Switch sw_vol,ctc_switch,virtual_switch,loudness_switch;

    public NewSettingDialog(@NonNull Context context) {
        super(context);
    }

    public NewSettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected NewSettingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    protected void onStart() {
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_setting);
        tv_sound_mode = findViewById(R.id.tv_sound_mode);
        tv_expansion_mode = findViewById(R.id.tv_expansion_mode);
        tv_expansion_coefficient = findViewById(R.id.tv_expansion_coefficient);
        tv_middle_range = findViewById(R.id.tv_middle_range);
        tv_voice_add = findViewById(R.id.tv_voice_add);
        tv_ctc_mode = findViewById(R.id.tv_ctc_mode);
        tv_virtual_style = findViewById(R.id.tv_virtual_style);
        tv_virtual_coefficient = findViewById(R.id.tv_virtual_coefficient);
        tv_loudness_output_value = findViewById(R.id.tv_loudness_output_value);
        sw_vol = findViewById(R.id.sw_vol);
        ctc_switch = findViewById(R.id.ctc_switch);
        virtual_switch = findViewById(R.id.virtual_switch);
        loudness_switch = findViewById(R.id.loudness_switch);
        tv_sound_mode.setOnClickListener(this);
        tv_expansion_mode.setOnClickListener(this);
        tv_expansion_coefficient.setOnClickListener(this);
        tv_middle_range.setOnClickListener(this);
        tv_voice_add.setOnClickListener(this);
        tv_ctc_mode.setOnClickListener(this);
        tv_virtual_style.setOnClickListener(this);
        tv_virtual_coefficient.setOnClickListener(this);
        tv_loudness_output_value.setOnClickListener(this);

    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        Log.d(TAG,"keycode :"+keyEvent.getKeyCode());
        if(keyEvent.getAction()==KeyEvent.ACTION_DOWN)
            return true;
        switch (keyEvent.getKeyCode()){
            case KeyEvent.KEYCODE_ENTER://ok
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                choose(true);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case 54 ://减少
//                choose(false);

                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
            case 52://增加
//                choose(true);
                break;
        }
        return false;
    }
    private void choose(boolean isUp) {
        if (row == 0){//音效
            sw_vol.setChecked(!sw_vol.isChecked());
            if(sw_vol.isChecked()){
                tv_sound_mode.setClickable(true);
                tv_expansion_mode.setClickable(true);
                tv_expansion_coefficient.setClickable(true);
                tv_middle_range.setClickable(true);
                tv_voice_add.setClickable(true);
            }else {
                tv_sound_mode.setClickable(false);
                tv_expansion_mode.setClickable(false);
                tv_expansion_coefficient.setClickable(false);
                tv_middle_range.setClickable(false);
                tv_voice_add.setClickable(false);
            }
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_sound_mode:
                break;

        }

    }
}
