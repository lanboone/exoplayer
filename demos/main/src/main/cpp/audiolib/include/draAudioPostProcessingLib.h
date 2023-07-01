#ifndef __DRA_AUDIO_POST_PROCESSING_H__
#define __DRA_AUDIO_POST_PROCESSING_H__

#ifdef __cplusplus
extern "C"
{
#endif

#define MAX_DRA_SOUNDBAR_CHANNLE_IN 10

#define SIMPLE_DOWNMIX_MODE			0
#define HRTF_MODE					1
#define STEREO_WIDEN_MODE			2

#define EQ_NUM_10					10
#define EQ_NUM_30					30
#define DRC_NODE_NUM				5

#define EQ_MODE_NONE				0
#define EQ_MODE_BAND_10				1
#define EQ_MODE_BAND_30				2

typedef struct
{
	int   nHRTFMode;
	int	  bCenterChannelElevOn;
	float afHRTFChannelGain[MAX_DRA_SOUNDBAR_CHANNLE_IN];

	int   bEqActive;
	int   nUseEQMode;
	float afEQGain[EQ_NUM_30];

	int   bDrcActive;
	int   nDRCId;
	int   anDrcCurveParameters[DRC_NODE_NUM * 2];
	float fDRCInputLoudness;

	int bLimiterActive;

	int bUpmixInvalid;
	int nDelayAllPass;
	int nAddCorLRtoULUR;
	int nUlUrMODE;

	int nStereoWidenDistance;
	int nStereoWidenAngleSrc;
	int nStereoWidenAngleDst;

	float fSpeakerToSpeaker;
	float fListenerToSpeaker;
	float fEarToEar;
	int	  nLoopNumber;
	float fGain;
	int   bCTCInvalid;
} DRA_AUDIO_POST_PROCESSING_PARAM_INPUT;

typedef struct _DRA_AUDIO_POST_PROCESSING_ DRA_AUDIO_POST_PROCESSING, *H_DRA_AUDIO_POST_PROCESSING;

int InitDraAudioPostProcessing(H_DRA_AUDIO_POST_PROCESSING				   *phDraAudioPostProcessing,
							   const DRA_AUDIO_POST_PROCESSING_PARAM_INPUT *hDraAudioPostProcessingParamInput,
							   const unsigned int						   nInChannels,
							   const unsigned int						   nOutChannels,
							   const unsigned int						   nSampleRate,
							   const unsigned int						   nFrameSize);

int ResetDraAudioPostProcessing(H_DRA_AUDIO_POST_PROCESSING					hDraAudioPostProcessing,
								const DRA_AUDIO_POST_PROCESSING_PARAM_INPUT *hDraAudioPostProcessingParamInput);

int MakeDraAudioPostProcessing(H_DRA_AUDIO_POST_PROCESSING hDraAudioPostProcessing, 
							   const float				   **ppInBuffer, 
							   float					   **ppOutBuffer, 
							   unsigned int				   nFrameSize);

void CloseDraAudioPostProcessing(H_DRA_AUDIO_POST_PROCESSING *phDraAudioPostProcessing);

#ifdef __cplusplus
}
#endif

#endif