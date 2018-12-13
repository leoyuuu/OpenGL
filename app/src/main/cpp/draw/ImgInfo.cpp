//
// Created by 刘煜 on 2018/12/13.
//

#include <android/log.h>
#include "ImgInfo.h"
#include "../native_helper.h"
#define STB_IMAGE_IMPLEMENTATION
#include "../stb/stb_image.h"


#define LOG_TAG "ImgInfo"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

bool ImgInfo::initFromAsset(const char *assetName) {
    std::vector<uint8_t> fileBits;
    Asset_ReadBuf(assetName, fileBits);
    stbi_set_flip_vertically_on_load(1);
    size = (int)fileBits.size();
    imageBits = stbi_load_from_memory(fileBits.data(), size, &width, &height, &channel, 4);
    return imageBits != nullptr;
}

ImgInfo::~ImgInfo() {
    LOG_I("destruct ImgInfo");

    if (imageBits != NULL) {
        stbi_image_free(imageBits);
    }
}

void ImgInfo::logInfo() {
    LOG_I("ImgInfo{width:%d, height:%d, channel:%d, size:%d}", width, height, channel, size);
}