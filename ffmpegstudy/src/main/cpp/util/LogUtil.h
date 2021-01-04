//
// Created by kylin on 21-1-3.
//

#ifndef NDK_SAMPLE_LOGUTIL_H
#define NDK_SAMPLE_LOGUTIL_H

#include<android/log.h>

#define  LOG_TAG "ffmpeg-study"

#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG,__VA_ARGS__)
#define  LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG,__VA_ARGS__)

#endif //NDK_SAMPLE_LOGUTIL_H
