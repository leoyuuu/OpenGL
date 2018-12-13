//
// Created by 刘煜 on 2018/12/13.
//

#include <android/log.h>
#include "RenderV3.h"
#include "../../gl_helper.h"
#include "../../../native_helper.h"
#include "../../ImgInfo.h"

#define LOG_TAG "v3_render"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

class RenderV3Simple: public RenderV3 {
private:
    const GLfloat RECT_POINTS[24] = {
                    // 位置                   颜色
                    0.5f,   0.5f,  0.0f,    1.0f, 0.0f, 0.0f, // 右上角
                    0.5f,  -0.5f,  0.0f,    0.0f, 1.0f, 0.0f, // 右下角
                    -0.5f, -0.5f,  0.0f,    0.0f, 0.0f, 1.0f, // 左下角
                    -0.5f,  0.5f,  0.0f,    1.0f, 1.0f, 1.0f, // 左上角
            };
    const GLuint RECT_INDICES[6] = { 0, 1, 3, 1, 2, 3};

    GLuint vaoNum = 0;
    GLuint vboNum = 0;
    GLuint eboNum = 0;
    GLuint VAO[VAO_NUM];
    GLuint VBO[VBO_NUM];
    GLuint EBO[EBO_NUM];
    GLuint program;
public:
    RenderV3Simple() {}
    virtual void init() ;
    virtual void renderFrame();
    virtual void resize(uint width, uint height);
    ~RenderV3Simple();
    void click() {}
};

class RenderV3Texture: public RenderV3 {
private:
    const GLfloat RECT_POINTS[8] = {-1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f};
    const GLfloat COORDINATES[8] = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    GLuint program;
    GLuint texture;
    GLuint aPosition;
    GLuint aCoordinate;

public:
    RenderV3Texture() {}
    virtual void init() ;
    virtual void renderFrame();
    virtual void resize(uint width, uint height);
    ~RenderV3Texture();
    void click() {}
};

RenderV3::~RenderV3() {}

RenderV3* RenderV3::getRender() {
    return new RenderV3Texture();
}

void RenderV3Simple::init() {
    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
    program = createProgram("v3/base.vert", "v3/base.frag");

    glGenVertexArrays(1, VAO);
    glGenBuffers(1, VBO);
    glGenBuffers(1, EBO);
    vaoNum = 1;
    vboNum = 1;
    eboNum = 1;

    glBindVertexArray(VAO[0]);

    glBindBuffer(GL_ARRAY_BUFFER, VBO[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(RECT_POINTS), RECT_POINTS, GL_STATIC_DRAW);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(RECT_INDICES), RECT_INDICES, GL_STATIC_DRAW);

    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 6 * sizeof(float), (void *)0);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 6 * sizeof(float), (void *)(3 * sizeof(float)));
    glEnableVertexAttribArray(1);

    LOG_I("init program program: %d", program);
}

void RenderV3Simple::renderFrame() {
    glClear(GL_COLOR_BUFFER_BIT);
    glUseProgram(program);
    glBindVertexArray(VAO[0]);
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, (void *)0);
    glBindVertexArray(0);
    LOG_I("render frame");
}

void RenderV3Simple::resize(uint width, uint height) {
    glViewport(0, 0, width, height);
}

RenderV3Simple::~RenderV3Simple() {
    glDeleteVertexArrays(vaoNum, VAO);
    glDeleteBuffers(vboNum, VBO);
    glDeleteBuffers(eboNum, EBO);
    glDeleteProgram(program);
}

void RenderV3Texture::init() {
    glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
    program = createProgram("v3/img.vert", "v3/img.frag");

    aPosition = glGetAttribLocation(program, "aPosition");
    aCoordinate = glGetAttribLocation(program, "aCoordinate");

    GLuint a[1];
    glGenTextures(1, a);
    texture = a[0];
    glBindTexture(GL_TEXTURE_2D, texture);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
    ImgInfo imgInfo;
    if (imgInfo.initFromAsset("pics/mianma.jpg")) {
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, imgInfo.getWidth(), imgInfo.getHeight(), 0,
                     GL_RGBA, GL_UNSIGNED_BYTE, imgInfo.getData());
        checkGlError("glTexImage2D");
        glGenerateMipmap(GL_TEXTURE_2D);
        checkGlError("glGenerateMipmap");
    }
    imgInfo.logInfo();
}

void RenderV3Texture::renderFrame() {
    glUseProgram(program);

    glEnableVertexAttribArray(aPosition);
    glEnableVertexAttribArray(aCoordinate);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texture);
    glVertexAttribPointer(aPosition, 2, GL_FLOAT, GL_FALSE, 0, RECT_POINTS);
    glVertexAttribPointer(aCoordinate, 2, GL_FLOAT, GL_FALSE, 0, COORDINATES);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    glDisableVertexAttribArray(aCoordinate);
    glDisableVertexAttribArray(aPosition);
}

void RenderV3Texture::resize(uint width, uint height) {
    glViewport(0, 0, width, height);
}

RenderV3Texture::~RenderV3Texture() {
    glDeleteProgram(program);
}