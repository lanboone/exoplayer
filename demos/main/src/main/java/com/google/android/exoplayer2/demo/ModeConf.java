package com.google.android.exoplayer2.demo;

public class ModeConf {
    public int nHRTFMode;
    public int bCenterChannelElevOn;
    public float[] afHRTFChannelGain = new float[10];
    public int bEqActive;
    public int nUseEQMode;
    public float[] afEQGain = new float[30];
    public int bDrcActive;
    public int nDRCId;
    public int[] anDrcCurveParameters = new int[10];
    public float fDRCInputLoudness;
    public int bLimiterActive;
    public int bUpmixInvalid;
    public int nDelayAllPass;
    public int nAddCorLRtoULUR;
    public int nUlUrMODE;
    public int nStereoWidenDistance;
    public int nStereoWidenAngleSrc;
    public int nStereoWidenAngleDst;
    public float fSpeakerToSpeaker;
    public float fListenerToSpeaker;
    public float fEarToEar;
    public int nLoopNumber;
    public float fGain;
    public int bCTCInvalid;


    public int nEffectActive;
    public int nWidenMode;
    public int nWidenGain;
    public int nWidenCenter;
    public int nVoiceGain;
    public int bCTCActive;
    public int nCTCMode;
    public int bVBActive;
    public int nVBStyle;
    public int nVBGain;
    public int bLoudnessActive;
    public int nLoudness;

    ModeConf() {

    }

    void copy(ModeConf conf) {
        nHRTFMode = conf.nHRTFMode;
        bCenterChannelElevOn = conf.bCenterChannelElevOn;
        for (int i = 0; i < 10; i++) {
            afHRTFChannelGain[i] = conf.afHRTFChannelGain[i];
        }
        bEqActive = conf.bEqActive;
        nUseEQMode = conf.nUseEQMode;
        for (int i = 0; i < 30; i++) {
            afEQGain[i] = conf.afEQGain[i];
        }

        bDrcActive = conf.bDrcActive;

        nDRCId = conf.nDRCId;
        for (int i = 0; i < 10; i++) {
            anDrcCurveParameters[i] = conf.anDrcCurveParameters[i];
        }

        fDRCInputLoudness = conf.fDRCInputLoudness;
        bLimiterActive = conf.bLimiterActive;
        bUpmixInvalid = conf.bUpmixInvalid;
        nDelayAllPass = conf.nDelayAllPass;
        nAddCorLRtoULUR = conf.nAddCorLRtoULUR;
        nUlUrMODE = conf.nUlUrMODE;
        nStereoWidenDistance = conf.nStereoWidenDistance;
        nStereoWidenAngleSrc = conf.nStereoWidenAngleSrc;
        nStereoWidenAngleDst = conf.nStereoWidenAngleDst;
        fSpeakerToSpeaker = conf.fSpeakerToSpeaker;
        fListenerToSpeaker = conf.fListenerToSpeaker;
        fEarToEar = conf.fEarToEar;
        nLoopNumber = conf.nLoopNumber;
        fGain = conf.fGain;
        bCTCInvalid = conf.bCTCInvalid;

        nEffectActive = conf.nEffectActive;
        nWidenMode = conf.nWidenMode;
        nWidenGain = conf.nWidenGain;
        nWidenCenter = conf.nWidenCenter;
        nVoiceGain = conf.nVoiceGain;
        bCTCActive = conf.bCTCActive;
        nCTCMode = conf.nCTCMode;
        bVBActive = conf.bVBActive;
        nVBStyle = conf.nVBStyle;
        nVBGain = conf.nVBGain;
        bLoudnessActive = conf.bLoudnessActive;
        nLoudness = conf.nLoudness;
    }

}
