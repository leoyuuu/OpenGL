//
// Created by 刘煜 on 2018/12/10.
//

#ifndef OPENGL_CUBERENDER_H
#define OPENGL_CUBERENDER_H

#include "Render.h"

class RenderCube:public Render {
protected:
    GLuint vPosition;
    GLuint aColor;
    GLuint uMatrix;
    GLfloat rad;
    GLfloat mProjectMatrix[16] = {
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
    };
    GLfloat mViewMatrix[16] = {
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
    };
    GLfloat matrix[16] = {
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
    };
public:
    RenderCube();
    void init();
    void resize(uint width, uint height);
    void renderFrame();
    void click();
    ~RenderCube();
};


#endif //OPENGL_CUBERENDER_H
