package com.google.android.exoplayer2.demo;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfParser {
  private String jsonPath ="/sdcard/userdata/conf.json";


//    private String jsonPath = Environment.getExternalStorageDirectory() +"/conf.json";
///
  ConfParser(){

  }
  // /sdcard/userdata/conf.json
  String jsonParser(String path){
    StringBuffer stringBuffer = new StringBuffer();

    try {
      FileInputStream inStream = new FileInputStream(path);
      InputStreamReader inputStreamReader = new InputStreamReader(inStream,"UTF-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line);
      }
      inStream.close();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return stringBuffer.toString();
  }
  public void readConf(ModeConf mode1,ModeConf mode2,ModeConf mode3,ModeConf mode4,ModeConf mode5,ModeConf mode6){
    readConf(mode1,"mode1");
    readConf(mode2,"mode2");
    readConf(mode3,"mode3");
    readConf(mode4,"mode4");
    readConf(mode5,"mode5");
    readConf(mode6,"mode6");
  }

  void readConf(ModeConf conf,String mode){
    String str = jsonParser(jsonPath);
    Log.d("JSON",str);
    try {
      JSONObject jsonObject = new JSONObject(str);
      JSONObject ObjMode1 = jsonObject.getJSONObject(mode);
      conf.nHRTFMode = ObjMode1.getInt("nHRTFMode");
      conf.bCenterChannelElevOn = ObjMode1.getInt("bCenterChannelElevOn");
      JSONArray array = ObjMode1.getJSONArray("afHRTFChannelGain");
      for(int i = 0;i < array.length();i++){
        if(i < 10){
        conf.afHRTFChannelGain[i] = (float) array.getDouble(i);
        }
      }
      conf.bEqActive = ObjMode1.getInt("bEqActive");
      conf.nUseEQMode = ObjMode1.getInt("nUseEQMode");
      array = ObjMode1.getJSONArray("afEQGain");
      for(int i = 0;i < array.length();i++){
        if(i < 30){
          conf.afEQGain[i] = (float) array.getDouble(i);
        }
      }
      conf.bDrcActive = ObjMode1.getInt("bDrcActive");
      conf.nDRCId = ObjMode1.getInt("nDRCId");

      array = ObjMode1.getJSONArray("anDrcCurveParameters");
      for(int i = 0;i < array.length();i++){
        if(i < 10){
          conf.anDrcCurveParameters[i] = array.getInt(i);
        }
      }
      conf.fDRCInputLoudness = (float) ObjMode1.getDouble("fDRCInputLoudness");
      conf.bLimiterActive = ObjMode1.getInt("bLimiterActive");
      conf.bUpmixInvalid = ObjMode1.getInt("bUpmixInvalid");
      conf.nDelayAllPass = ObjMode1.getInt("nDelayAllPass");
      conf.nAddCorLRtoULUR = ObjMode1.getInt("nAddCorLRtoULUR");
      conf.nUlUrMODE = ObjMode1.getInt("nUlUrMODE");
      conf.nStereoWidenDistance = ObjMode1.getInt("nStereoWidenDistance");
      conf.nStereoWidenAngleSrc = ObjMode1.getInt("nStereoWidenAngleSrc");
      conf.nStereoWidenAngleDst = ObjMode1.getInt("nStereoWidenAngleDst");

      conf.fSpeakerToSpeaker = (float) ObjMode1.getDouble("fSpeakerToSpeaker");

      conf.fListenerToSpeaker = (float) ObjMode1.getDouble("fListenerToSpeaker");
      conf.fEarToEar = (float) ObjMode1.getDouble("fEarToEar");
      conf.nLoopNumber = ObjMode1.getInt("nLoopNumber");
      conf.fGain = (float) ObjMode1.getDouble("fGain");
      conf.bCTCInvalid = ObjMode1.getInt("bCTCInvalid");


    } catch (JSONException e) {
      e.printStackTrace();
    }

  }


  void saveJson(String str){
    File file=new File(jsonPath);
    try {
      FileOutputStream fileOutputStream=new FileOutputStream(file);
      OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream,"utf-8");
      BufferedWriter bufferedWriter= new BufferedWriter(outputStreamWriter);
      bufferedWriter.write(str);//将格式化的jsonarray字符串写入文件
      bufferedWriter.flush();//清空缓冲区，强制输出数据
      bufferedWriter.close();//关闭输出流

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveMode4(ModeConf conf){
    String str = jsonParser(jsonPath);
    Log.d("JSON",str);

    try {
      JSONObject jsonObject = new JSONObject(str);
      JSONObject ObjMode1 = jsonObject.getJSONObject("mode4");

      ObjMode1.put("nHRTFMode",conf.nHRTFMode);

      ObjMode1.put("bCenterChannelElevOn",conf.bCenterChannelElevOn);

      JSONArray array = ObjMode1.getJSONArray("afHRTFChannelGain");
      for(int i = 0;i < array.length();i++){
        if(i < 10){
          array.put(i,conf.afHRTFChannelGain[i]);
        }
      }

      ObjMode1.put("bEqActive",conf.bEqActive);

      ObjMode1.put("nUseEQMode",conf.nUseEQMode);

      array = ObjMode1.getJSONArray("afEQGain");
      for(int i = 0;i < array.length();i++){
        if(i < 30){
          array.put(i,(double)conf.afEQGain[i]);
        }
      }
      ObjMode1.put("bDrcActive",conf.bDrcActive);

      ObjMode1.put("nDRCId",conf.nDRCId);

      array = ObjMode1.getJSONArray("anDrcCurveParameters");
      for(int i = 0;i < array.length();i++){
        if(i < 10){
          array.put(i,conf.anDrcCurveParameters[i]);
        }
      }
      ObjMode1.put("fDRCInputLoudness",conf.fDRCInputLoudness);
      ObjMode1.put("bLimiterActive",conf.bLimiterActive);
      ObjMode1.put("bUpmixInvalid",conf.bUpmixInvalid);
      ObjMode1.put("nDelayAllPass",conf.nDelayAllPass);
      ObjMode1.put("nAddCorLRtoULUR",conf.nAddCorLRtoULUR);
      ObjMode1.put("nUlUrMODE",conf.nUlUrMODE);
      ObjMode1.put("nStereoWidenDistance",conf.nStereoWidenDistance);
      ObjMode1.put("nStereoWidenAngleSrc",conf.nStereoWidenAngleSrc);
      ObjMode1.put("nStereoWidenAngleDst",conf.nStereoWidenAngleDst);

      ObjMode1.put("fSpeakerToSpeaker",conf.fSpeakerToSpeaker);

      ObjMode1.put("fListenerToSpeaker",conf.fListenerToSpeaker);

      ObjMode1.put("fEarToEar",conf.fEarToEar);
      ObjMode1.put("nLoopNumber",conf.nLoopNumber);
      ObjMode1.put("fGain",conf.fGain);
      ObjMode1.put("bCTCInvalid",conf.bCTCInvalid);

      saveJson(jsonObject.toString());

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

}
