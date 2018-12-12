//
// Created by 刘煜 on 2018/12/8.
//

#ifndef OPENGL_DRAW_H
#define OPENGL_DRAW_H


#include <GLES2/gl2.h>

#define BTN_LEFT        0
#define BTN_UP          1
#define BTN_RIGHT       2
#define BTN_BOTTOM      3
#define BTN_A           4
#define BTN_B           5
#define BTN_C           6
#define BTN_D           7

#define INIT_TYPE_DEFAULT 0

void glInit();
void glInitType(int type);
void glResizeScreen(uint width, uint height);
void glDrawFrame();
void glDone();

void onClick(int btn);

GLint getCameraTexture();

#endif //OPENGL_DRAW_H
