//
// Created by 刘煜 on 2018/12/14.
//

#ifndef OPENGL_BOX_H
#define OPENGL_BOX_H

const static GLfloat NORMAL_BOX[] = {
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f,  0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f,  0.5f
};

const static GLushort NORMAL_BOX_INDICES_STRIP[] = {
        0, 1, 2, 3, 6, 7, 4, 5,
        2, 6, 0, 4, 1, 5, 3, 7
};

const static GLfloat NORMAL_BOX_COLOR[] = {
        0.0f, 0.0f, 1.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.5f, 0.1f, 0.9f, 1.0f
};

const static GLfloat COORDINATES[] = {0, 0, 0, 1, 1, 0, 1, 1};

#endif //OPENGL_BOX_H
