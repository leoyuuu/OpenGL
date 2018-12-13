//
// Created by 刘煜 on 2018/12/14.
//

#include <android/log.h>
#include "RenderV3Box.h"
#include "../../gl_helper.h"
#include "../../../glm/glm.hpp"
#include "../../ImgInfo.h"
#include "../../../glm/gtc/matrix_transform.hpp"
#include "Shapes.h"


#define LOG_TAG "v3_box_render"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

class RenderV3BoxImp:public RenderV3Box{
private:
    GLuint program;
    GLint matrix;
    GLuint VBO, EBO;
    GLfloat matrix4[16];
public:
    virtual void init();
    virtual void resize(uint width, uint height);
    virtual void renderFrame();
    virtual void click();
    virtual ~RenderV3BoxImp();
};

class RenderV3Boxes:public RenderV3Box{
private:
    GLuint program;
    GLint matrix;
public:
    virtual void init();
    virtual void resize(uint width, uint height);
    virtual void renderFrame();
    virtual void click();
    virtual ~RenderV3Boxes();
};

RenderV3Box* RenderV3Box::getRender() {
    return new RenderV3BoxImp();
}

void RenderV3BoxImp::init() {
    glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
    glEnable(GL_DEPTH_TEST);

    program = createProgram("v3/box.vert", "v3/box.frag");
    matrix = glGetUniformLocation(program, "matrix");
    checkGlError("glGetUniformLocation", __LINE__);

    glGenBuffers(1, &EBO);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(NORMAL_BOX_INDICES_STRIP), NORMAL_BOX_INDICES_STRIP, GL_STATIC_DRAW);
    glGenBuffers(1, &VBO);
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(NORMAL_BOX) + sizeof(NORMAL_BOX_COLOR), NULL, GL_STATIC_DRAW);
    glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof(NORMAL_BOX), NORMAL_BOX);
    glBufferSubData(GL_ARRAY_BUFFER, sizeof(NORMAL_BOX), sizeof(NORMAL_BOX_COLOR), NORMAL_BOX_COLOR);

    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, NULL);
    glVertexAttribPointer(1, 4, GL_FLOAT, GL_FALSE, 0, (const GLvoid *)sizeof(NORMAL_BOX));
    glBindBuffer(GL_ARRAY_BUFFER, 0);

}

void RenderV3BoxImp::resize(uint width, uint height) {
    glViewport(0, 0, width, height);
    GLfloat ratio = ((GLfloat)width) / height;
    GLfloat projectMatrix[16];
    GLfloat viewMatrix[16];
    frustumM(projectMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 20.0f);
    setLookAtM(viewMatrix, 0, -5.0f, 5.0f, 10.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    multiplyMM(matrix4, 0, projectMatrix, 0, viewMatrix, 0);
}

void RenderV3BoxImp::renderFrame() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glUseProgram(program);
    rotateM(matrix4, 0, 1, 0, 1, 0);
    glUniformMatrix4fv(matrix, 1, GL_FALSE, matrix4);
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);
    glDrawElements(GL_TRIANGLE_STRIP, 8, GL_UNSIGNED_SHORT, NULL);
    glDrawElements(GL_TRIANGLE_STRIP, 8, GL_UNSIGNED_SHORT,(const GLvoid *)(8 * sizeof(GLushort)));
    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
    checkGlError("render frame", __LINE__);
}

void RenderV3BoxImp::click() {}

RenderV3BoxImp::~RenderV3BoxImp() {
    glDeleteBuffers(1, &VBO);
    glDeleteBuffers(1, &EBO);
    glDeleteProgram(program);
}


void RenderV3Boxes::init() {}
void RenderV3Boxes::click() {}
void RenderV3Boxes::renderFrame() {}
void RenderV3Boxes::resize(uint width, uint height) {}
RenderV3Boxes::~RenderV3Boxes() {}