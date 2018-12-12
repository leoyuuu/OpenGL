//
// Created by 刘煜 on 2018/12/13.
//

#ifndef OPENGL_RENDERV3_H
#define OPENGL_RENDERV3_H


#include <GLES3/gl3.h>
#include <sys/types.h>

class RenderV3{
protected:
    GLuint program;
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
    RenderV3();
    virtual void init();
    virtual void resize(uint width, uint height);
    virtual void renderFrame();
    virtual void click();
    virtual ~RenderV3();
};


#endif //OPENGL_RENDERV3_H
