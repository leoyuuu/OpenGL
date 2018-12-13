//
// Created by 刘煜 on 2018/12/13.
//

#ifndef OPENGL_RENDERV3_H
#define OPENGL_RENDERV3_H


#include <GLES3/gl3.h>
#include <sys/types.h>

#define GL3 1

#define VAO_NUM 16
#define VBO_NUM 16
#define EBO_NUM 16
#define TEXTURE_NUM 16

class RenderV3 {
protected:
    RenderV3(){};
    virtual ~RenderV3();
public:
    virtual void init(){}
    virtual void resize(uint width, uint height){}
    virtual void renderFrame(){}
    virtual void click(){}
    static RenderV3* getRender();
};


#endif //OPENGL_RENDERV3_H
