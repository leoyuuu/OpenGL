//
// Created by 刘煜 on 2018/12/8.
//

#include <assert.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <android/log.h>
#include "native_gl.h"
#include "draw/draw.h"
#include "native_helper.h"

#define  LOG_TAG    "lib_native_app"
#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeInitAssetManager(JNIEnv * env, jobject obj, jobject assetManager)
{
    LOG_I("native init");
    initAsset(AAssetManager_fromJava(env, assetManager));
}

JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeInit(JNIEnv * env, jobject obj)
{
    glInit();
}

JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeResize(JNIEnv * env, jobject obj,  jint width, jint height)
{
    glResizeScreen((uint)width, (uint)height);
}

JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeRender(JNIEnv * env, jobject obj)
{
    glDrawFrame();
}

JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeDone(JNIEnv * env, jobject obj)
{
    glDone();
}

JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeInitType(JNIEnv * env, jobject obj,  jint type)
{
    glInitType((int)type);
}

JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeClickBtn(JNIEnv * env, jobject obj, jint btn)
{
    onClick((int)btn);
}


JNIEXPORT jint JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeCameraRenderGetTexture(JNIEnv * env, jobject obj){
    return (jint)getCameraTexture();
}