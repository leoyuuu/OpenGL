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
#include "renders/v3/RenderV3.h"

#define LOG_TAG "native_gl_draw"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

Render* render = NULL;
RenderV3* renderV3 = NULL;

void glInit() {
    LOG_I("gl init");
    if (render != NULL) {
        render->init();
    } else if (renderV3 != NULL) {
        renderV3->init();
    } else {
        LOG_E("native render not init");
    }
}

void glResizeScreen(uint width, uint height) {
    LOG_I("glResizeScreen(%d, %d)", width, height);
    if (render != NULL) {
        render->resize(width, height);
    } if (renderV3 != NULL) {
        renderV3->resize(width, height);
    } else {
        LOG_E("native render not init");
    }
}

void glDrawFrame() {
    if (render != NULL) {
        render->renderFrame();
    } else if (renderV3 != NULL) {
        renderV3->renderFrame();
    } else {
        LOG_E("native render not init");
    }
}

void glInitType(int type) {
    LOG_E("gl init type: %d", type);
    switch (type) {
        case 1:
            render = new RenderSimple();
            break;
        case 2:
            render = new RenderCube();
            break;
        case 3:
            render = new RenderCamera();
            break;
        case 4:
            renderV3 = RenderV3::getRender();
            break;
        default:
            render = new Render();
            break;
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
    } if (renderV3 != NULL) {
        renderV3->click();
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