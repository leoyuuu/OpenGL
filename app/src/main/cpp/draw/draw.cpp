//
// Created by 刘煜 on 2018/12/8.
//

// OpenGL ES 2.0 code


#include <android/log.h>
#include "gl_helper.h"
#include <typeinfo>
#include "renders/Render.h"
#include "renders/RenderCamera.h"
#include "renders/RenderCube.h"
#include "renders/RenderSimple.h"

#define LOG_TAG "native_gl_draw"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

Render* render = NULL;


void glInit() {
    LOG_I("gl init");
    if (render != NULL) {
        render->init();
    } else {
        LOG_E("native render not init");
    }
}

void glResizeScreen(uint width, uint height) {
    LOG_I("glResizeScreen(%d, %d)", width, height);
    if (render != NULL) {
        render->resize(width, height);
    } else {
        LOG_E("native render not init");
    }
}

void glDrawFrame() {
    if (render != NULL) {
        render->renderFrame();
    } else {
        LOG_E("native render not init");
    }
}

void glInitType(int type) {
    if (type == 1) {
        render = new RenderSimple();
    } else if (type == 2){
        render = new RenderCube();
    } else if (type == 3) {
        render = new RenderCamera();
    } else{
        render = new Render();
    }
}

void glDone() {
    if (render != NULL) {
        LOG_I("render %s delete", typeid(render).name());
        delete render;
        render = NULL;
    }
    LOG_I("GL done");
}

void onClick(int btn) {
    if (render != NULL) {
        render->click();
    } else {
        LOG_E("native render not init");
    }
}

GLint getCameraTexture() {
    if (render != NULL) {
        if (typeid(render) == typeid( RenderCamera ) ) {
            return ((RenderCamera *)render)->textureId();
        }
    }
    return 0;
}