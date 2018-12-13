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
                0.5f, 0.5f, 0.0f,   // 右上角
                0.5f, -0.5f, 0.0f,  // 右下角
                -0.5f, -0.5f, 0.0f, // 左下角
                -0.5f, 0.5f, 0.0f   // 左上角
        };

static const GLuint point_indices[] = {
        0, 1, 3, // 第一个三角形
        1, 2, 3  // 第二个三角形
};

void RenderV3::init() {
    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
    program = createProgram("v3/base.vert", "v3/base.frag");
    vPosition = (GLuint)glGetAttribLocation(program, "aPosition");
    uColor = (GLuint)glGetUniformLocation(program, "uColor");

    glGenVertexArrays(1, VAO);
    glGenBuffers(1, VBO);
    glGenBuffers(1, EBO);
    vaoNum++;
    vboNum++;
    eboNum++;

    glBindVertexArray(VAO[0]);

    glBindBuffer(GL_ARRAY_BUFFER, VBO[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(points), points, GL_STATIC_DRAW);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(point_indices), point_indices, GL_STATIC_DRAW);

    glVertexAttribPointer(vPosition, 3, GL_FLOAT, GL_FALSE, 0, (void *)0);
    glEnableVertexAttribArray(vPosition);

    glBindBuffer(GL_ARRAY_BUFFER, 0);

    LOG_I("init program program: %d, position: %d", program, vPosition);
}

void RenderV3::resize(uint width, uint height) {
    glViewport(0, 0, width, height);
}

void RenderV3::renderFrame() {
    glClear(GL_COLOR_BUFFER_BIT);
    glUseProgram(program);
    glUniform4f(uColor, 1.0f, 0.0f, 0.0f, 1.0f);
    glBindVertexArray(VAO[0]);
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void *)0);
    glBindVertexArray(0);
}

void RenderV3::click() {

}

RenderV3::RenderV3() {

}

RenderV3::~RenderV3() {
    glDeleteVertexArrays(vaoNum, VAO);
    glDeleteBuffers(vboNum, VBO);
    glDeleteBuffers(eboNum, EBO);
}
