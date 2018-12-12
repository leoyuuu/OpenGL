//
// Created by 刘煜 on 2018/12/12.
//

#ifndef OPENGL_RENDERCAMERA_H
#define OPENGL_RENDERCAMERA_H


#include "Render.h"

class RenderCamera : public Render {
private:
    GLfloat vertices[8] = {-1.0f,1.0f, -1.0f,-1.0f, 1.0f,1.0f, 1.0f,-1.0f};
    GLfloat coordinates[8] = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    GLuint vPosition;
    GLuint uMatrix;
    GLuint aCoordinate;
    GLuint sTexture;
    GLuint iTextureId;
public:
    RenderCamera();
    void init();
    void resize(uint width, uint height);
    void renderFrame();
    void click();
    GLint textureId();
    ~RenderCamera();
};


#endif //OPENGL_RENDERCAMERA_H
