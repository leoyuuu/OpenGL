//
// Created by 刘煜 on 2018/12/8.
//

#ifndef OPENGL_NATIVE_APP_H
#define OPENGL_NATIVE_APP_H

#include <jni.h>
#include <android/log.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

extern "C" {
    JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeInit(JNIEnv * env, jobject obj);
    JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeResize(JNIEnv * env, jobject obj,  jint width, jint height);
    JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeRender(JNIEnv * env, jobject obj);
    JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeDone(JNIEnv * env, jobject obj);
    JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeInitType(JNIEnv * env, jobject obj, jint type);
    JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeClickBtn(JNIEnv * env, jobject obj, jint btn);
    JNIEXPORT void JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeInitAssetManager(JNIEnv * env, jobject obj, jobject assetManager);


    JNIEXPORT jint JNICALL Java_me_leoyuu_opengl_jni_JniGl_nativeCameraRenderGetTexture(JNIEnv * env, jobject obj);
}

#endif //OPENGL_NATIVE_APP_H
