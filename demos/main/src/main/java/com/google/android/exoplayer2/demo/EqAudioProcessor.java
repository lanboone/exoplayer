package com.google.android.exoplayer2.demo;

import android.util.Log;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.audio.AudioProcessor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class EqAudioProcessor implements AudioProcessor {

  private static final String TAG = "EqAudioProcessor";
  private boolean isActive = false;
  private ByteBuffer outputBuffer;


  private boolean inputEnded = false;
  private AudioFormat mAudioFormat = null;
  private AudioFormat mInputAudioFormat = null;

  private boolean isEnable = false;

  private int outputChannel = 2;//设置输出声道数

  public EqAudioProcessor(){
    outputBuffer = EMPTY_BUFFER;
  }

  public void enableAudioProcess(){
    isEnable = true;
  }
  public void disableAudioProcess(){
    isEnable = false;
  }
  @Override
  public AudioFormat configure(AudioFormat inputAudioFormat) throws UnhandledAudioFormatException {
    Log.d(TAG,"configure");
    if (inputAudioFormat.encoding != C.ENCODING_PCM_16BIT) {
      throw new UnhandledAudioFormatException(inputAudioFormat);
    }
    Log.d(TAG,inputAudioFormat.toString());
    mInputAudioFormat = inputAudioFormat;
    //EqAudioProcessorJni.getInstance().initAudioProcessing2(inputAudioFormat.channelCount,outputChannel,inputAudioFormat.sampleRate,1024);
    EqAudioProcessorJni.getInstance().initAudioProcessing(inputAudioFormat.channelCount,outputChannel,inputAudioFormat.sampleRate);

    mAudioFormat = new AudioFormat(
        inputAudioFormat.sampleRate, outputChannel, C.ENCODING_PCM_16BIT);

    isActive = true;
    return mAudioFormat;
  }

  @Override
  public boolean isActive() {
    Log.d(TAG,"isActive");
    return isActive&&isEnable;
    //return true;
  }

  @Override
  public void queueInput(ByteBuffer inputBuffer) {
    if (!inputBuffer.hasRemaining()) {
      return;
    }
    int inputSize = inputBuffer.remaining();

    int frameSize = (inputSize/2)/mInputAudioFormat.channelCount;
    if(frameSize == 1024 || frameSize == 1536||frameSize == 1152){
      ByteBuffer output = ByteBuffer.allocateDirect(frameSize * outputChannel * 2).order(ByteOrder.nativeOrder());
      EqAudioProcessorJni.getInstance().process(inputBuffer,output,frameSize);
      outputBuffer = output;
    }else
    {
      outputBuffer = EMPTY_BUFFER;
      Log.d(TAG,"don't support framesize = "+frameSize);
    }
//    ByteBuffer output = ByteBuffer.allocateDirect(1024*2*2*10).order(ByteOrder.nativeOrder());
//    EqAudioProcessorJni.getInstance().process(inputBuffer,output);
//    outputBuffer = output;
    inputBuffer.position(inputBuffer.position() + inputSize);
  }

  @Override
  public void queueEndOfStream() {
    Log.d(TAG,"queueEndOfStream");
    outputBuffer = EMPTY_BUFFER;
    inputEnded = true;
    //EqAudioProcessorJni.getInstance().close();
  }

  @Override
  public ByteBuffer getOutput() {
    ByteBuffer buffer = outputBuffer;
    outputBuffer = EMPTY_BUFFER;
    return buffer;
  }

  @Override
  public boolean isEnded() {
    Log.d(TAG,"isEnded:"+inputEnded);
    return inputEnded;
  }

  @Override
  public void flush() {
    Log.d(TAG,"flush");
    outputBuffer = EMPTY_BUFFER;
    inputEnded = false;
  }

  @Override
  public void reset() {
    Log.d(TAG,"reset");
    flush();
  }
}
