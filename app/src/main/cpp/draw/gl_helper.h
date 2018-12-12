//
// Created by 刘煜 on 2018/12/8.
//

#ifndef OPENGL_GL_HELPER_H
#define OPENGL_GL_HELPER_H

#include <GLES2/gl2.h>

GLuint createProgram(const char* pVertexSource, const char* pFragmentSource);

void checkGlError(const char* op);
void printGLString(const char *name, GLenum s);

void frustumM(GLfloat m[16], GLint offset, GLfloat left, GLfloat right,
              GLfloat bottom, GLfloat top, GLfloat near, GLfloat far);

void transposeM(GLfloat mTrans[], GLint mTransOffset, GLfloat m[], GLint mOffset);

void translateM(GLfloat m[], int mOffset, float x, float y, float z);

void setLookAtM(GLfloat rm[16], int rmOffset, float eyeX, float eyeY, float eyeZ,
                float centerX, float centerY, float centerZ, float upX, float upY, float upZ);

void multiplyMM(GLfloat *result, int resultOffset, GLfloat *lhs, int lhsOffset, GLfloat *rhs, int rhsOffset);

void rotateM(float m[], int mOffset, float a, float x, float y, float z);

void showMatrix(GLfloat *matrix);
#endif //OPENGL_GL_HELPER_H
