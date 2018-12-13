//
// Created by 刘煜 on 2018/12/8.
//

#ifndef OPENGL_NATIVE_HELPER_H
#define OPENGL_NATIVE_HELPER_H

#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <assert.h>
#include <string>
#include <vector>

class S_App {
public:
    S_App(){}
    AAssetManager *assetManager;
};

// 需要调 free 释放本函数申请的数据
char* Asset_ReadString(const char* filename);

bool Asset_ReadBuf(const char * filename, std::vector<uint8_t>& buf);

void initAsset(AAssetManager* assetManager);

#endif //OPENGL_NATIVE_HELPER_H
