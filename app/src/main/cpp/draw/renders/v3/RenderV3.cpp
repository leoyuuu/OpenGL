//
// Created by 刘煜 on 2018/12/13.
//

#include <android/log.h>
#include "RenderV3.h"
#include "../../gl_helper.h"

#define LOG_TAG "v3_render"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

static const GLfloat points[] =
        {
                -0.5f, -0.5f, 0.0f,
                -0.25f, 0.5f, 0.0f,
                0.5f,  0.5f, 0.0f,
                0.25f, -0.25f, 0.0f
        };

static const GLushort point_indices[] = {
        1, 0, 2, 3
};

void RenderV3::init() {
    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
    program = createProgram("v3/base.vert", "v3/base.frag");
    vPosition = (GLuint)glGetAttribLocation(program, "vPosition");
    uColor = (GLuint)glGetUniformLocation(program, "uColor");

    GLuint ebo[1];
    glGenBuffers(1, ebo);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(point_indices), point_indices, GL_STATIC_DRAW);

    GLuint vbo[1];
    glGenBuffers(1, vbo);
    glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
    glBufferData(GL_ARRAY_BUFFER, 4 * 3 * 4, points, GL_STATIC_DRAW);
    glVertexAttribPointer(vPosition, 3, GL_FLOAT, GL_FALSE, 0, NULL);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    LOG_I("init program program: %d, position: %d", program, vPosition);
}

void RenderV3::resize(uint width, uint height) {
    glViewport(0, 0, width, height);
}

void RenderV3::renderFrame() {
    glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glUseProgram(program);
    glUniform4f(uColor, 1.0f, 0.0f, 0.0f, 1.0f);
    glEnableVertexAttribArray(vPosition);
    glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, NULL);
    checkGlError("glDrawArrays");
    glDisableVertexAttribArray(this->vPosition);
    LOG_I("render frame program: %d, position: %d", this->program, this->vPosition);
}

void RenderV3::click() {

}

RenderV3::RenderV3() {

}

RenderV3::~RenderV3() {

}
