//
// Created by 刘煜 on 2018/12/14.
//

#ifndef OPENGL_RENDERV3BOX_H
#define OPENGL_RENDERV3BOX_H


#include "RenderV3.h"

class RenderV3Box: public RenderV3 {
public:
    virtual void init(){}
    virtual void resize(uint width, uint height){}
    virtual void renderFrame(){}
    virtual void click(){}
    virtual ~RenderV3Box(){}
    static  RenderV3Box* getRender();
};

#endif //OPENGL_RENDERV3BOX_H
