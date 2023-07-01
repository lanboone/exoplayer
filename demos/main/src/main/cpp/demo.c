#include <jni.h>
#include <android/log.h>
#include <malloc.h>
#include "draAudioPostProcessingLib.h"
typedef struct processor_context {
  JavaVM  *javaVM;
  jclass   jniClz;
  jobject  jniObj;
} ProcessorContext;
ProcessorContext g_ctx;

int audioMode = 2;  //模式设置

int isCtcValid = 0;


#define DRA_FRAME_LEN			1024 // 1152 // 1536   //

//////////////////////////////////////////////////////////////////////////
//int   nHRTFMode = 2;
//int   bCenterChannelElevOn = 0;
//float afHRTFChannelGain[10] = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f};
//
//int bEqActive = 1;
//int nUseEQMode = 2;
//float afEQGain10[EQ_NUM_10] = {3.5f, 3.5f, 1.5f, -3.07f, -5.4f, -1.8f, -0.2f, 3.0f, 3.5f, 3.7f};
//float afEQGain30[EQ_NUM_30] = {3.5f, 3.5f, 1.5f, -3.07f, -5.4f, -1.8f, -0.2f, 3.0f, 3.5f, 3.7f,
//                               3.5f, 3.5f, 1.5f, -3.07f, -5.4f, -1.8f, -0.2f, 3.0f, 3.5f, 3.7f,
//                               3.5f, 3.5f, 1.5f, -3.07f, -5.4f, -1.8f, -0.2f, 3.0f, 3.5f, 3.7f};
//
//int   bDrcActive = 1;
//int   nDRCId = 0;
//int   anDrcCurveParameters[DRC_NODE_NUM * 2] = {-22, 6, -10, 0, 10, 0, 20, -5, 40, -24 };
//float fDRCInputLoudness = -24.0f;
//
//int bLimitActive = 1;
//
//int bUpmixInvalid = 0;
//int nDelayAllPass = 5;
//int nAddCorLRtoULUR = 1;
//int nUlUrMODE = 1;
//
//int nStereoWidenDistance = 130;	// support: 50, 75, 100, 130, 160
//int nStereoWidenAngleSrc = 10;		// support: 10
//int nStereoWidenAngleDst = 45;		// support: 30, 45, 60, 75
//////////////////////////////////////////////////////////////////////////


//mode 1
float afHRTFChannelGainMode1[10] = {6.0f, 6.0f, 8.0f, 0.0f, 10.0f, 10.0f, 12.0f, 12.0f, 12.0f, 12.0f};
float afEQGain10Mode1[EQ_NUM_10] = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 4.0f, 5.0f, 7.0f, 8.0f};
int   anDrcCurveParametersMode1[DRC_NODE_NUM * 2] = {-12, 12, 0,  10,  5,  5,  15, 3,  35, -18};

//mode 2

float afHRTFChannelGainMode2[10] = {10.0f,  10.0f , 8.0f,  0.0f,  10.0f,  10.0f,  12.0f, 12.0f, 12.0f, 12.0f};
float afEQGain10Mode2[EQ_NUM_10] = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 4.0f, 5.0f, 7.0f, 8.0f};
int   anDrcCurveParametersMode2[DRC_NODE_NUM * 2] = {-12, 12, 0,  10,  5,  5,  15, 3,  35, -18};

//mode 3

float afHRTFChannelGainMode3[10] = {8.0f,  8.0f,  12.0f,   0.0f,  10.0f,  10.0f,  12.0f, 12.0f, 12.0f, 12.0f};
float afEQGain10Mode3[EQ_NUM_10] = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 4.0f, 5.0f, 7.0f, 8.0f};
int   anDrcCurveParametersMode3[DRC_NODE_NUM * 2] = {-12, 12, 0,  10,  5,  5,  15, 3,  35, -18};




DRA_AUDIO_POST_PROCESSING_PARAM_INPUT draAudioPostProcessingParamInput;
H_DRA_AUDIO_POST_PROCESSING			  hDraAudioPostProcessing = NULL;



float **ppInBuffer          = NULL;
float **ppOutBuffer         = NULL;


unsigned int nInChannels = 0;
unsigned int nOutChannels = 0;
unsigned int nSampleRate = 0;
unsigned int nFrameSize = 0;

static const char* kTAG = "EqAudioProcessor";
void closeAudioProcessor(void);
void setModeParam(int mode);

#define LOGI(...) \
  ((void)__android_log_print(ANDROID_LOG_INFO, kTAG, __VA_ARGS__))

#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, kTAG, __VA_ARGS__))

float short2float(short data){
  float coeff = 1.0*1.0/32768;
  return data * coeff;
}

short float2short(float data){
  float coeff = 32768.0;
  short ret = (short)(data * coeff);
  return ret;
}

JNIEXPORT void JNICALL
Java_com_google_android_exoplayer2_demo_EqAudioProcessorJni_test(JNIEnv *env, jobject thiz) {

  jclass clz = (*env)->GetObjectClass(env, thiz);
  g_ctx.jniClz = (*env)->NewGlobalRef(env, clz);
  g_ctx.jniObj = (*env)->NewGlobalRef(env, thiz);
  LOGI("Test Function");
}

JNIEXPORT void JNICALL
Java_com_google_android_exoplayer2_demo_EqAudioProcessorJni_initAudioProcessing(JNIEnv *env,
                                                                                jobject thiz,
                                                                                jint input_channel,
                                                                                jint output_channel,
                                                                                jint samplrate,
                                                                                jint frame_size) {
  LOGI("initAudioProcessing");

  if(hDraAudioPostProcessing != NULL){
    closeAudioProcessor();
  }

//////////////////////////////////////////////////////////////////////////
//  draAudioPostProcessingParamInput.nHRTFMode = nHRTFMode;
//  draAudioPostProcessingParamInput.bCenterChannelElevOn = bCenterChannelElevOn;
//
//  for (int ch = 0; ch < 10; ch++)
//  {
//    draAudioPostProcessingParamInput.afHRTFChannelGain[ch] = afHRTFChannelGain[ch];
//  }
//
//  draAudioPostProcessingParamInput.bEqActive = bEqActive;
//  draAudioPostProcessingParamInput.nUseEQMode = nUseEQMode;
//
//  switch (draAudioPostProcessingParamInput.nUseEQMode)
//  {
//    case 0:
//      for (int i = 0; i < EQ_NUM_30; i++)
//      {
//        draAudioPostProcessingParamInput.afEQGain[i] = 0.0f;
//      }
//      break;
//
//    case 1:
//      for (int i = 0; i < EQ_NUM_10; i++)
//      {
//        draAudioPostProcessingParamInput.afEQGain[i] = afEQGain10[i];
//      }
//
//      for ( int i= 10; i < EQ_NUM_30; i++)
//      {
//        draAudioPostProcessingParamInput.afEQGain[i] = 0.0f;
//      }
//      break;
//
//    case 2:
//      for (int i = 0; i < EQ_NUM_30; i++)
//      {
//        draAudioPostProcessingParamInput.afEQGain[i] = afEQGain30[i];
//      }
//      break;
//    default:
//      break;
//  }
//
//  draAudioPostProcessingParamInput.bDrcActive = bDrcActive;
//  draAudioPostProcessingParamInput.nDRCId = nDRCId;
//
//  for (int i = 0; i < DRC_NODE_NUM * 2; i++)
//  {
//    draAudioPostProcessingParamInput.anDrcCurveParameters[i] = anDrcCurveParameters[i];
//  }
//
//  draAudioPostProcessingParamInput.fDRCInputLoudness = fDRCInputLoudness;
//
//  bLimitActive = bEqActive | bDrcActive;
//  draAudioPostProcessingParamInput.bLimiterActive = bLimitActive;
//
//  draAudioPostProcessingParamInput.nDelayAllPass = nDelayAllPass;
//  draAudioPostProcessingParamInput.nAddCorLRtoULUR = nAddCorLRtoULUR;
//  draAudioPostProcessingParamInput.nUlUrMODE = nUlUrMODE;
//
//  draAudioPostProcessingParamInput.bUpmixInvalid = bUpmixInvalid;
//  draAudioPostProcessingParamInput.nStereoWidenDistance = nStereoWidenDistance;
//  draAudioPostProcessingParamInput.nStereoWidenAngleSrc = nStereoWidenAngleSrc;
//  draAudioPostProcessingParamInput.nStereoWidenAngleDst = nStereoWidenAngleDst;
////////////////////////////////////////////////////////////////////////////

  //setModeParam(audioMode);
  InitDraAudioPostProcessing(&hDraAudioPostProcessing,
                             &draAudioPostProcessingParamInput,
                             input_channel,
                             output_channel,
                             samplrate,
                             frame_size);


  nInChannels = input_channel;
  nOutChannels = output_channel;
  nSampleRate = samplrate;
  nFrameSize = frame_size;
  ppInBuffer = (float **)malloc(input_channel*sizeof(float *));
  ppOutBuffer = (float **)malloc(output_channel*sizeof(float *));
  for (int ch = 0; ch < input_channel; ch++)
  {
    ppInBuffer[ch] = (float *)malloc(frame_size*sizeof(float));
  }

  for (int ch = 0; ch < output_channel; ch++)
  {
    ppOutBuffer[ch] = (float *)malloc(frame_size*sizeof(float));
  }

}
JNIEXPORT void JNICALL
Java_com_google_android_exoplayer2_demo_EqAudioProcessorJni_closeProcessing(JNIEnv *env,
                                                                            jobject thiz) {

  closeAudioProcessor();
}

void closeAudioProcessor(void){
  if(hDraAudioPostProcessing == NULL){
    return;
  }
  CloseDraAudioPostProcessing(&hDraAudioPostProcessing);
  hDraAudioPostProcessing = NULL;

  for (int ch = 0; ch < nInChannels; ch++)
  {
    if(ppInBuffer[ch] != NULL){
      free(ppInBuffer[ch]);
    }
  }

  for (int ch = 0; ch < nOutChannels; ch++)
  {
    if(ppOutBuffer[ch] != NULL){
      free(ppOutBuffer[ch]);
    }
  }
  if(ppInBuffer != NULL){
    free(ppInBuffer);
  }
  if(ppOutBuffer != NULL){
    free(ppOutBuffer);
  }
}

JNIEXPORT jshortArray JNICALL
Java_com_google_android_exoplayer2_demo_EqAudioProcessorJni_processInput2(JNIEnv *env,
                                                                          jobject thiz,
                                                                          jshortArray input_buffer,
                                                                          jint input_size) {

  //LOGE("input size = %d",input_size);
  //LOGE("processor running");
  if(input_size > nInChannels * nFrameSize){
    LOGE("输入超过处理能力");
  }
  short * pInputBuffer = (short *) malloc(sizeof(short) * input_size);
  (*env)->GetShortArrayRegion(env,input_buffer,0,input_size,(jshort *) pInputBuffer);

//  if((input_size/nInChannels) < nFrameSize){
//    for(int i = 0;i < nInChannels; i++ ){
//      for(int j = 0; j < nFrameSize; j++){
//        ppInBuffer[i][j] = 0;
//        ppOutBuffer[i][j] = 0;
//      }
//    }
//  }

  for(int i = 0;i < nInChannels; i++ ){
    for(int j = 0; j < input_size / nInChannels; j++){
      ppInBuffer[i][j] = short2float(pInputBuffer[j * nInChannels + i]);
    }
  }

  MakeDraAudioPostProcessing(hDraAudioPostProcessing,ppInBuffer,ppOutBuffer,nFrameSize);

  jshort * pOutputBuffer = (short *) malloc(sizeof(short) * nFrameSize*nOutChannels);
  //ppOutBuffer -> pOutputBuffer
  for(int i = 0;i < nOutChannels; i++ ){
    for(int j = 0; j < nFrameSize; j++){
      pOutputBuffer[j * nOutChannels + i] = float2short(ppOutBuffer[i][j]);
    }
  }
  jshortArray output;
  output = (*env)->NewShortArray(env,nFrameSize * nOutChannels);
  (*env)->SetShortArrayRegion(env,output,0,nFrameSize * nOutChannels,pOutputBuffer);

  if(pInputBuffer != NULL)
  {
    free(pInputBuffer);
  }
  if(pOutputBuffer != NULL){
    free(pOutputBuffer);
  }
  return output;
}

void setModeParam(int mode){
  if(mode == 1){
    draAudioPostProcessingParamInput.nHRTFMode = 2;
    draAudioPostProcessingParamInput.bCenterChannelElevOn = 0;
    for(int i = 0;i < 10 ;i++){
      draAudioPostProcessingParamInput.afHRTFChannelGain[i] = afHRTFChannelGainMode1[i];
    }
    draAudioPostProcessingParamInput.bEqActive = 1;
    draAudioPostProcessingParamInput.nUseEQMode = 1;

    for(int i = 0;i < 30 ;i++){
      draAudioPostProcessingParamInput.afEQGain[i] = 0.0f;
    }
    for(int i = 0;i < 10 ;i++){
      draAudioPostProcessingParamInput.afEQGain[i] = afEQGain10Mode1[i];
    }
    draAudioPostProcessingParamInput.bDrcActive = 1;
    draAudioPostProcessingParamInput.nDRCId = 1;

    for (int i = 0; i < DRC_NODE_NUM * 2; i++)
    {
      draAudioPostProcessingParamInput.anDrcCurveParameters[i] = anDrcCurveParametersMode1[i];
    }
    draAudioPostProcessingParamInput.fDRCInputLoudness = -10.0f;
    draAudioPostProcessingParamInput.bLimiterActive = 1;

    draAudioPostProcessingParamInput.bUpmixInvalid = 0;	 // 0->用upmix，1->不用upmix，用于“无效果模式”
    draAudioPostProcessingParamInput.nDelayAllPass = 15;     // upmix参数
    draAudioPostProcessingParamInput.nAddCorLRtoULUR = 0;   // upmix参数，必须是0，1或者2
    draAudioPostProcessingParamInput.nUlUrMODE = 1;      	 // upmix参数，必须是0或者1

    draAudioPostProcessingParamInput.nStereoWidenDistance = 130;  // 左右声道扩展参数，必须是：50, 75, 100, 130 或者 160
    draAudioPostProcessingParamInput.nStereoWidenAngleSrc = 10;  // 左右声道扩展参数，必须是：10
    draAudioPostProcessingParamInput.nStereoWidenAngleDst = 45;  // 左右声道扩展参数，必须是：30, 45, 60 或者 75

    draAudioPostProcessingParamInput.fSpeakerToSpeaker = 0.8f;		//扬声器间距离（单位：米）
    draAudioPostProcessingParamInput.fListenerToSpeaker = 1.5f;		//听者距离电视的距离（单位：米）
    draAudioPostProcessingParamInput.fEarToEar = 0.215f;				//听者双耳距离（单位：米）
    draAudioPostProcessingParamInput.nLoopNumber = 50;				//必须小于300
    draAudioPostProcessingParamInput.fGain = 0.5;					//CTC输出增益调整（线性，非dB）
    draAudioPostProcessingParamInput.bCTCInvalid = isCtcValid;				// 0->用CTC，1->不用CTC，用于“无效果模式”
  }


  if(mode == 2){
    draAudioPostProcessingParamInput.nHRTFMode = 2;
    draAudioPostProcessingParamInput.bCenterChannelElevOn = 0;
    for(int i = 0;i < 10 ;i++){
      draAudioPostProcessingParamInput.afHRTFChannelGain[i] = afHRTFChannelGainMode2[i];
    }
    draAudioPostProcessingParamInput.bEqActive = 1;
    draAudioPostProcessingParamInput.nUseEQMode = 1;

    for(int i = 0;i < 30 ;i++){
      draAudioPostProcessingParamInput.afEQGain[i] = 0.0f;
    }
    for(int i = 0;i < 10 ;i++){
      draAudioPostProcessingParamInput.afEQGain[i] = afEQGain10Mode2[i];
    }
    draAudioPostProcessingParamInput.bDrcActive = 1;
    draAudioPostProcessingParamInput.nDRCId = 1;

    for (int i = 0; i < DRC_NODE_NUM * 2; i++)
    {
      draAudioPostProcessingParamInput.anDrcCurveParameters[i] = anDrcCurveParametersMode2[i];
    }
    draAudioPostProcessingParamInput.fDRCInputLoudness = -10.0f;
    draAudioPostProcessingParamInput.bLimiterActive = 1;

    draAudioPostProcessingParamInput.bUpmixInvalid = 0;	 // 0->用upmix，1->不用upmix，用于“无效果模式”
    draAudioPostProcessingParamInput.nDelayAllPass = 15;     // upmix参数
    draAudioPostProcessingParamInput.nAddCorLRtoULUR = 0;   // upmix参数，必须是0，1或者2
    draAudioPostProcessingParamInput.nUlUrMODE = 1;      	 // upmix参数，必须是0或者1

    draAudioPostProcessingParamInput.nStereoWidenDistance = 130;  // 左右声道扩展参数，必须是：50, 75, 100, 130 或者 160
    draAudioPostProcessingParamInput.nStereoWidenAngleSrc = 10;  // 左右声道扩展参数，必须是：10
    draAudioPostProcessingParamInput.nStereoWidenAngleDst = 45;  // 左右声道扩展参数，必须是：30, 45, 60 或者 75

    draAudioPostProcessingParamInput.fSpeakerToSpeaker = 0.8f;		//扬声器间距离（单位：米）
    draAudioPostProcessingParamInput.fListenerToSpeaker = 1.5f;		//听者距离电视的距离（单位：米）
    draAudioPostProcessingParamInput.fEarToEar = 0.215f;				//听者双耳距离（单位：米）
    draAudioPostProcessingParamInput.nLoopNumber = 50;				//必须小于300
    draAudioPostProcessingParamInput.fGain = 0.4;					//CTC输出增益调整（线性，非dB）
    draAudioPostProcessingParamInput.bCTCInvalid = isCtcValid;				// 0->用CTC，1->不用CTC，用于“无效果模式”
  }


  if(mode == 3){
    draAudioPostProcessingParamInput.nHRTFMode = 2;
    draAudioPostProcessingParamInput.bCenterChannelElevOn = 0;
    for(int i = 0;i < 10 ;i++){
      draAudioPostProcessingParamInput.afHRTFChannelGain[i] = afHRTFChannelGainMode3[i];
    }
    draAudioPostProcessingParamInput.bEqActive = 1;
    draAudioPostProcessingParamInput.nUseEQMode = 1;

    for(int i = 0;i < 30 ;i++){
      draAudioPostProcessingParamInput.afEQGain[i] = 0.0f;
    }
    for(int i = 0;i < 10 ;i++){
      draAudioPostProcessingParamInput.afEQGain[i] = afEQGain10Mode3[i];
    }
    draAudioPostProcessingParamInput.bDrcActive = 1;
    draAudioPostProcessingParamInput.nDRCId = 1;

    for (int i = 0; i < DRC_NODE_NUM * 2; i++)
    {
      draAudioPostProcessingParamInput.anDrcCurveParameters[i] = anDrcCurveParametersMode3[i];
    }
    draAudioPostProcessingParamInput.fDRCInputLoudness = -10.0f;
    draAudioPostProcessingParamInput.bLimiterActive = 1;

    draAudioPostProcessingParamInput.bUpmixInvalid = 0;	 // 0->用upmix，1->不用upmix，用于“无效果模式”
    draAudioPostProcessingParamInput.nDelayAllPass = 15;     // upmix参数
    draAudioPostProcessingParamInput.nAddCorLRtoULUR = 0;   // upmix参数，必须是0，1或者2
    draAudioPostProcessingParamInput.nUlUrMODE = 1;      	 // upmix参数，必须是0或者1

    draAudioPostProcessingParamInput.nStereoWidenDistance = 130;  // 左右声道扩展参数，必须是：50, 75, 100, 130 或者 160
    draAudioPostProcessingParamInput.nStereoWidenAngleSrc = 10;  // 左右声道扩展参数，必须是：10
    draAudioPostProcessingParamInput.nStereoWidenAngleDst = 45;  // 左右声道扩展参数，必须是：30, 45, 60 或者 75

    draAudioPostProcessingParamInput.fSpeakerToSpeaker = 0.8f;		//扬声器间距离（单位：米）
    draAudioPostProcessingParamInput.fListenerToSpeaker = 1.5f;		//听者距离电视的距离（单位：米）
    draAudioPostProcessingParamInput.fEarToEar = 0.215f;				//听者双耳距离（单位：米）
    draAudioPostProcessingParamInput.nLoopNumber = 50;				//必须小于300
    draAudioPostProcessingParamInput.fGain = 0.3;					//CTC输出增益调整（线性，非dB）
    draAudioPostProcessingParamInput.bCTCInvalid = isCtcValid;				// 0->用CTC，1->不用CTC，用于“无效果模式”
  }

  if(mode == 4){
    draAudioPostProcessingParamInput.nHRTFMode = 2;
    draAudioPostProcessingParamInput.bCenterChannelElevOn = 0;
    for(int i = 0;i < 10 ;i++){
      draAudioPostProcessingParamInput.afHRTFChannelGain[i] = afHRTFChannelGainMode3[i];
    }
    draAudioPostProcessingParamInput.bEqActive = 1;
    draAudioPostProcessingParamInput.nUseEQMode = 1;

    for(int i = 0;i < 30 ;i++){
      draAudioPostProcessingParamInput.afEQGain[i] = 0.0f;
    }
    for(int i = 0;i < 10 ;i++){
      draAudioPostProcessingParamInput.afEQGain[i] = afEQGain10Mode3[i];
    }
    draAudioPostProcessingParamInput.bDrcActive = 1;
    draAudioPostProcessingParamInput.nDRCId = 1;

    for (int i = 0; i < DRC_NODE_NUM * 2; i++)
    {
      draAudioPostProcessingParamInput.anDrcCurveParameters[i] = anDrcCurveParametersMode3[i];
    }
    draAudioPostProcessingParamInput.fDRCInputLoudness = -10.0f;
    draAudioPostProcessingParamInput.bLimiterActive = 1;

    draAudioPostProcessingParamInput.bUpmixInvalid = 0;	 // 0->用upmix，1->不用upmix，用于“无效果模式”
    draAudioPostProcessingParamInput.nDelayAllPass = 15;     // upmix参数
    draAudioPostProcessingParamInput.nAddCorLRtoULUR = 0;   // upmix参数，必须是0，1或者2
    draAudioPostProcessingParamInput.nUlUrMODE = 1;      	 // upmix参数，必须是0或者1

    draAudioPostProcessingParamInput.nStereoWidenDistance = 130;  // 左右声道扩展参数，必须是：50, 75, 100, 130 或者 160
    draAudioPostProcessingParamInput.nStereoWidenAngleSrc = 10;  // 左右声道扩展参数，必须是：10
    draAudioPostProcessingParamInput.nStereoWidenAngleDst = 45;  // 左右声道扩展参数，必须是：30, 45, 60 或者 75

    draAudioPostProcessingParamInput.fSpeakerToSpeaker = 0.15f;		//扬声器间距离（单位：米）
    draAudioPostProcessingParamInput.fListenerToSpeaker = 0.4f;		//听者距离电视的距离（单位：米）
    draAudioPostProcessingParamInput.fEarToEar = 0.215f;				//听者双耳距离（单位：米）
    draAudioPostProcessingParamInput.nLoopNumber = 50;				//必须小于300
    draAudioPostProcessingParamInput.fGain = 0.8;					//CTC输出增益调整（线性，非dB）
    draAudioPostProcessingParamInput.bCTCInvalid = isCtcValid;				// 0->用CTC，1->不用CTC，用于“无效果模式”
  }
}


JNIEXPORT void JNICALL
Java_com_google_android_exoplayer2_demo_EqAudioProcessorJni_setModeNative(JNIEnv *env,
                                                                          jobject thiz,
                                                                          jint mode) {
  LOGI("setModeNative");

  //audioMode = mode;

  //setModeParam(audioMode);
//  if(hDraAudioPostProcessing == NULL)
//    return;

//  int eqMode = mode - 1;
//  if(eqMode == 3){
//    eqMode = 2;
//  }


  //////////////////////////////////////////////////////////////////////////
//  draAudioPostProcessingParamInput.nHRTFMode = nHRTFMode;
//  draAudioPostProcessingParamInput.bCenterChannelElevOn = bCenterChannelElevOn;

//  for (int ch = 0; ch < 10; ch++)
//  {
//    draAudioPostProcessingParamInput.afHRTFChannelGain[ch] = afHRTFChannelGain[ch];
//  }

  //draAudioPostProcessingParamInput.bEqActive = bEqActive;
  //draAudioPostProcessingParamInput.nUseEQMode = eqMode; //设置模式

//  switch (draAudioPostProcessingParamInput.nUseEQMode)
//  {
//    case 0:
//      for (int i = 0; i < EQ_NUM_30; i++)
//      {
//        draAudioPostProcessingParamInput.afEQGain[i] = 0.0f;
//      }
//      break;
//
//    case 1:
//      for (int i = 0; i < EQ_NUM_10; i++)
//      {
//        draAudioPostProcessingParamInput.afEQGain[i] = afEQGain10[i];
//      }
//
//      for ( int i= 10; i < EQ_NUM_30; i++)
//      {
//        draAudioPostProcessingParamInput.afEQGain[i] = 0.0f;
//      }
//      break;
//
//    case 2:
//      for (int i = 0; i < EQ_NUM_30; i++)
//      {
//        draAudioPostProcessingParamInput.afEQGain[i] = afEQGain30[i];
//      }
//      break;
//
//    default:
//
//      break;
//  }

//  draAudioPostProcessingParamInput.bDrcActive = bDrcActive;
//  draAudioPostProcessingParamInput.nDRCId = nDRCId;
//
//  for (int i = 0; i < DRC_NODE_NUM * 2; i++)
//  {
//    draAudioPostProcessingParamInput.anDrcCurveParameters[i] = anDrcCurveParameters[i];
//  }
//
//  draAudioPostProcessingParamInput.fDRCInputLoudness = fDRCInputLoudness;
//
//  bLimitActive = bEqActive | bDrcActive;
//  draAudioPostProcessingParamInput.bLimiterActive = bLimitActive;
//
//  draAudioPostProcessingParamInput.nDelayAllPass = nDelayAllPass;
//  draAudioPostProcessingParamInput.nAddCorLRtoULUR = nAddCorLRtoULUR;
//  draAudioPostProcessingParamInput.nUlUrMODE = nUlUrMODE;
//
//  draAudioPostProcessingParamInput.bUpmixInvalid = bUpmixInvalid;
//  draAudioPostProcessingParamInput.nStereoWidenDistance = nStereoWidenDistance;
//  draAudioPostProcessingParamInput.nStereoWidenAngleSrc = nStereoWidenAngleSrc;
//  draAudioPostProcessingParamInput.nStereoWidenAngleDst = nStereoWidenAngleDst;
//////////////////////////////////////////////////////////////////////////

  //LOGI("setModeNative2");
  if(hDraAudioPostProcessing != NULL)
    ResetDraAudioPostProcessing(hDraAudioPostProcessing,&draAudioPostProcessingParamInput);

//  for (int ch = 0; ch < nInChannels; ch++)
//  {
//    if(ppInBuffer[ch] != NULL){
//      free(ppInBuffer[ch]);
//    }
//  }
//
//  for (int ch = 0; ch < nOutChannels; ch++)
//  {
//    if(ppOutBuffer[ch] != NULL){
//      free(ppOutBuffer[ch]);
//    }
//  }
//  if(ppInBuffer != NULL){
//    free(ppInBuffer);
//  }
//  if(ppOutBuffer != NULL){
//    free(ppOutBuffer);
//  }

}
JNIEXPORT void JNICALL
Java_com_google_android_exoplayer2_demo_EqAudioProcessorJni_setCtcValid(JNIEnv *env,
                                                                        jobject thiz,
                                                                        jint is_valid) {

}
JNIEXPORT void JNICALL
Java_com_google_android_exoplayer2_demo_EqAudioProcessorJni_updateConfNative(JNIEnv *env,
                                                                             jobject thiz,
                                                                             jint n_hrtfmode,
                                                                             jint b_center_channel_elev_on,
                                                                             jfloatArray af_hrtfchannel_gain,
                                                                             jint b_eq_active,
                                                                             jint n_use_eqmode,
                                                                             jfloatArray af_eqgain,
                                                                             jint b_drc_active,
                                                                             jint n_drcid,
                                                                             jintArray an_drc_curve_parameters,
                                                                             jfloat f_drcinput_loudness,
                                                                             jint b_limiter_active,
                                                                             jint b_upmix_invalid,
                                                                             jint n_delay_all_pass,
                                                                             jint n_add_cor_lrto_ulur,
                                                                             jint n_ul_ur_mode,
                                                                             jint n_stereo_widen_distance,
                                                                             jint n_stereo_widen_angle_src,
                                                                             jint n_stereo_widen_angle_dst,
                                                                             jfloat f_speaker_to_speaker,
                                                                             jfloat f_listener_to_speaker,
                                                                             jfloat f_ear_to_ear,
                                                                             jint n_loop_number,
                                                                             jfloat f_gain,
                                                                             jint b_ctcinvalid) {
  // TODO: implement updateConfNative()
  draAudioPostProcessingParamInput.nHRTFMode = n_hrtfmode;
  draAudioPostProcessingParamInput.bCenterChannelElevOn = b_center_channel_elev_on;

  (*env)->GetFloatArrayRegion(env,af_hrtfchannel_gain,0,10,(jfloat *) draAudioPostProcessingParamInput.afHRTFChannelGain);

  draAudioPostProcessingParamInput.bEqActive = b_eq_active;
  draAudioPostProcessingParamInput.nUseEQMode = n_use_eqmode;

  (*env)->GetFloatArrayRegion(env,af_eqgain,0,30,(jfloat *)draAudioPostProcessingParamInput.afEQGain);

  draAudioPostProcessingParamInput.bDrcActive = b_drc_active;
  draAudioPostProcessingParamInput.nDRCId = n_drcid;

  (*env)->GetIntArrayRegion(env,an_drc_curve_parameters,0,10,(jint *)draAudioPostProcessingParamInput.anDrcCurveParameters);

  draAudioPostProcessingParamInput.fDRCInputLoudness = f_drcinput_loudness;
  draAudioPostProcessingParamInput.bLimiterActive = b_limiter_active;

  draAudioPostProcessingParamInput.bUpmixInvalid = b_upmix_invalid;	 // 0->用upmix，1->不用upmix，用于“无效果模式”
  draAudioPostProcessingParamInput.nDelayAllPass = n_delay_all_pass;     // upmix参数
  draAudioPostProcessingParamInput.nAddCorLRtoULUR = n_add_cor_lrto_ulur;   // upmix参数，必须是0，1或者2
  draAudioPostProcessingParamInput.nUlUrMODE = n_ul_ur_mode;      	 // upmix参数，必须是0或者1

  draAudioPostProcessingParamInput.nStereoWidenDistance = n_stereo_widen_distance;  // 左右声道扩展参数，必须是：50, 75, 100, 130 或者 160
  draAudioPostProcessingParamInput.nStereoWidenAngleSrc = n_stereo_widen_angle_src;  // 左右声道扩展参数，必须是：10
  draAudioPostProcessingParamInput.nStereoWidenAngleDst = n_stereo_widen_angle_dst;  // 左右声道扩展参数，必须是：30, 45, 60 或者 75

  draAudioPostProcessingParamInput.fSpeakerToSpeaker = f_speaker_to_speaker;		//扬声器间距离（单位：米）
  draAudioPostProcessingParamInput.fListenerToSpeaker = f_listener_to_speaker;		//听者距离电视的距离（单位：米）
  draAudioPostProcessingParamInput.fEarToEar = f_ear_to_ear;				//听者双耳距离（单位：米）
  draAudioPostProcessingParamInput.nLoopNumber = n_loop_number;				//必须小于300
  draAudioPostProcessingParamInput.fGain = f_gain;					//CTC输出增益调整（线性，非dB）
  draAudioPostProcessingParamInput.bCTCInvalid = b_ctcinvalid;				// 0->用CTC，1->不用CTC，用于“无效果模式”


}