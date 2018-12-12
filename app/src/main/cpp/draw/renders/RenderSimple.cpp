//
// Created by 刘煜 on 2018/12/10.
//

#include <android/log.h>
#include "RenderSimple.h"
#include "../gl_helper.h"

#define LOG_TAG "simple_render"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

#define G_VERTICES_SHADER  "jni/simple.vert"
#define G_FRAGMENT_SHADER  "jni/simple.frag"

static const GLfloat points[] =
        {
                -0.5f, -0.5f,
                -0.25f, 0.5f,
                 0.5f,  0.5f,
                 0.25f, -0.25f
        };

static const GLushort point_indices[] = {
        1, 0, 2, 3
};

RenderSimple::RenderSimple() {
    LOG_I("new render cube");
}

void RenderSimple::init() {
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

    this->program = createProgram(G_VERTICES_SHADER, G_FRAGMENT_SHADER);
    this->vPosition = (GLuint)glGetAttribLocation(this->program, "vPosition");

    GLuint ebo[1];
    glGenBuffers(1, ebo);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(point_indices), point_indices, GL_STATIC_DRAW);

    GLuint vbo[1];
    glGenBuffers(1, vbo);
    glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
    glBufferData(GL_ARRAY_BUFFER, 4 * 2 * 4, points, GL_STATIC_DRAW);
    glVertexAttribPointer(this->vPosition, 2, GL_FLOAT, GL_FALSE, 0, NULL);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    LOG_I("init program program: %d, position: %d", this->program, this->vPosition);
}

void RenderSimple::resize(uint width, uint height) {
    glViewport(0, 0, width, height);
}

void RenderSimple::renderFrame() {
    glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glUseProgram(this->program);
    glEnableVertexAttribArray(this->vPosition);
    glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, NULL);
    checkGlError("glDrawArrays");
    glDisableVertexAttribArray(this->vPosition);
    LOG_I("render frame program: %d, position: %d", this->program, this->vPosition);
}

void RenderSimple::click() {
    LOG_I("click ");
}

RenderSimple::~RenderSimple() {

}