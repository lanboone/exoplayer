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
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;


public class ParamDialog extends Dialog implements DialogInterface.OnKeyListener {
    private static final String TAG = "ParamDialog";

    private Listener listener;

    private int row = 0; //行
    private int column = 0; //列
    private int[] rowLimit = {2,1,2,2,2,2,1,2,2,2,2,2,6,3,2,2};



    private List<View> selects0 = new ArrayList<>();
    private List<View> selects1 = new ArrayList<>();
    private List<View> selects2 = new ArrayList<>();
    private List<View> selects3 = new ArrayList<>();
    private List<View> selects4 = new ArrayList<>();
    private List<View> selects5 = new ArrayList<>();
    private List<View> selects6 = new ArrayList<>();
    private List<View> selects7 = new ArrayList<>();
    private List<View> selects8 = new ArrayList<>();
    private List<View> selects9 = new ArrayList<>();
    private List<View> selects10 = new ArrayList<>();
    private List<View> selects11 = new ArrayList<>();
    private List<View> selects12 = new ArrayList<>();
    private List<View> selects13 = new ArrayList<>();
    private List<View> selects14 = new ArrayList<>();
    private List<View> selects15 = new ArrayList<>();

    private ModeConf mode4 = null;
    private int index=0;
    public ParamDialog(@NonNull Context context) {
        super(context);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    public ParamDialog(@NonNull Context context, Listener listener) {
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
        setContentView(R.layout.dialog_param);
        setOnKeyListener(this);

        //private List<View> selects0 = new ArrayList<>();
        selects0.add(findViewById(R.id.sw_upmix));
        selects0.add(findViewById(R.id.seekBarUpmixDelay));
        selects1.add(findViewById(R.id.sw_render));
        selects2.add(findViewById(R.id.seekBarLeftFront));
        selects2.add(findViewById(R.id.seekBarRightFront));
        selects3.add(findViewById(R.id.seekBarMid));
        selects3.add(findViewById(R.id.seekBarLow));

        selects4.add(findViewById(R.id.seekLeftRear));
        selects4.add(findViewById(R.id.seekBarRightRear));

        selects5.add(findViewById(R.id.seekLeftUp));
        selects5.add(findViewById(R.id.seekBarRightUp));
        selects6.add(findViewById(R.id.sw_Eq));

        selects7.add(findViewById(R.id.seekBar150));
        selects7.add(findViewById(R.id.seekBar500));

        selects8.add(findViewById(R.id.seek1k));
        selects8.add(findViewById(R.id.seek2k));

        selects9.add(findViewById(R.id.seek4k));
        selects9.add(findViewById(R.id.seekBar6k));

        selects10.add(findViewById(R.id.seek8k));
        selects10.add(findViewById(R.id.seekBar10k));

        selects11.add(findViewById(R.id.seek12k));
        selects11.add(findViewById(R.id.seekBar16k));

        selects12.add(findViewById(R.id.sw_drc));
        selects12.add(findViewById(R.id.rb0));
        selects12.add(findViewById(R.id.rb1));
        selects12.add(findViewById(R.id.rb2));
        selects12.add(findViewById(R.id.rb3));
        selects12.add(findViewById(R.id.rb4));


        selects13.add(findViewById(R.id.sw_ctc));
        selects13.add(findViewById(R.id.seekSpkDis));
        selects13.add(findViewById(R.id.seekSeeDis));

        selects14.add(findViewById(R.id.seekBarLoopTime));
        selects14.add(findViewById(R.id.seekBarGain));

        selects15.add(findViewById(R.id.btn_confirm));
        selects15.add(findViewById(R.id.btn_cancel));


        //获取配置
        mode4 = new ModeConf();
        mode4.copy(EqAudioProcessorJni.getInstance().mode4Conf);

        updateView(mode4);

    }

    private void updateView(ModeConf conf){
        {
            Switch sw = (Switch) selects0.get(0);
            if (conf.bUpmixInvalid == 0) {
                sw.setChecked(true);
            } else if (conf.bUpmixInvalid == 1) {
                sw.setChecked(false);
            }
        }

        {
            ((SeekBar) selects0.get(1)).setProgress(conf.nDelayAllPass * 5);
            StringBuilder sb = new StringBuilder();
            sb.append(conf.nDelayAllPass);
            sb.append("m");
            ((TextView) findViewById(R.id.upmixDelay)).setText(sb.toString());
            sb = null;
        }
        {
            Switch sw = (Switch) selects1.get(0);
            if (conf.nHRTFMode == 2) {
                sw.setChecked(true);
            } else if (conf.nHRTFMode == 0) {
                sw.setChecked(false);
            }
        }
        {
            ((SeekBar)selects2.get(0)).setProgress((int)(conf.afHRTFChannelGain[0]+24)*2);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afHRTFChannelGain[0]);
            sb.append("dB");
            ((TextView)findViewById(R.id.TextLeftFront)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar)selects2.get(1)).setProgress((int)(conf.afHRTFChannelGain[1]+24)*2);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afHRTFChannelGain[1]);
            sb.append("dB");
            ((TextView)findViewById(R.id.TextRightFront)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar)selects3.get(0)).setProgress((int)(conf.afHRTFChannelGain[2]+24)*2);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afHRTFChannelGain[2]);
            sb.append("dB");
            ((TextView)findViewById(R.id.TextMid)).setText(sb.toString());
            sb = null;
        }


        {
            ((SeekBar)selects3.get(1)).setProgress((int)(conf.afHRTFChannelGain[3]+24)*2);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afHRTFChannelGain[3]);
            sb.append("dB");
            ((TextView)findViewById(R.id.TextLow)).setText(sb.toString());
            sb = null;
        }

        {
            ((SeekBar)selects4.get(0)).setProgress((int)(conf.afHRTFChannelGain[4]+24)*2);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afHRTFChannelGain[4]);
            sb.append("dB");
            ((TextView)findViewById(R.id.TextLeftRear)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar)selects4.get(1)).setProgress((int)(conf.afHRTFChannelGain[5]+24)*2);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afHRTFChannelGain[5]);
            sb.append("dB");
            ((TextView)findViewById(R.id.TextRightRear)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar)selects5.get(0)).setProgress((int)(conf.afHRTFChannelGain[6]+24)*2);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afHRTFChannelGain[6]);
            sb.append("dB");
            ((TextView)findViewById(R.id.TextLeftUp)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar)selects5.get(1)).setProgress((int)(conf.afHRTFChannelGain[7]+24)*2);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afHRTFChannelGain[7]);
            sb.append("dB");
            ((TextView)findViewById(R.id.TextRightUp)).setText(sb.toString());
            sb = null;
        }

        {
            Switch sw = (Switch) selects6.get(0);
            if (conf.bEqActive == 1) {
                sw.setChecked(true);
            } else if (conf.bEqActive == 0) {
                sw.setChecked(false);
            }
        }


        {
            ((SeekBar)selects7.get(0)).setProgress((int)(conf.afEQGain[0]+12)*4);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afEQGain[0]);
            sb.append("dB");
            ((TextView)findViewById(R.id.Text150)).setText(sb.toString());
            sb = null;
        }

        {
            ((SeekBar)selects7.get(1)).setProgress((int)(conf.afEQGain[1]+12)*4);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afEQGain[1]);
            sb.append("dB");
            ((TextView)findViewById(R.id.Text500)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar) selects8.get(0)).setProgress((int) (conf.afEQGain[2] + 12) * 4);
            StringBuilder sb = new StringBuilder();
            sb.append((int) conf.afEQGain[2]);
            sb.append("dB");
            ((TextView) findViewById(R.id.Text1k)).setText(sb.toString());
            sb = null;
        }

        {
            ((SeekBar) selects8.get(1)).setProgress((int) (conf.afEQGain[3] + 12) * 4);
            StringBuilder sb = new StringBuilder();
            sb.append((int) conf.afEQGain[3]);
            sb.append("dB");
            ((TextView) findViewById(R.id.Text2k)).setText(sb.toString());
            sb = null;
        }

        {
            ((SeekBar) selects9.get(0)).setProgress((int) (conf.afEQGain[4] + 12) * 4);
            StringBuilder sb = new StringBuilder();
            sb.append((int) conf.afEQGain[4]);
            sb.append("dB");
            ((TextView) findViewById(R.id.Text4k)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar)selects9.get(1)).setProgress((int)(conf.afEQGain[5]+12)*4);
            StringBuilder sb = new StringBuilder();
            sb.append((int)conf.afEQGain[5]);
            sb.append("dB");
            ((TextView)findViewById(R.id.Text6k)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar) selects10.get(0)).setProgress((int) (conf.afEQGain[6] + 12) * 4);
            StringBuilder sb = new StringBuilder();
            sb.append((int) conf.afEQGain[6]);
            sb.append("dB");
            ((TextView) findViewById(R.id.Text8k)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar) selects10.get(1)).setProgress((int) (conf.afEQGain[7] + 12) * 4);
            StringBuilder sb = new StringBuilder();
            sb.append((int) conf.afEQGain[7]);
            sb.append("dB");
            ((TextView) findViewById(R.id.Text10k)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar) selects11.get(0)).setProgress((int) (conf.afEQGain[8] + 12) * 4);
            StringBuilder sb = new StringBuilder();
            sb.append((int) conf.afEQGain[8]);
            sb.append("dB");
            ((TextView) findViewById(R.id.Text12k)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar) selects11.get(1)).setProgress((int) (conf.afEQGain[9] + 12) * 4);
            StringBuilder sb = new StringBuilder();
            sb.append((int) conf.afEQGain[9]);
            sb.append("dB");
            ((TextView) findViewById(R.id.Text16k)).setText(sb.toString());
            sb = null;
        }

        //DCR SWICH
        {
            Switch sw = (Switch) selects12.get(0);
            if (conf.bDrcActive == 1) {
                sw.setChecked(true);
            } else if (conf.bDrcActive == 0) {
                sw.setChecked(false);
            }
        }

        {
            ((RadioButton)selects12.get(conf.nDRCId+1)).setChecked(true);
        }

        //conf. nDRCId

        //CTC SWICH
        {
            Switch sw = (Switch) selects13.get(0);
            if (conf.bCTCInvalid == 0) {
                sw.setChecked(true);
            } else if (conf.bCTCInvalid == 1) {
                sw.setChecked(false);
            }
        }

        {
            ((SeekBar)selects13.get(1)).setProgress((int)((conf.fSpeakerToSpeaker-0.1)*50));
            StringBuilder sb = new StringBuilder();
            sb.append(conf.fSpeakerToSpeaker);
            sb.append("米");
            ((TextView)findViewById(R.id.TextSpkDis)).setText(sb.toString());
            sb = null;

        }
        {
            ((SeekBar)selects13.get(2)).setProgress((int)((conf.fListenerToSpeaker * 10 - 5)*4));
            StringBuilder sb = new StringBuilder();
            sb.append(conf.fListenerToSpeaker);
            sb.append("米");
            ((TextView)findViewById(R.id.TextSeeDis)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar)selects14.get(0)).setProgress(conf.nLoopNumber);
            StringBuilder sb = new StringBuilder();
            sb.append(conf.nLoopNumber);
            ((TextView)findViewById(R.id.TextLoopTime)).setText(sb.toString());
            sb = null;
        }
        {
            ((SeekBar)selects14.get(1)).setProgress((int)((conf.fGain-0.1)*50));
            StringBuilder sb = new StringBuilder();
            sb.append(conf.fGain);
            ((TextView)findViewById(R.id.TextGain)).setText(sb.toString());
            sb = null;
        }
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        Log.d(TAG,"keycode :"+keyEvent.getKeyCode());
        if(keyEvent.getAction()==KeyEvent.ACTION_DOWN)
            return true;
        switch (keyEvent.getKeyCode()){
            case KeyEvent.KEYCODE_DPAD_UP:
                clearbg();
                //if(index>0)
                //    index--;
                //setbg();
                if(row > 0){
                    row--;
                }
                column = 0;
                setBg(row,column);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                clearbg();
                //index++;
                //index=index%selects.size();
                //setbg();
                if(row < 15){
                    row ++;
                }
                column = 0;
                setBg(row,column);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                clearbg();
                if(column > 0){
                    column--;
                }
                setBg(row,column);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                clearbg();
                if(column < (rowLimit[row] - 1)){
                    column++;
                }
                setBg(row,column);
                break;

            case KeyEvent.KEYCODE_ENTER://ok
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                choose(true,true);
                break;
            case KeyEvent.KEYCODE_BACK://返回
                dismiss();
                break;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case 54 ://减少
                choose(false,false);
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
            case 52://增加
                choose(true,false);
                break;
        }
        Log.d(TAG,"row :"+row + ",column:"+column);
        return true;
    }

    private void choose(boolean flag,boolean flag2) {
        if(row == 15 && column == 0) //确定
        {
            //写入配置文件
            EqAudioProcessorJni.getInstance().mode4Conf.copy(mode4);

            ConfParser parser = new ConfParser();
            parser.saveMode4(mode4);
            if( EqAudioProcessorJni.getInstance().mMode == 4) {
                EqAudioProcessorJni.getInstance().setMode(4);
            }

            dismiss();
        }
        else if(row == 15 && column == 1)
        {
            dismiss();
        }
        else if(row == 0 && column == 0){
            //sw_vol.setChecked(!sw_vol.isChecked());sw_vol.setChecked(!sw_vol.isChecked());
            Switch sw = (Switch)selects0.get(column);
            sw.setChecked(!sw.isChecked());
            if(sw.isChecked() == true){
                mode4.bUpmixInvalid = 0;
            }else{
                mode4.bUpmixInvalid = 1;
            }
        }
        else if(row == 1 && column == 0){
            Switch sw = (Switch)selects1.get(column);
            sw.setChecked(!sw.isChecked());
            if(sw.isChecked() == true){
                mode4.nHRTFMode = 2;
            }else{
                mode4.nHRTFMode = 0;
            }

        }
        else if(row == 6 && column == 0){
            Switch sw = (Switch)selects6.get(column);
            sw.setChecked(!sw.isChecked());

            if(sw.isChecked() == true){
                mode4.bEqActive = 1;
            }else{
                mode4.bEqActive = 0;
            }

        }
        else if(row == 12 && column == 0){
            Switch sw = (Switch)selects12.get(column);
            sw.setChecked(!sw.isChecked());
            if(sw.isChecked() == true){
                mode4.bDrcActive = 1;
            }else{
                mode4.bDrcActive = 0;
            }
        }
        else if(row == 13 && column == 0){
            Switch sw = (Switch)selects13.get(column);
            sw.setChecked(!sw.isChecked());
            if(sw.isChecked() == true){
                mode4.bCTCInvalid = 0;
            }else{
                mode4.bCTCInvalid = 1;
            }
        }
        else if(row == 12 && (column == 1||column ==2 ||column== 3||column==4||column==5)){
            ((RadioButton)selects12.get(column)).setChecked(true);
            mode4.nDRCId = column - 1;
        }
        else
        {
            if(row == 0 && column == 1){  //Upmix 延迟
                if(flag2 == false){
                    if(flag == true){
                        if(mode4.nDelayAllPass < 20){
                            mode4.nDelayAllPass++;
                        }

                    }else{
                        if(mode4.nDelayAllPass > 0){
                            mode4.nDelayAllPass--;
                        }
                    }
                }else{
                    if(mode4.nDelayAllPass < 21){
                        mode4.nDelayAllPass++;
                    }
                    if(mode4.nDelayAllPass == 21){
                        mode4.nDelayAllPass = 0;
                    }
                }
                ((SeekBar)selects0.get(1)).setProgress(mode4.nDelayAllPass*5);
                StringBuilder sb = new StringBuilder();
                sb.append(mode4.nDelayAllPass);
                sb.append("m");
                ((TextView)findViewById(R.id.upmixDelay)).setText(sb.toString());
                sb = null;
            }else if(row == 2 && column == 0){//左前、右前、中置、超低、左后、右后、左上、右上；后两个：保留
                //mode4.afHRTFChannelGain[0] =
                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afHRTFChannelGain[0] < 24){
                            mode4.afHRTFChannelGain[0]++;
                        }
                    }else{
                        if(mode4.afHRTFChannelGain[0]> -24){
                            mode4.afHRTFChannelGain[0]--;
                        }
                    }
                }else{
                    if(mode4.afHRTFChannelGain[0] < 25){
                        mode4.afHRTFChannelGain[0]++;
                    }
                    if(mode4.afHRTFChannelGain[0] == 25){
                        mode4.afHRTFChannelGain[0] = -24;
                    }
                }
                ((SeekBar)selects2.get(0)).setProgress((int)(mode4.afHRTFChannelGain[0]+24)*2);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afHRTFChannelGain[0]);
                sb.append("dB");
                ((TextView)findViewById(R.id.TextLeftFront)).setText(sb.toString());
                sb = null;
            }else if(row == 2 && column == 1){//左前、右前、中置、超低、左后、右后、左上、右上；后两个：保留
                //mode4.afHRTFChannelGain[0] =

                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afHRTFChannelGain[1] < 24){
                            mode4.afHRTFChannelGain[1]++;
                        }
                    }else{
                        if(mode4.afHRTFChannelGain[1]> -24){
                            mode4.afHRTFChannelGain[1]--;
                        }
                    }
                }else{
                    if(mode4.afHRTFChannelGain[1] < 25){
                        mode4.afHRTFChannelGain[1]++;
                    }
                    if(mode4.afHRTFChannelGain[1] == 25){
                        mode4.afHRTFChannelGain[1] = -24;
                    }
                }
                ((SeekBar)selects2.get(1)).setProgress((int)(mode4.afHRTFChannelGain[1]+24)*2);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afHRTFChannelGain[1]);
                sb.append("dB");
                ((TextView)findViewById(R.id.TextRightFront)).setText(sb.toString());
                sb = null;
            }else if(row == 3 && column == 0){//左前、右前、中置、超低、左后、右后、左上、右上；后两个：保留
                //mode4.afHRTFChannelGain[0] =

                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afHRTFChannelGain[2] < 24){
                            mode4.afHRTFChannelGain[2]++;
                        }
                    }else{
                        if(mode4.afHRTFChannelGain[2]> -24){
                            mode4.afHRTFChannelGain[2]--;
                        }
                    }
                }else{
                    if(mode4.afHRTFChannelGain[2] < 25){
                        mode4.afHRTFChannelGain[2]++;
                    }
                    if(mode4.afHRTFChannelGain[2] == 25){
                        mode4.afHRTFChannelGain[2] = -24;
                    }
                }
                ((SeekBar)selects3.get(column)).setProgress((int)(mode4.afHRTFChannelGain[2]+24)*2);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afHRTFChannelGain[2]);
                sb.append("dB");
                ((TextView)findViewById(R.id.TextMid)).setText(sb.toString());
                sb = null;
            }
            else if(row == 3 && column == 1){//左前、右前、中置、超低、左后、右后、左上、右上；后两个：保留
                //mode4.afHRTFChannelGain[0] =

                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afHRTFChannelGain[3] < 24){
                            mode4.afHRTFChannelGain[3]++;
                        }
                    }else{
                        if(mode4.afHRTFChannelGain[3]> -24){
                            mode4.afHRTFChannelGain[3]--;
                        }
                    }
                }else{
                    if(mode4.afHRTFChannelGain[3] < 25){
                        mode4.afHRTFChannelGain[3]++;
                    }
                    if(mode4.afHRTFChannelGain[3] == 25){
                        mode4.afHRTFChannelGain[3] = -24;
                    }
                }
                ((SeekBar)selects3.get(column)).setProgress((int)(mode4.afHRTFChannelGain[3]+24)*2);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afHRTFChannelGain[3]);
                sb.append("dB");
                ((TextView)findViewById(R.id.TextLow)).setText(sb.toString());
                sb = null;
            }
            else if(row == 4 && column == 0){//左前、右前、中置、超低、左后、右后、左上、右上；后两个：保留
                //mode4.afHRTFChannelGain[0] =


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afHRTFChannelGain[4] < 24){
                            mode4.afHRTFChannelGain[4]++;
                        }
                    }else{
                        if(mode4.afHRTFChannelGain[4]> -24){
                            mode4.afHRTFChannelGain[4]--;
                        }
                    }
                }else{
                    if(mode4.afHRTFChannelGain[4] < 25){
                        mode4.afHRTFChannelGain[4]++;
                    }
                    if(mode4.afHRTFChannelGain[4] == 25){
                        mode4.afHRTFChannelGain[4] = -24;
                    }
                }
                ((SeekBar)selects4.get(column)).setProgress((int)(mode4.afHRTFChannelGain[4]+24)*2);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afHRTFChannelGain[4]);
                sb.append("dB");
                ((TextView)findViewById(R.id.TextLeftRear)).setText(sb.toString());
                sb = null;
            }else if(row == 4 && column == 1){//左前、右前、中置、超低、左后、右后、左上、右上；后两个：保留
                //mode4.afHRTFChannelGain[0] =


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afHRTFChannelGain[5] < 24){
                            mode4.afHRTFChannelGain[5]++;
                        }
                    }else{
                        if(mode4.afHRTFChannelGain[5]> -24){
                            mode4.afHRTFChannelGain[5]--;
                        }
                    }
                }else{
                    if(mode4.afHRTFChannelGain[5] < 25){
                        mode4.afHRTFChannelGain[5]++;
                    }
                    if(mode4.afHRTFChannelGain[5] == 25){
                        mode4.afHRTFChannelGain[5] = -24;
                    }
                }
                ((SeekBar)selects4.get(column)).setProgress((int)(mode4.afHRTFChannelGain[5]+24)*2);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afHRTFChannelGain[5]);
                sb.append("dB");
                ((TextView)findViewById(R.id.TextRightRear)).setText(sb.toString());
                sb = null;
            }else if(row == 5 && column == 0){//左前、右前、中置、超低、左后、右后、左上、右上；后两个：保留
                //mode4.afHRTFChannelGain[0] =


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afHRTFChannelGain[6] < 24){
                            mode4.afHRTFChannelGain[6]++;
                        }
                    }else{
                        if(mode4.afHRTFChannelGain[6]> -24){
                            mode4.afHRTFChannelGain[6]--;
                        }
                    }
                }else{
                    if(mode4.afHRTFChannelGain[6] < 25){
                        mode4.afHRTFChannelGain[6]++;
                    }
                    if(mode4.afHRTFChannelGain[6] == 25){
                        mode4.afHRTFChannelGain[6] = -24;
                    }
                }
                ((SeekBar)selects5.get(column)).setProgress((int)(mode4.afHRTFChannelGain[6]+24)*2);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afHRTFChannelGain[6]);
                sb.append("dB");
                ((TextView)findViewById(R.id.TextLeftUp)).setText(sb.toString());
                sb = null;
            }else if(row == 5 && column == 1){//左前、右前、中置、超低、左后、右后、左上、右上；后两个：保留
                //mode4.afHRTFChannelGain[0] =

                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afHRTFChannelGain[7] < 24){
                            mode4.afHRTFChannelGain[7]++;
                        }
                    }else{
                        if(mode4.afHRTFChannelGain[7]> -24){
                            mode4.afHRTFChannelGain[7]--;
                        }
                    }
                }else{
                    if(mode4.afHRTFChannelGain[7] < 25){
                        mode4.afHRTFChannelGain[7]++;
                    }
                    if(mode4.afHRTFChannelGain[7] == 25){
                        mode4.afHRTFChannelGain[7] = -24;
                    }
                }
                ((SeekBar)selects5.get(column)).setProgress((int)(mode4.afHRTFChannelGain[7]+24)*2);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afHRTFChannelGain[7]);
                sb.append("dB");
                ((TextView)findViewById(R.id.TextRightUp)).setText(sb.toString());
                sb = null;
            }
            else if(row == 7 && column == 0){ //150hz 数组[10]，对应频点从低到高（顺序：从左到右，从上到下）


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afEQGain[0] < 12){
                            mode4.afEQGain[0]++;
                        }
                    }else{
                        if(mode4.afEQGain[0] > -12){
                            mode4.afEQGain[0]--;
                        }
                    }
                }else{
                    if(mode4.afEQGain[0] < 13){
                        mode4.afEQGain[0]++;
                    }
                    if(mode4.afEQGain[0] == 13){
                        mode4.afEQGain[0] = -12;
                    }
                }
                ((SeekBar)selects7.get(column)).setProgress((int)(mode4.afEQGain[0]+12)*4);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afEQGain[0]);
                sb.append("dB");
                ((TextView)findViewById(R.id.Text150)).setText(sb.toString());
                sb = null;
            }else if(row == 7 && column == 1){ //150hz 数组[10]，对应频点从低到高（顺序：从左到右，从上到下）


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afEQGain[1] < 12){
                            mode4.afEQGain[1]++;
                        }
                    }else{
                        if(mode4.afEQGain[1] > -12){
                            mode4.afEQGain[1]--;
                        }
                    }
                }else{
                    if(mode4.afEQGain[1] < 13){
                        mode4.afEQGain[1]++;
                    }
                    if(mode4.afEQGain[1] == 13){
                        mode4.afEQGain[1] = -12;
                    }
                }
                ((SeekBar)selects7.get(column)).setProgress((int)(mode4.afEQGain[1]+12)*4);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afEQGain[1]);
                sb.append("dB");
                ((TextView)findViewById(R.id.Text500)).setText(sb.toString());
                sb = null;
            }else if(row == 8 && column == 0){ //150hz 数组[10]，对应频点从低到高（顺序：从左到右，从上到下）


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afEQGain[2] < 12){
                            mode4.afEQGain[2]++;
                        }
                    }else{
                        if(mode4.afEQGain[2] > -12){
                            mode4.afEQGain[2]--;
                        }
                    }
                }else{
                    if(mode4.afEQGain[2] < 13){
                        mode4.afEQGain[2]++;
                    }
                    if(mode4.afEQGain[2] == 13){
                        mode4.afEQGain[2] = -12;
                    }
                }
                ((SeekBar)selects8.get(column)).setProgress((int)(mode4.afEQGain[2]+12)*4);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afEQGain[2]);
                sb.append("dB");
                ((TextView)findViewById(R.id.Text1k)).setText(sb.toString());
                sb = null;
            }else if(row == 8 && column == 1){ //150hz 数组[10]，对应频点从低到高（顺序：从左到右，从上到下）


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afEQGain[3] < 12){
                            mode4.afEQGain[3]++;
                        }
                    }else{
                        if(mode4.afEQGain[3] > -12){
                            mode4.afEQGain[3]--;
                        }
                    }
                }else{
                    if(mode4.afEQGain[4] < 13){
                        mode4.afEQGain[4]++;
                    }
                    if(mode4.afEQGain[4] == 13){
                        mode4.afEQGain[4] = -12;
                    }
                }
                ((SeekBar)selects8.get(column)).setProgress((int)(mode4.afEQGain[3]+12)*4);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afEQGain[3]);
                sb.append("dB");
                ((TextView)findViewById(R.id.Text2k)).setText(sb.toString());
                sb = null;
            }else if(row == 9 && column == 0){ //150hz 数组[10]，对应频点从低到高（顺序：从左到右，从上到下）


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afEQGain[4] < 12){
                            mode4.afEQGain[4]++;
                        }
                    }else{
                        if(mode4.afEQGain[4] > -12){
                            mode4.afEQGain[4]--;
                        }
                    }
                }else{
                    if(mode4.afEQGain[4] < 13){
                        mode4.afEQGain[4]++;
                    }
                    if(mode4.afEQGain[4] == 13){
                        mode4.afEQGain[4] = -12;
                    }
                }
                ((SeekBar)selects9.get(column)).setProgress((int)(mode4.afEQGain[4]+12)*4);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afEQGain[4]);
                sb.append("dB");
                ((TextView)findViewById(R.id.Text4k)).setText(sb.toString());
                sb = null;
            }else if(row == 9 && column == 1){ //150hz 数组[10]，对应频点从低到高（顺序：从左到右，从上到下）


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afEQGain[5] < 12){
                            mode4.afEQGain[5]++;
                        }
                    }else{
                        if(mode4.afEQGain[5] > -12){
                            mode4.afEQGain[5]--;
                        }
                    }
                }else{
                    if(mode4.afEQGain[5] < 13){
                        mode4.afEQGain[5]++;
                    }
                    if(mode4.afEQGain[5] == 13){
                        mode4.afEQGain[5] = -12;
                    }
                }
                ((SeekBar)selects9.get(column)).setProgress((int)(mode4.afEQGain[5]+12)*4);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afEQGain[5]);
                sb.append("dB");
                ((TextView)findViewById(R.id.Text6k)).setText(sb.toString());
                sb = null;
            }else if(row == 10 && column == 0){ //150hz 数组[10]，对应频点从低到高（顺序：从左到右，从上到下）


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afEQGain[6] < 12){
                            mode4.afEQGain[6]++;
                        }
                    }else{
                        if(mode4.afEQGain[6] > -12){
                            mode4.afEQGain[6]--;
                        }
                    }
                }else{
                    if(mode4.afEQGain[6] < 13){
                        mode4.afEQGain[6]++;
                    }
                    if(mode4.afEQGain[6] == 13){
                        mode4.afEQGain[6] = -12;
                    }
                }
                ((SeekBar)selects10.get(column)).setProgress((int)(mode4.afEQGain[6]+12)*4);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afEQGain[6]);
                sb.append("dB");
                ((TextView)findViewById(R.id.Text8k)).setText(sb.toString());
                sb = null;
            }else if(row == 10 && column == 1){ //150hz 数组[10]，对应频点从低到高（顺序：从左到右，从上到下）


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afEQGain[7] < 12){
                            mode4.afEQGain[7]++;
                        }
                    }else{
                        if(mode4.afEQGain[7] > -12){
                            mode4.afEQGain[7]--;
                        }
                    }
                }else{
                    if(mode4.afEQGain[7] < 13){
                        mode4.afEQGain[7]++;
                    }
                    if(mode4.afEQGain[7] == 13){
                        mode4.afEQGain[7] = -12;
                    }
                }
                ((SeekBar)selects10.get(column)).setProgress((int)(mode4.afEQGain[7]+12)*4);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afEQGain[7]);
                sb.append("dB");
                ((TextView)findViewById(R.id.Text10k)).setText(sb.toString());
                sb = null;
            }else if(row == 11 && column == 0){ //150hz 数组[10]，对应频点从低到高（顺序：从左到右，从上到下）


                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afEQGain[8] < 12){
                            mode4.afEQGain[8]++;
                        }
                    }else{
                        if(mode4.afEQGain[8] > -12){
                            mode4.afEQGain[8]--;
                        }
                    }
                }else{
                    if(mode4.afEQGain[8] < 13){
                        mode4.afEQGain[8]++;
                    }
                    if(mode4.afEQGain[8] == 13){
                        mode4.afEQGain[8] = -12;
                    }
                }
                ((SeekBar)selects11.get(column)).setProgress((int)(mode4.afEQGain[8]+12)*4);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afEQGain[8]);
                sb.append("dB");
                ((TextView)findViewById(R.id.Text12k)).setText(sb.toString());
                sb = null;
            }else if(row == 11 && column == 1){ //150hz 数组[10]，对应频点从低到高（顺序：从左到右，从上到下）

                if(flag2 == false){
                    if(flag == true){
                        if(mode4.afEQGain[9] < 12){
                            mode4.afEQGain[9]++;
                        }
                    }else{
                        if(mode4.afEQGain[9] > -12){
                            mode4.afEQGain[9]--;
                        }
                    }
                }else{
                    if(mode4.afEQGain[9] < 13){
                        mode4.afEQGain[9]++;
                    }
                    if(mode4.afEQGain[9] == 13){
                        mode4.afEQGain[9] = -12;
                    }
                }
                ((SeekBar)selects11.get(column)).setProgress((int)(mode4.afEQGain[9]+12)*4);
                StringBuilder sb = new StringBuilder();
                sb.append((int)mode4.afEQGain[9]);
                sb.append("dB");
                ((TextView)findViewById(R.id.Text16k)).setText(sb.toString());
                sb = null;
            }
            else if(row == 13 && column == 1){ //喇叭距离 0.1 - 2 （0.1）
                int vT = (int )(mode4.fSpeakerToSpeaker * 10);
                if(flag2 == false) {
                    if (flag == true) {
                        if (vT < 20) {
                            vT++;
                        }

                    } else {
                        if (vT > 1) {
                            vT--;
                        }
                    }
                }else{
                    if (vT < 21) {
                        vT++;
                    }
                    if(vT == 21){
                        vT = 1;
                    }
                }
                mode4.fSpeakerToSpeaker = (float)vT/10;
                ((SeekBar)selects13.get(1)).setProgress((int)((mode4.fSpeakerToSpeaker-0.1)*50));
                StringBuilder sb = new StringBuilder();
                sb.append(mode4.fSpeakerToSpeaker);
                sb.append("米");
                ((TextView)findViewById(R.id.TextSpkDis)).setText(sb.toString());
                sb = null;

            }
            else if(row == 13 && column == 2){ //观看距离 0.5 - 3  (0.1)
                int vT = (int )(mode4.fListenerToSpeaker * 10);
                if(flag2 == false){
                    if(flag == true){
                        if(vT < 30){
                            vT++;
                        }

                    }else{
                        if(vT > 5){
                            vT--;
                        }
                    }
                }else{
                    if(vT < 31){
                        vT++;
                    }
                    if(vT == 31 ){
                        vT = 5;
                    }
                }

                mode4.fListenerToSpeaker = (float)vT/10;
                ((SeekBar)selects13.get(2)).setProgress((vT - 5)*4);
                StringBuilder sb = new StringBuilder();
                sb.append(mode4.fListenerToSpeaker);
                sb.append("米");
                ((TextView)findViewById(R.id.TextSeeDis)).setText(sb.toString());
                sb = null;
            }
            else if(row == 14 && column == 0){ //循环次数 5- 100 （1）
                if(flag2 == false) {
                    if (flag == true) {
                        if (mode4.nLoopNumber < 100) {
                            mode4.nLoopNumber++;
                        }
                    } else {
                        if (mode4.nLoopNumber > 5) {
                            mode4.nLoopNumber--;
                        }
                    }
                }else{
                    if (mode4.nLoopNumber < 101) {
                        mode4.nLoopNumber++;
                    }
                    if(mode4.nLoopNumber == 101){
                        mode4.nLoopNumber = 5;
                    }
                }
                ((SeekBar)selects14.get(0)).setProgress(mode4.nLoopNumber);
                StringBuilder sb = new StringBuilder();
                sb.append(mode4.nLoopNumber);
                ((TextView)findViewById(R.id.TextLoopTime)).setText(sb.toString());
                sb = null;
            }
            else if(row == 14 && column == 1){ //增益调整（0.1-2）（0.1）
                //fGain
                int vT = (int )(mode4.fGain * 10);
                if(flag2 == false) {
                    if (flag == true) {
                        if (vT < 20) {
                            vT++;
                        }

                    } else {
                        if (vT > 1) {
                            vT--;
                        }
                    }
                }else{
                    if (vT < 21) {
                        vT++;
                    }
                    if(vT == 21){
                        vT = 1;
                    }
                }
                mode4.fGain = (float)vT/10;
                ((SeekBar)selects14.get(1)).setProgress((int)((mode4.fGain-0.1)*50));
                StringBuilder sb = new StringBuilder();
                sb.append(mode4.fGain);
                ((TextView)findViewById(R.id.TextGain)).setText(sb.toString());
                sb = null;
            }

        }
        /*
        switch (index){
            case 0:

                break;
            case 1:
            case 2:
            case 3:
            case 4:

                break;
            case 5:

                dismiss();
                break;
            default:
                dismiss();
        }*/
    }
    private void setBg(int row,int column){
        if(row > 15){
            return;
        }
        switch (row){
            case 0 :
                selects0.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 1 :
                selects1.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 2 :
                selects2.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 3 :
                selects3.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 4 :
                selects4.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 5 :
                selects5.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 6 :
                selects6.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 7 :
                selects7.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 8 :
                selects8.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 9 :
                selects9.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 10 :
                selects10.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 11 :
                selects11.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 12 :
                selects12.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 13 :
                selects13.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 14 :
                selects14.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            case 15 :
                selects15.get(column).setBackgroundColor(Color.parseColor("#80808fff"));
                break;
            default:
                break;
        }
    }

    private void clearbg() {
        if(row > 15){
            return;
        }
        switch (row){
            case 0 :
                selects0.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 1 :
                selects1.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 2 :
                selects2.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 3 :
                selects3.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 4 :
                selects4.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 5 :
                selects5.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 6 :
                selects6.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 7 :
                selects7.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 8 :
                selects8.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 9 :
                selects9.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 10 :
                selects10.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 11 :
                selects11.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 12 :
                selects12.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 13 :
                selects13.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 14 :
                selects14.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            case 15 :
                selects15.get(column).setBackgroundColor(Color.TRANSPARENT);
                break;
            default:
                break;
        }
//        for (int i = 0; i < selects.size(); i++) {
//            if(i==5||i==6){
//                selects.get(i).setBackgroundColor(Color.parseColor("#ff888888"));
//            }else
//                selects.get(i).setBackgroundColor(Color.TRANSPARENT);
//        }
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
