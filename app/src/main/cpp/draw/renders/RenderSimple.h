//
// Created by 刘煜 on 2018/12/10.
//

#ifndef OPENGL_RENDERSIMPLE_H
#define OPENGL_RENDERSIMPLE_H

#include "Render.h"

class RenderSimple : public Render {
protected:
    GLuint vPosition;
public:
    RenderSimple();
    void init();
    void resize(uint width, uint height);
    void renderFrame();
    void click();
    ~RenderSimple();
};


#endif //OPENGL_RENDERSIMPLE_H
