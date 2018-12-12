//
// Created by 刘煜 on 2018/12/8.
//
#include <malloc.h>
#include <android/log.h>
#include "native_helper.h"


#define  LOG_TAG    "lib_native_helper"
#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

S_App sApp;

void initAsset(AAssetManager* assetManager) {
    sApp.assetManager = assetManager;
}

char* Asset_ReadString(const char* filename) {
    AAsset* asset = AAssetManager_open(sApp.assetManager, filename, AASSET_MODE_BUFFER);

    assert(asset != NULL);

    off_t len = AAsset_getLength(asset);
    char * buf = (char *)malloc((size_t)len + 1);
    buf[len] = 0;
    AAsset_read(asset, buf, (size_t )len);
    AAsset_close(asset);

    LOG_I("asset read %s, content: %s", filename, buf);
    return buf;
}