//
// Created by 刘煜 on 2018/12/9.
//



#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>
#include <cmath>
#include "../native_helper.h"


#define LOG_TAG "native_helper"

#define  LOG_I(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOG_E(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

void printGLString(const char *name, GLenum s) {
    const char *v = (const char *) glGetString(s);
    LOG_I("GL %s = %s\n", name, v);
}

void checkGlError(const char* op) {
    for (GLint error = glGetError(); error; error = glGetError()) {
        LOG_I("after %s() glError (0x%x)\n", op, error);
    }
}

GLfloat tmp[32];

GLuint loadShader(GLenum shaderType, const char* filename) {
    char * content = Asset_ReadString(filename);
    GLuint shader = glCreateShader(shaderType);
    if (shader && content) {
        glShaderSource(shader, 1, &content, NULL);
        glCompileShader(shader);
        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
        if (!compiled) {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen) {
                char* buf = (char*) malloc((size_t)infoLen);
                if (buf) {
                    glGetShaderInfoLog(shader, infoLen, NULL, buf);
                    LOG_E("Could not compile shader %d:\n%s\n",
                          shaderType, buf);
                    free(buf);
                }
                glDeleteShader(shader);
                shader = 0;
            }
        }
        free(content);
    }
    return shader;
}

GLuint createProgram(const char* pVertexSource, const char* pFragmentSource) {
    GLuint vertexShader = loadShader(GL_VERTEX_SHADER, pVertexSource);
    if (!vertexShader) {
        return 0;
    }

    GLuint pixelShader = loadShader(GL_FRAGMENT_SHADER, pFragmentSource);
    if (!pixelShader) {
        return 0;
    }

    GLuint program = glCreateProgram();
    if (program) {
        glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");
        glAttachShader(program, pixelShader);
        checkGlError("glAttachShader");
        glLinkProgram(program);
        GLint linkStatus = GL_FALSE;
        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
        if (linkStatus != GL_TRUE) {
            GLint bufLength = 0;
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength) {
                char* buf = (char*) malloc((size_t)bufLength);
                if (buf) {
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
                    LOG_E("Could not link program:\n%s\n", buf);
                    free(buf);
                }
            }
            glDeleteProgram(program);
            program = 0;
        }
    }
    return program;
}


void frustumM(GLfloat m[16], GLint offset, GLfloat left, GLfloat right,
              GLfloat bottom, GLfloat top, GLfloat near, GLfloat far) {
    assert(left != right);
    assert(top != bottom);
    assert(near != far);
    assert(near > 0.0);
    assert(far > 0.0);

    GLfloat r_width  = 1.0f / (right - left);
    GLfloat r_height = 1.0f / (top - bottom);
    GLfloat r_depth  = 1.0f / (near - far);
    GLfloat x = 2.0f * (near * r_width);
    GLfloat y = 2.0f * (near * r_height);
    GLfloat A = (right + left) * r_width;
    GLfloat B = (top + bottom) * r_height;
    GLfloat C = (far + near) * r_depth;
    GLfloat D = 2.0f * (far * near * r_depth);
    m[offset + 0] = x;
    m[offset + 5] = y;
    m[offset + 8] = A;
    m[offset +  9] = B;
    m[offset + 10] = C;
    m[offset + 14] = D;
    m[offset + 11] = -1.0f;
    m[offset +  1] = 0.0f;
    m[offset +  2] = 0.0f;
    m[offset +  3] = 0.0f;
    m[offset +  4] = 0.0f;
    m[offset +  6] = 0.0f;
    m[offset +  7] = 0.0f;
    m[offset + 12] = 0.0f;
    m[offset + 13] = 0.0f;
    m[offset + 15] = 0.0f;
}

void transposeM(GLfloat mTrans[], GLint mTransOffset, GLfloat m[], GLint mOffset) {
    for (GLint i = 0; i < 4; i++) {
        GLint mBase = i * 4 + mOffset;
        mTrans[i + mTransOffset] = m[mBase];
        mTrans[i + 4 + mTransOffset] = m[mBase + 1];
        mTrans[i + 8 + mTransOffset] = m[mBase + 2];
        mTrans[i + 12 + mTransOffset] = m[mBase + 3];
    }
}

GLboolean invertM(GLfloat mInv[16], GLint mInvOffset, GLfloat m[],
                GLint mOffset) {
    // Invert a 4 x 4 matrix using Cramer's Rule

    // transpose matrix
    GLfloat src0  = m[mOffset +  0];
    GLfloat src4  = m[mOffset +  1];
    GLfloat src8  = m[mOffset +  2];
    GLfloat src12 = m[mOffset +  3];

    GLfloat src1  = m[mOffset +  4];
    GLfloat src5  = m[mOffset +  5];
    GLfloat src9  = m[mOffset +  6];
    GLfloat src13 = m[mOffset +  7];

    GLfloat src2  = m[mOffset +  8];
    GLfloat src6  = m[mOffset +  9];
    GLfloat src10 = m[mOffset + 10];
    GLfloat src14 = m[mOffset + 11];

    GLfloat src3  = m[mOffset + 12];
    GLfloat src7  = m[mOffset + 13];
    GLfloat src11 = m[mOffset + 14];
    GLfloat src15 = m[mOffset + 15];

    // calculate pairs for first 8 elements (cofactors)
    GLfloat atmp0  = src10 * src15;
    GLfloat atmp1  = src11 * src14;
    GLfloat atmp2  = src9  * src15;
    GLfloat atmp3  = src11 * src13;
    GLfloat atmp4  = src9  * src14;
    GLfloat atmp5  = src10 * src13;
    GLfloat atmp6  = src8  * src15;
    GLfloat atmp7  = src11 * src12;
    GLfloat atmp8  = src8  * src14;
    GLfloat atmp9  = src10 * src12;
    GLfloat atmp10 = src8  * src13;
    GLfloat atmp11 = src9  * src12;

    // calculate first 8 elements (cofactors)
    GLfloat dst0  = (atmp0 * src5 + atmp3 * src6 + atmp4  * src7)
                        - (atmp1 * src5 + atmp2 * src6 + atmp5  * src7);
    GLfloat dst1  = (atmp1 * src4 + atmp6 * src6 + atmp9  * src7)
                        - (atmp0 * src4 + atmp7 * src6 + atmp8  * src7);
    GLfloat dst2  = (atmp2 * src4 + atmp7 * src5 + atmp10 * src7)
                        - (atmp3 * src4 + atmp6 * src5 + atmp11 * src7);
    GLfloat dst3  = (atmp5 * src4 + atmp8 * src5 + atmp11 * src6)
                        - (atmp4 * src4 + atmp9 * src5 + atmp10 * src6);
    GLfloat dst4  = (atmp1 * src1 + atmp2 * src2 + atmp5  * src3)
                        - (atmp0 * src1 + atmp3 * src2 + atmp4  * src3);
    GLfloat dst5  = (atmp0 * src0 + atmp7 * src2 + atmp8  * src3)
                        - (atmp1 * src0 + atmp6 * src2 + atmp9  * src3);
    GLfloat dst6  = (atmp3 * src0 + atmp6 * src1 + atmp11 * src3)
                        - (atmp2 * src0 + atmp7 * src1 + atmp10 * src3);
    GLfloat dst7  = (atmp4 * src0 + atmp9 * src1 + atmp10 * src2)
                        - (atmp5 * src0 + atmp8 * src1 + atmp11 * src2);

    // calculate pairs for second 8 elements (cofactors)
    GLfloat btmp0  = src2 * src7;
    GLfloat btmp1  = src3 * src6;
    GLfloat btmp2  = src1 * src7;
    GLfloat btmp3  = src3 * src5;
    GLfloat btmp4  = src1 * src6;
    GLfloat btmp5  = src2 * src5;
    GLfloat btmp6  = src0 * src7;
    GLfloat btmp7  = src3 * src4;
    GLfloat btmp8  = src0 * src6;
    GLfloat btmp9  = src2 * src4;
    GLfloat btmp10 = src0 * src5;
    GLfloat btmp11 = src1 * src4;

    // calculate second 8 elements (cofactors)
    GLfloat dst8  = (btmp0  * src13 + btmp3  * src14 + btmp4  * src15)
                        - (btmp1  * src13 + btmp2  * src14 + btmp5  * src15);
    GLfloat dst9  = (btmp1  * src12 + btmp6  * src14 + btmp9  * src15)
                        - (btmp0  * src12 + btmp7  * src14 + btmp8  * src15);
    GLfloat dst10 = (btmp2  * src12 + btmp7  * src13 + btmp10 * src15)
                        - (btmp3  * src12 + btmp6  * src13 + btmp11 * src15);
    GLfloat dst11 = (btmp5  * src12 + btmp8  * src13 + btmp11 * src14)
                        - (btmp4  * src12 + btmp9  * src13 + btmp10 * src14);
    GLfloat dst12 = (btmp2  * src10 + btmp5  * src11 + btmp1  * src9 )
                        - (btmp4  * src11 + btmp0  * src9  + btmp3  * src10);
    GLfloat dst13 = (btmp8  * src11 + btmp0  * src8  + btmp7  * src10)
                        - (btmp6  * src10 + btmp9  * src11 + btmp1  * src8 );
    GLfloat dst14 = (btmp6  * src9  + btmp11 * src11 + btmp3  * src8 )
                        - (btmp10 * src11 + btmp2  * src8  + btmp7  * src9 );
    GLfloat dst15 = (btmp10 * src10 + btmp4  * src8  + btmp9  * src9 )
                        - (btmp8  * src9  + btmp11 * src10 + btmp5  * src8 );

    // calculate determinant
    GLfloat det =
            src0 * dst0 + src1 * dst1 + src2 * dst2 + src3 * dst3;

    if (det == 0.0f) {
        return GL_FALSE;
    }

    // calculate matrix inverse
    GLfloat invdet = 1.0f / det;
    mInv[     mInvOffset] = dst0  * invdet;
    mInv[ 1 + mInvOffset] = dst1  * invdet;
    mInv[ 2 + mInvOffset] = dst2  * invdet;
    mInv[ 3 + mInvOffset] = dst3  * invdet;

    mInv[ 4 + mInvOffset] = dst4  * invdet;
    mInv[ 5 + mInvOffset] = dst5  * invdet;
    mInv[ 6 + mInvOffset] = dst6  * invdet;
    mInv[ 7 + mInvOffset] = dst7  * invdet;

    mInv[ 8 + mInvOffset] = dst8  * invdet;
    mInv[ 9 + mInvOffset] = dst9  * invdet;
    mInv[10 + mInvOffset] = dst10 * invdet;
    mInv[11 + mInvOffset] = dst11 * invdet;

    mInv[12 + mInvOffset] = dst12 * invdet;
    mInv[13 + mInvOffset] = dst13 * invdet;
    mInv[14 + mInvOffset] = dst14 * invdet;
    mInv[15 + mInvOffset] = dst15 * invdet;

    return GL_TRUE;
}

void orthoM(GLfloat m[16], GLint mOffset,
            GLfloat left, GLfloat right, GLfloat bottom, GLfloat top,
            GLfloat near, GLfloat far) {
    
    assert(left != right);
    assert(bottom != top);
    assert(near != far);

    GLfloat r_width  = 1.0f / (right - left);
    GLfloat r_height = 1.0f / (top - bottom);
    GLfloat r_depth  = 1.0f / (far - near);
    GLfloat x =  2.0f * (r_width);
    GLfloat y =  2.0f * (r_height);
    GLfloat z = -2.0f * (r_depth);
    GLfloat tx = -(right + left) * r_width;
    GLfloat ty = -(top + bottom) * r_height;
    GLfloat tz = -(far + near) * r_depth;
    m[mOffset + 0] = x;
    m[mOffset + 5] = y;
    m[mOffset +10] = z;
    m[mOffset +12] = tx;
    m[mOffset +13] = ty;
    m[mOffset +14] = tz;
    m[mOffset +15] = 1.0f;
    m[mOffset + 1] = 0.0f;
    m[mOffset + 2] = 0.0f;
    m[mOffset + 3] = 0.0f;
    m[mOffset + 4] = 0.0f;
    m[mOffset + 6] = 0.0f;
    m[mOffset + 7] = 0.0f;
    m[mOffset + 8] = 0.0f;
    m[mOffset + 9] = 0.0f;
    m[mOffset + 11] = 0.0f;
}

void perspectiveM(GLfloat m[16], GLint offset,
                  GLfloat fovy, GLfloat aspect, GLfloat zNear, GLfloat zFar) {
    GLfloat f = 1.0f / (GLfloat) tan(fovy * (M_PI / 360.0));
    GLfloat rangeReciprocal = 1.0f / (zNear - zFar);

    m[offset + 0] = f / aspect;
    m[offset + 1] = 0.0f;
    m[offset + 2] = 0.0f;
    m[offset + 3] = 0.0f;

    m[offset + 4] = 0.0f;
    m[offset + 5] = f;
    m[offset + 6] = 0.0f;
    m[offset + 7] = 0.0f;

    m[offset + 8] = 0.0f;
    m[offset + 9] = 0.0f;
    m[offset + 10] = (zFar + zNear) * rangeReciprocal;
    m[offset + 11] = -1.0f;

    m[offset + 12] = 0.0f;
    m[offset + 13] = 0.0f;
    m[offset + 14] = 2.0f * zFar * zNear * rangeReciprocal;
    m[offset + 15] = 0.0f;
}

GLfloat length(GLfloat x, GLfloat y, GLfloat z) {
    return (GLfloat) sqrt(x * x + y * y + z * z);
}

void scaleM(GLfloat sm[], GLint smOffset,
            GLfloat m[], GLint mOffset,
            GLfloat x, GLfloat y, GLfloat z) {
    for (GLint i=0 ; i<4 ; i++) {
        GLint smi = smOffset + i;
        GLint mi = mOffset + i;
        sm[     smi] = m[     mi] * x;
        sm[ 4 + smi] = m[ 4 + mi] * y;
        sm[ 8 + smi] = m[ 8 + mi] * z;
        sm[12 + smi] = m[12 + mi];
    }
}

void scaleM(GLfloat m[], GLint mOffset,
            GLfloat x, GLfloat y, GLfloat z) {
    for (GLint i=0 ; i<4 ; i++) {
        GLint mi = mOffset + i;
        m[     mi] *= x;
        m[ 4 + mi] *= y;
        m[ 8 + mi] *= z;
    }
}

void translateM(GLfloat m[], int mOffset, GLfloat x, GLfloat y, GLfloat z) {
    for (int i=0 ; i<4 ; i++) {
        int mi = mOffset + i;
        m[12 + mi] += m[mi] * x + m[4 + mi] * y + m[8 + mi] * z;
    }
}
void setLookAtM(GLfloat rm[16], int rmOffset, GLfloat eyeX, GLfloat eyeY, GLfloat eyeZ,
                GLfloat centerX, GLfloat centerY, GLfloat centerZ, GLfloat upX, GLfloat upY, GLfloat upZ) {

    GLfloat fx = centerX - eyeX;
    GLfloat fy = centerY - eyeY;
    GLfloat fz = centerZ - eyeZ;

    // Normalize f
    GLfloat rlf = 1.0f / length(fx, fy, fz);
    fx *= rlf;
    fy *= rlf;
    fz *= rlf;

    // compute s = f x up (x means "cross product")
    GLfloat sx = fy * upZ - fz * upY;
    GLfloat sy = fz * upX - fx * upZ;
    GLfloat sz = fx * upY - fy * upX;

    // and normalize s
    GLfloat rls = 1.0f / length(sx, sy, sz);
    sx *= rls;
    sy *= rls;
    sz *= rls;

    // compute u = s x f
    GLfloat ux = sy * fz - sz * fy;
    GLfloat uy = sz * fx - sx * fz;
    GLfloat uz = sx * fy - sy * fx;

    rm[rmOffset + 0] = sx;
    rm[rmOffset + 1] = ux;
    rm[rmOffset + 2] = -fx;
    rm[rmOffset + 3] = 0.0f;

    rm[rmOffset + 4] = sy;
    rm[rmOffset + 5] = uy;
    rm[rmOffset + 6] = -fy;
    rm[rmOffset + 7] = 0.0f;

    rm[rmOffset + 8] = sz;
    rm[rmOffset + 9] = uz;
    rm[rmOffset + 10] = -fz;
    rm[rmOffset + 11] = 0.0f;

    rm[rmOffset + 12] = 0.0f;
    rm[rmOffset + 13] = 0.0f;
    rm[rmOffset + 14] = 0.0f;
    rm[rmOffset + 15] = 1.0f;

    translateM(rm, rmOffset, -eyeX, -eyeY, -eyeZ);
}

void multiplyMM(GLfloat *result, int resultOffset, GLfloat *lhs, int lhsOffset, GLfloat *rhs, int rhsOffset) {
    for (int i = 0; i < 4; ++i) {
        for (int j = 0; j < 4; ++j) {
            GLfloat total = 0.0f;
            for (int k = 0; k < 4; ++k) {
                total += rhs[lhsOffset + i * 4 + k] * lhs[rhsOffset + j + k * 4];
            }
            result[resultOffset + i * 4 + j] = total;
        }
    }
}

void setRotateM(GLfloat rm[], int rmOffset,
                GLfloat a, GLfloat x, GLfloat y, GLfloat z) {
    rm[rmOffset + 3] = 0;
    rm[rmOffset + 7] = 0;
    rm[rmOffset + 11]= 0;
    rm[rmOffset + 12]= 0;
    rm[rmOffset + 13]= 0;
    rm[rmOffset + 14]= 0;
    rm[rmOffset + 15]= 1;
    a *= (float) (M_PI / 180.0f);
    GLfloat s = sin(a);
    GLfloat c = cos(a);
    if (1.0f == x && 0.0f == y && 0.0f == z) {
        rm[rmOffset + 5] = c;   rm[rmOffset + 10]= c;
        rm[rmOffset + 6] = s;   rm[rmOffset + 9] = -s;
        rm[rmOffset + 1] = 0;   rm[rmOffset + 2] = 0;
        rm[rmOffset + 4] = 0;   rm[rmOffset + 8] = 0;
        rm[rmOffset + 0] = 1;
    } else if (0.0f == x && 1.0f == y && 0.0f == z) {
        rm[rmOffset + 0] = c;   rm[rmOffset + 10]= c;
        rm[rmOffset + 8] = s;   rm[rmOffset + 2] = -s;
        rm[rmOffset + 1] = 0;   rm[rmOffset + 4] = 0;
        rm[rmOffset + 6] = 0;   rm[rmOffset + 9] = 0;
        rm[rmOffset + 5] = 1;
    } else if (0.0f == x && 0.0f == y && 1.0f == z) {
        rm[rmOffset + 0] = c;   rm[rmOffset + 5] = c;
        rm[rmOffset + 1] = s;   rm[rmOffset + 4] = -s;
        rm[rmOffset + 2] = 0;   rm[rmOffset + 6] = 0;
        rm[rmOffset + 8] = 0;   rm[rmOffset + 9] = 0;
        rm[rmOffset + 10]= 1;
    } else {
        GLfloat len = length(x, y, z);
        if (1.0f != len) {
            GLfloat recipLen = 1.0f / len;
            x *= recipLen;
            y *= recipLen;
            z *= recipLen;
        }
        GLfloat nc = 1.0f - c;
        GLfloat xy = x * y;
        GLfloat yz = y * z;
        GLfloat zx = z * x;
        GLfloat xs = x * s;
        GLfloat ys = y * s;
        GLfloat zs = z * s;
        rm[rmOffset +  0] = x*x*nc +  c;
        rm[rmOffset +  4] =  xy*nc - zs;
        rm[rmOffset +  8] =  zx*nc + ys;
        rm[rmOffset +  1] =  xy*nc + zs;
        rm[rmOffset +  5] = y*y*nc +  c;
        rm[rmOffset +  9] =  yz*nc - xs;
        rm[rmOffset +  2] =  zx*nc - ys;
        rm[rmOffset +  6] =  yz*nc + xs;
        rm[rmOffset + 10] = z*z*nc +  c;
    }
}

void rotateM(float m[], int mOffset, float a, float x, float y, float z) {
    setRotateM(tmp, 0, a, x, y, z);
    multiplyMM(tmp, 16, m, mOffset, tmp, 0);
    for (int i = 0; i < 16; ++i) {
        m[i] = tmp[i + 16];
    }
}

void showMatrix(GLfloat *matrix) {
    LOG_I("{");
    for (int i = 0; i < 4; ++i) {
        LOG_I("%2.2f, %2.2f, %2.2f, %2.2f\n", matrix[0 + i*4], matrix[1 + i*4], matrix[2 + i*4], matrix[3 + i*4]);
    }
    LOG_I("}");
}