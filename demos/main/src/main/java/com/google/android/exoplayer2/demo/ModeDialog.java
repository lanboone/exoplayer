package com.google.android.exoplayer2.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class ModeDialog extends Dialog implements DialogInterface.OnKeyListener {
    private static final String TAG = "ModeDialog";

    private Listener listener;
    private int mode = 2;
    private String modeName = "";
    private Switch sw_vol;
    private List<View> selects = new ArrayList<>();
    private int index=0;
    public ModeDialog(@NonNull Context context) {
        super(context);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    public ModeDialog(@NonNull Context context, Listener listener) {
        this(context);
        this.listener = listener;
    }

    @Override
    protected void onStart() {
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mode);
        setOnKeyListener(this);
        sw_vol = findViewById(R.id.sw_vol);
        sw_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.isEnable(sw_vol.isChecked());
                }
            }
        });
        selects.add(findViewById(R.id.item1));
        selects.add(findViewById(R.id.rb1));
        selects.add(findViewById(R.id.rb2));
        selects.add(findViewById(R.id.rb3));
        selects.add(findViewById(R.id.rb4));
        selects.add(findViewById(R.id.rb5));
        selects.add(findViewById(R.id.rb6));
        selects.add(findViewById(R.id.btn_confirm));
        selects.add(findViewById(R.id.btn_cancel));
        setOnKeyListener(this);
        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.ll_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ((RadioGroup)findViewById(R.id.rg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                modeName = rb.getText().toString();
                switch (modeName){
                    case "标准":
                        mode = 1;
                        break;
                    case "电影":
                        mode = 2;
                        break;
                    case "游戏":
                        mode = 3;
                        break;
                    case "音乐":
                        mode = 4;
                        break;
                    case "新闻":
                        mode = 5;
                        break;
                    case "电视":
                        mode = 6;
                        break;

                }
                if(listener!=null){
                    listener.modeChange(mode);
                }
            }
        });
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    //listener.onSelect(sw_vol.isChecked(),mode);
                    //Log.d(TAG, "音效: "+(sw_vol.isChecked()?"开":"关")+",模式："+modeName);
                }
                dismiss();
            }
        });
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        Log.d(TAG,"keycode :"+keyEvent.getKeyCode());
        if(keyEvent.getAction()==KeyEvent.ACTION_DOWN)
            return true;
        switch (keyEvent.getKeyCode()){
            case KeyEvent.KEYCODE_DPAD_UP:
                clearbg();
                if(index>0)
                    index--;
                setbg();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                clearbg();
                index++;
                index=index%selects.size();
                setbg();
                break;
            case KeyEvent.KEYCODE_ENTER://ok
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                choose();
                break;
            case KeyEvent.KEYCODE_BACK://返回
                dismiss();
                break;
        }
        return true;
    }

    private void choose() {
        switch (index){
            case 0:
                sw_vol.setChecked(!sw_vol.isChecked());
                if(listener!=null){
                    listener.isEnable(sw_vol.isChecked());
                }
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                ((RadioButton)selects.get(index)).setChecked(true);
                if(listener!=null){
                    listener.modeChange(index);
                }
                break;
            case 7:
                if(listener!=null){
                    //listener.onSelect(sw_vol.isChecked(),mode);
                    //Log.d(TAG, "音效: "+(sw_vol.isChecked()?"开":"关")+",模式："+modeName);
                }
                dismiss();
                break;
            default:
                dismiss();
        }
    }

    private void setbg() {
        selects.get(index).setBackgroundColor(Color.parseColor("#80808fff"));
    }

    private void clearbg() {
        for (int i = 0; i < selects.size(); i++) {
            if(i==5||i==6){
                selects.get(i).setBackgroundColor(Color.parseColor("#ff888888"));
            }else
                selects.get(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public interface Listener{
        /**
         * 数据回调
         * @param bVolEffect 音效
         * @param mode 模式：1，2，3，4
         */
        void onSelect(boolean bVolEffect,int mode);
        void isEnable(boolean bVolEffect);
        void modeChange(int mode);
    }



}
