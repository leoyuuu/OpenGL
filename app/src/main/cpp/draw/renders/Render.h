//
// Created by 刘煜 on 2018/12/10.
//

#ifndef OPENGL_RENDER_H
#define OPENGL_RENDER_H


#include <sys/types.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

class Render {
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
    Render();
    virtual void init();
    virtual void resize(uint width, uint height);
    virtual void renderFrame();
    virtual void click();
    virtual ~Render();
};


#endif //OPENGL_RENDER_H
