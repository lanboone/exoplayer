package com.google.android.exoplayer2.demo;

import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class EqAudioProcessorJni {
  private static final String TAG = "EqAudioProcessor";

  private int mInputChannel = 0;
  private int mOutputChannel = 0;
  private int mFramesize = 0;
  private int mSamplerate = 0;

  private boolean isProcessorInit = false;

  public ModeConf mode1Conf = null;
  public ModeConf mode2Conf = null;
  public ModeConf mode3Conf = null;
  public ModeConf mode4Conf = null;
  public ModeConf mode5Conf = null;
  public ModeConf mode6Conf = null;

  public int mMode = 2;

  ByteBuffer mStashBuffer = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder()); //用于缓存残余数据

  //private Long time;

  private static EqAudioProcessorJni instance = null;
  public static EqAudioProcessorJni getInstance()
  {
    if(null == instance)
    {
      instance = new EqAudioProcessorJni();
    }
    return instance;
  }
  private EqAudioProcessorJni(){
    mode1Conf = new ModeConf();
    mode2Conf = new ModeConf();
    mode3Conf = new ModeConf();
    mode4Conf = new ModeConf();
    mode5Conf = new ModeConf();
    mode6Conf = new ModeConf();



    //运行三分钟退出

//    time = System.currentTimeMillis();
  }

  public void initAudioProcessing(int inputChannel,int outputChannel,int samplrate){

//    if(System.currentTimeMillis()-time > 1000 * 60 * 10){
//      return;
//    }

    mInputChannel = inputChannel;
    mOutputChannel = outputChannel;
    mSamplerate = samplrate;
    if(isProcessorInit == true){
      closeProcessing();
      isProcessorInit = false;
    }
    if(mMode == 1){
      up(mode1Conf);
    }else if(mMode == 2){
      up(mode2Conf);
    }else if(mMode == 3){
      up(mode3Conf);
    }else if(mMode == 4){
      up(mode4Conf);
    }else if(mMode == 5){
      up(mode5Conf);
    }else if(mMode == 6){
      up(mode6Conf);
    }

    //setModeNative(2);
    //setCtcValid(0);
    //setMode(2);

  }
  public void initAudioProcessing2(int inputChannel,int outputChannel,int samplrate,int frameSize){
    initAudioProcessing(inputChannel,outputChannel,samplrate,frameSize);
    mInputChannel = inputChannel;
    mFramesize = frameSize;
  }

  public void process(ByteBuffer inputBuffer,ByteBuffer outputBuffer,int framesize) {

//    if(System.currentTimeMillis()-time > 1000 * 60 * 10){
//      return;
//    }


    if(isProcessorInit == false){
      if(mInputChannel != 0 && mOutputChannel != 0&&mSamplerate != 0){
        initAudioProcessing(mInputChannel,mOutputChannel,mSamplerate,framesize);
        mFramesize = framesize;
        isProcessorInit = true;
      }else
      {
        outputBuffer = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());
        return;
      }
    }

    ShortBuffer shortBuffer = inputBuffer.asShortBuffer();
    ShortBuffer shortOutputBuffer = outputBuffer.asShortBuffer();

    int inputSize = shortBuffer.remaining();
    if(mInputChannel * mFramesize == inputSize) {

      short[] input = new short[mInputChannel * mFramesize];
      int offset = 0;
      if(mStashBuffer.remaining() > 0){
        ShortBuffer shortStashBuffer = mStashBuffer.asShortBuffer();
        shortBuffer.get(input, 0, mStashBuffer.remaining());
        offset = mStashBuffer.remaining();
      }
      shortBuffer.get(input, offset, (mInputChannel * mFramesize) - offset);
      short[] output = processInput2(input, inputSize);
      shortOutputBuffer.put(output);
      outputBuffer.limit(mFramesize * 2 * 2);
      int left = shortBuffer.limit() - shortBuffer.position();
      if(left > 0){
        mStashBuffer = ByteBuffer.allocateDirect(left * 2).order(ByteOrder.nativeOrder());
        ShortBuffer shortStashBuffer = mStashBuffer.asShortBuffer();
        shortStashBuffer.put(shortBuffer);
      }else{
        mStashBuffer = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());
      }

    }else if(mInputChannel * mFramesize < inputSize) {
      while((shortBuffer.limit() - shortBuffer.position()) >= mInputChannel * mFramesize){

        short[] input = new short[mInputChannel * mFramesize];

        int offset = 0;
        if(mStashBuffer.remaining() > 0){
          ShortBuffer shortStashBuffer = mStashBuffer.asShortBuffer();
          shortBuffer.get(input, 0, mStashBuffer.remaining());
          offset = mStashBuffer.remaining();
        }

        shortBuffer.get(input, offset, (mInputChannel * mFramesize) - offset);
        short[] output = processInput2(input, mInputChannel * mFramesize);
        shortOutputBuffer.put(output);
      }

      outputBuffer.limit(shortOutputBuffer.position() * 2);
      int left = shortBuffer.limit() - shortBuffer.position();
      if(left > 0){
        mStashBuffer = ByteBuffer.allocateDirect(left * 2).order(ByteOrder.nativeOrder());
        ShortBuffer shortStashBuffer = mStashBuffer.asShortBuffer();

        shortStashBuffer.put(shortBuffer);
      }else{
        mStashBuffer = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());
      }
    }
    else if(mInputChannel * mFramesize > inputSize) {
      mStashBuffer = ByteBuffer.allocateDirect(inputSize * 2).order(ByteOrder.nativeOrder());
      ShortBuffer shortStashBuffer = mStashBuffer.asShortBuffer();
      shortStashBuffer.put(shortBuffer);
    }
  }

  public void setMode(int mode){
//    if(System.currentTimeMillis()-time > 1000 * 60 * 10 ){
//      return;
//    }
    mMode = mode;

    if(mode == 1){
      up(mode1Conf);
    }else if(mode == 2){
      up(mode2Conf);
    }else if(mode == 3){
      up(mode3Conf);
    }else if(mode == 4){
      up(mode4Conf);
    }else if(mode == 5){
      up(mode5Conf);
    }else if(mode == 6){
      up(mode6Conf);
    }

    setModeNative(mode);

    //isProcessorInit = false;
  }

  public void up(ModeConf conf){
    updateConfNative(conf.nHRTFMode,conf.bCenterChannelElevOn,conf.afHRTFChannelGain,
                        conf.bEqActive,conf.nUseEQMode,conf.afEQGain,conf.bDrcActive,conf.nDRCId,
        conf.anDrcCurveParameters,conf.fDRCInputLoudness,conf.bLimiterActive,conf.bUpmixInvalid,conf.nDelayAllPass,
        conf.nAddCorLRtoULUR,conf.nUlUrMODE,conf.nStereoWidenDistance,conf.nStereoWidenAngleSrc,
        conf.nStereoWidenAngleDst,conf.fSpeakerToSpeaker,conf.fListenerToSpeaker,conf.fEarToEar,conf.nLoopNumber,conf.fGain,
        conf.bCTCInvalid);
  }
  public void CtcValid(int isValid){
    setCtcValid(isValid);
  }

  public void close(){
    closeProcessing();
  }

  public void updateConf(){
//    mode1Conf = new ModeConf();
//    mode2Conf = new ModeConf();
//    mode3Conf = new ModeConf();
//    mode4Conf = new ModeConf();

    ConfParser parser = new ConfParser();
    parser.readConf(mode1Conf,mode2Conf,mode3Conf,mode4Conf,mode5Conf,mode6Conf);
  }

  public void testFunction(){
    test();
    initAudioProcessing(6,2,48000,1024);


    short[] input = new short[1024 * 6];

    for(int i = 0;i < 6; i++ ){
      for(int j = 0; j < 1024; j++){
        input[j * 6 + i] = (short)i;
      }
    }


    short[] output = processInput2(input,1024 * 6);
    Log.d(TAG,"output len:"+output.length);
    Log.d(TAG, "output:"+output[1000]);

  }

  public native void initAudioProcessing(int inputChannel,int outputChannel,int samplrate,int frameSize);

  private native short[] processInput2(short[] inputBuffer,int inputSize);
  private native void closeProcessing();

  private native void setModeNative(int mode);

  private native void setCtcValid(int isValid);
  public native void test();

  private native void updateConfNative(int nHRTFMode,
                                  int	bCenterChannelElevOn,
                                  float[] afHRTFChannelGain,
                                  int bEqActive,
                                  int   nUseEQMode,
                                  float[] afEQGain,
                                  int   bDrcActive,
                                  int   nDRCId,
                                  int[]   anDrcCurveParameters,
                                  float fDRCInputLoudness,
                                  int   bLimiterActive,
                                  int bUpmixInvalid,
                                  int nDelayAllPass,
                                  int nAddCorLRtoULUR,
                                  int nUlUrMODE,
                                  int nStereoWidenDistance,
                                  int nStereoWidenAngleSrc,
                                  int nStereoWidenAngleDst,
                                  float fSpeakerToSpeaker,
                                  float fListenerToSpeaker,
                                  float fEarToEar,
                                  int	nLoopNumber,
                                  float fGain,
                                  int   bCTCInvalid
  );

  static {
    System.loadLibrary("demo");
    getInstance();
  }
}
