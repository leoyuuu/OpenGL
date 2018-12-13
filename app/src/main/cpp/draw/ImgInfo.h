//
// Created by 刘煜 on 2018/12/13.
//

#ifndef OPENGL_IMGINFO_H
#define OPENGL_IMGINFO_H


#include <sys/types.h>
#include <cstdlib>

class ImgInfo {
private:
    int width;
    int height;
    int channel;
    int size;
    uint8_t* imageBits = nullptr;
public:
    virtual bool initFromAsset(const char * assetName);
    uint getWidth(){ return (uint)width;}
    uint getHeight() { return (uint)height;}
    uint getChannel() { return (uint)channel; }
    uint8_t* getData() { return imageBits; }
    virtual void logInfo();
    virtual ~ImgInfo();
};


#endif //OPENGL_IMGINFO_H
