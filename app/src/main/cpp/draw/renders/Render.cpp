//
// Created by 刘煜 on 2018/12/10.
//

#include <android/log.h>
#include <random>
#include "Render.h"
#include "../gl_helper.h"

#define LOG_TAG "render"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

#define G_VERTICES_SHADER  "tmp/tmp.vert"
#define G_FRAGMENT_SHADER  "tmp/tmp.frag"


const uint VERTICES_NUMBER = 5;
const GLfloat gTriangleVertices[] = { 0.0f,  0.5f,
                                      -0.5f, -0.5f,
                                      0.5f, -0.5f,
                                      -1.0f, -1.0f,
                                      1.0f, -1.0f};

bool setupGraphics(GLuint program) {
    printGLString("Version", GL_VERSION);
    printGLString("Vendor", GL_VENDOR);
    printGLString("Renderer", GL_RENDERER);
    printGLString("Extensions", GL_EXTENSIONS);

    glClearColor(0.0f, 1.0f, 1.0f, 1.0f);

    GLuint vPosition= (GLuint)glGetAttribLocation(program, "vPosition");
    checkGlError("glGetAttribLocation");

    LOG_I("glGetAttribLocation(\"vPosition\") = %d\n", vPosition);
    GLuint names[1];
    glGenBuffers(1, names);
    checkGlError("glGenBuffers");
    glBindBuffer(GL_ARRAY_BUFFER, names[0]);
    checkGlError("glBindBuffer");
    glBufferData(GL_ARRAY_BUFFER, 4 * 2 * VERTICES_NUMBER, gTriangleVertices, GL_STATIC_DRAW);
    checkGlError("glBufferData");
    glVertexAttribPointer(vPosition, 2, GL_FLOAT, GL_FALSE, 0, 0);
    checkGlError("glVertexAttribPointer");

    return true;
}


void drawFrame(GLuint program) {
    checkGlError("glClearColor");
    glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    checkGlError("glClear");

    glUseProgram(program);
    checkGlError("glUseProgram");
    GLuint vPosition= (GLuint)glGetAttribLocation(program, "vPosition");

    checkGlError("glVertexAttribPointer");
    glEnableVertexAttribArray(vPosition);
    checkGlError("glEnableVertexAttribArray");
    glDrawArrays(GL_TRIANGLE_STRIP, 0, VERTICES_NUMBER);
    checkGlError("glDrawArrays");
    glDisableVertexAttribArray(vPosition);
    checkGlError("glDisableVertexAttribArray");
}

Render::Render() {
    LOG_I("new render");
}

void Render::init() {
    this->program = createProgram(G_VERTICES_SHADER, G_FRAGMENT_SHADER);
    setupGraphics(this->program);
}

void Render::resize(uint width, uint height) {
    glViewport(0, 0, width, height);
}

void Render::renderFrame() {
    drawFrame(this->program);
}

void Render::click() {
    LOG_I("click");
}

Render::~Render() {
    LOG_I("delete render");
}