//
// Created by 刘煜 on 2018/12/12.
//

#include "RenderCamera.h"
#include "../gl_helper.h"


#define LOG_TAG "camera_render"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

#define G_VERTICES_SHADER  "camera/camera.vert"
#define G_FRAGMENT_SHADER  "camera/camera.frag"

GLint RenderCamera::textureId() {
    return (GLint)iTextureId;
}

RenderCamera::RenderCamera() {

}

RenderCamera::~RenderCamera() {

}

void RenderCamera::init() {
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

    program = createProgram(G_VERTICES_SHADER, G_FRAGMENT_SHADER);
    aCoordinate = (GLuint)glGetAttribLocation(program, "aCoordinate");
    vPosition = (GLuint)glGetAttribLocation(program, "aPosition");
    sTexture = (GLuint)glGetUniformLocation(program, "sTexture");
    uMatrix = (GLuint)glGetUniformLocation(program, "uMatrix");

    rotateM(matrix, 0, 90.0f, 0.0f, 0.0f, -1.0f);

    GLuint ids[1];
    glGenTextures(1, ids);
    iTextureId = ids[0];
    glBindTexture(GL_TEXTURE_EXTERNAL_OES, iTextureId);
    glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
}

void RenderCamera::renderFrame() {
    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glUseProgram(program);
    glUniformMatrix4fv(uMatrix, 1, GL_FALSE, matrix);
    glEnableVertexAttribArray(vPosition);
    glEnableVertexAttribArray(aCoordinate);
    glVertexAttribPointer(vPosition, 2, GL_FLOAT, GL_FALSE, 0, vertices);
    glVertexAttribPointer(aCoordinate, 2, GL_FLOAT, GL_FALSE, 0, coordinates);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    glDisableVertexAttribArray(aCoordinate);
    glDisableVertexAttribArray(vPosition);
}

void RenderCamera::resize(uint width, uint height) {
    glViewport(0, 0, width, height);
}

void RenderCamera::click() {

}