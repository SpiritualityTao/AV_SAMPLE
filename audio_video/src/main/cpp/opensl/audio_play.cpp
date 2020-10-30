#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "JniProjects"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG,  __VA_ARGS__);

extern "C"
JNIEXPORT void JNICALL
Java_com_peter_study_audio_1video_audio_AudioPlayActivity_nativePlayPcm(JNIEnv *env, jclass clazz,
                                                                        jstring pcm_path) {
    const char *str_path = env->GetStringUTFChars(pcm_path, NULL);
    LOGD("nativePlayPcm str_path:%s", str_path);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_peter_study_audio_1video_audio_AudioPlayActivity_nativeStopPcm(JNIEnv *env, jclass clazz) {
    LOGD("nativeStopPcm");
}