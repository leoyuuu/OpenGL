package me.leoyuu.opengl.nrenders.r05pic

import android.opengl.GLES20
import android.opengl.GLUtils
import me.leoyuu.opengl.App
import me.leoyuu.opengl.R
import me.leoyuu.opengl.glsl.createProgram
import me.leoyuu.opengl.nrenders.BasePreRender
import me.leoyuu.utils.loge
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/5
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class BitmapRender:BasePreRender() {
    private val bmp = App.testBmp
    private var p = 0
    private var texture = 0
    private var aPosition = -1
    private var aCoordinate = -1
    private var uMatrix = -1
    private var uTexture = -1

    /** 顶点坐标 */
    private val vertices = floatArrayOf(-1.0f,1.0f, -1.0f,-1.0f, 1.0f,1.0f, 1.0f,-1.0f)

    /** 纹理坐标*/
    private val sCoord = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f)

    private val vBuf = ByteBuffer.allocateDirect(vertices.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(vertices)
        position(0)
    }

    private val cBuf = ByteBuffer.allocateDirect(sCoord.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(sCoord)
        position(0)
    }


    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.5f, 0.5f, 1.0f)
        p = createProgram(R.raw.glsl_r05_v, R.raw.glsl_r05_f)

        aPosition = GLES20.glGetAttribLocation(p, "aPosition")
        aCoordinate = GLES20.glGetAttribLocation(p, "aCoordinate")
        uMatrix = GLES20.glGetUniformLocation(p, "uMatrix")
        uTexture = GLES20.glGetUniformLocation(p, "uTexture")

        genTexture()

        loge("aPosition: $aPosition, aCoordinate:$aCoordinate, uMatrix:$uMatrix, uTexture:$uTexture, texture: $texture")
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        if (p==0) return
        GLES20.glUseProgram(p)


        GLES20.glUniformMatrix4fv(uMatrix,1,false, matrix,0);

        GLES20.glEnableVertexAttribArray(aPosition)
        GLES20.glEnableVertexAttribArray(aCoordinate)
        GLES20.glUniform1i(uTexture, 0)
        showTexture()

        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_FLOAT, false, 0, vBuf)
        GLES20.glVertexAttribPointer(aCoordinate, 2, GLES20.GL_FLOAT, false, 0, cBuf)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(aCoordinate)
        GLES20.glDisableVertexAttribArray(aPosition)
    }

    private fun showTexture() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST.toFloat())
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR.toFloat())
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE.toFloat())
//设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE.toFloat())
        //根据以上指定的参数，生成一个2D纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)

    }

    private fun genTexture() {
        val t = IntArray(1)
        GLES20.glGenTextures(1, t, 0)
        texture = t[0]
    }
}
