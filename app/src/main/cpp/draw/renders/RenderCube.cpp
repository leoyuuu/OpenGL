//
// Created by 刘煜 on 2018/12/10.
//

#include <android/log.h>
#include <GLES2/gl2.h>
#include "RenderCube.h"
#include "../gl_helper.h"
#include <cmath>


#define LOG_TAG "cube_render"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

#define G_VERTICES_SHADER  "jni/cube.vert"
#define G_FRAGMENT_SHADER  "jni/cube.frag"


static const GLfloat cube_positions[] =
        {
                -0.5f, -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f,  0.5f, 0.5f,
                -0.5f,  0.5f, -0.5f, 0.5f,
                -0.5f,  0.5f,  0.5f, 0.5f,
                0.5f, -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f,  0.5f, 0.5f,
                0.5f,  0.5f, -0.5f, 0.5f,
                0.5f,  0.5f,  0.5f, 0.5f
        };

// Color for each vertex
static const GLfloat cube_colors[] = {
        1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 1.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.5f, 0.5f, 0.5f, 1.0f
};

static const GLushort cube_indices[] = {
        0, 1, 2, 3, 6, 7, 4, 5, // First strip
        0xFFFF,                 // <<-- This is the restart index
        2, 6, 0, 4, 1, 5, 3, 7  // Second strip
};

RenderCube::RenderCube() {
    LOG_I("new render cube");
}

void RenderCube::init() {
    glEnable(GL_DEPTH_TEST);
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

    this->program = createProgram(G_VERTICES_SHADER, G_FRAGMENT_SHADER);
    this->vPosition = (GLuint)glGetAttribLocation(this->program, "vPosition");
    this->uMatrix = (GLuint)glGetUniformLocation(this->program, "uMatrix");
    this->aColor = (GLuint)glGetAttribLocation(this->program, "aColor");

    GLuint ebo[1];
    glGenBuffers(1, ebo);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(cube_indices), cube_indices, GL_STATIC_DRAW);

    GLuint vbo[1];
    glGenBuffers(1, vbo);
    glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(cube_positions) + sizeof(cube_colors), NULL, GL_STATIC_DRAW);
    glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof(cube_positions), cube_positions);
    glBufferSubData(GL_ARRAY_BUFFER, sizeof(cube_positions), sizeof(cube_colors), cube_colors);

    glVertexAttribPointer(this->vPosition, 3, GL_FLOAT, GL_FALSE, 16, NULL);
    glVertexAttribPointer(this->aColor, 4, GL_FLOAT, GL_FALSE, 0, (const GLvoid *)sizeof(cube_positions));
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    LOG_I("init program");
}

void RenderCube::resize(uint width, uint height) {
    glViewport(0, 0, width, height);
    GLfloat ratio = ((GLfloat)width) / height;
    frustumM(mProjectMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 20.0f);
    LOG_I("project matrix: ");
    showMatrix(mProjectMatrix);

    setLookAtM(mViewMatrix, 0, -5.0f, 5.0f, 10.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    LOG_I("mViewMatrix: ");
    showMatrix(mViewMatrix);

    multiplyMM(matrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    LOG_I("matrix: ");
    showMatrix(matrix);
}

void RenderCube::renderFrame() {
    glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glUseProgram(this->program);
    glUniformMatrix4fv(this->uMatrix, 1, GL_FALSE, matrix);
    glEnableVertexAttribArray(this->vPosition);
    glEnableVertexAttribArray(this->aColor);
    glDrawElements(GL_TRIANGLE_STRIP, 8, GL_UNSIGNED_SHORT, NULL);
    glDrawElements(GL_TRIANGLE_STRIP, 8, GL_UNSIGNED_SHORT,(const GLvoid *)(9 * sizeof(GLushort)));
    glDisableVertexAttribArray(this->vPosition);
    glDisableVertexAttribArray(this->aColor);
    LOG_I("render frame");
}

void RenderCube::click() {
    LOG_I("click ");
    this->rad += M_PI / 10;
    rotateM(this->matrix, 0, this->rad, 1.0f, 1.0f, -1.0f);
}

RenderCube::~RenderCube() {

}