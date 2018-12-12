//
// Created by 刘煜 on 2018/12/13.
//

#include "RenderV3.h"

#define LOG_TAG "v3_render"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

void RenderV3::init() {
    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
}

void RenderV3::resize(uint width, uint height) {
    glViewport(0, 0, width, height);
}

void RenderV3::renderFrame() {
    glClear(GL_COLOR_BUFFER_BIT);
}

void RenderV3::click() {

}

RenderV3::RenderV3() {

}

RenderV3::~RenderV3() {

}
