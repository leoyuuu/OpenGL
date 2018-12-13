//
// Created by 刘煜 on 2018/12/13.
//

#ifndef OPENGL_RENDERV3_H
#define OPENGL_RENDERV3_H


#include <GLES3/gl3.h>
#include <sys/types.h>

#define GL3 1

#define VAO_NUM 20
#define VBO_NUM 20
#define EBO_NUM 20

class RenderV3 {
protected:
    GLuint vaoNum = 0;
    GLuint vboNum = 0;
    GLuint eboNum = 0;
    GLuint VAO[VAO_NUM];
    GLuint VBO[VBO_NUM];
    GLuint EBO[EBO_NUM];
    GLuint program;
    GLuint vPosition;
    GLuint uColor;
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
