package me.leoyuu.opengl.nrenders.r07cube

import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.Matrix
import android.util.Log
import me.leoyuu.opengl.R
import me.leoyuu.opengl.glsl.createProgram
import me.leoyuu.opengl.nrenders.BasePreRender
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/6
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class CubeRender : BasePreRender() {
    private var p = 0
    private var aColor = 0
    private var vPosition = 0
    private var uMatrix = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.5f, 0.5f, 1.0f)
        p = createProgram(R.raw.glsl_r07_v, R.raw.glsl_r07_f)
        aColor = glGetAttribLocation(p, "aColor")
        vPosition = glGetAttribLocation(p, "vPosition")
        uMatrix = glGetUniformLocation(p, "uMatrix")
        glEnable(GLES20.GL_DEPTH_TEST)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        val ratio= width.toFloat()/height
        // 透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f);
        // 相机位置
        Matrix.setLookAtM(mViewMatrix, 0, -5.0f, 5f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        //
        Matrix.multiplyMM(matrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
        Log.e("CubeRender", "matrix: ${matrix.toList()}")
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        glUseProgram(p)
        glUniformMatrix4fv(uMatrix,1,false, matrix,0)
        glEnableVertexAttribArray(vPosition)
        glVertexAttribPointer(vPosition, 3, GLES20.GL_FLOAT, false, 0, pBuf)
        glEnableVertexAttribArray(aColor);
        glVertexAttribPointer(aColor,4, GLES20.GL_FLOAT,false, 0, colorBuf)
        glDrawElements(GLES20.GL_TRIANGLES, 36 , GLES20.GL_UNSIGNED_SHORT, indexBuf)
        glDisableVertexAttribArray(aColor)
        glDisableVertexAttribArray(vPosition)
    }


    companion object {
        /** 立方体8个顶点*/
        private val pBuf = floatArrayOf(
            -1.0f,1.0f,1.0f,    // 正面左上0
            -1.0f,-1.0f,1.0f,   //正面左下1
            1.0f,-1.0f,1.0f,    //正面右下2
            1.0f,1.0f,1.0f,     //正面右上3

            -1.0f,1.0f,-1.0f,    //反面左上4
            -1.0f,-1.0f,-1.0f,   //反面左下5
            1.0f,-1.0f,-1.0f,    //反面右下6
            1.0f,1.0f,-1.0f     //反面右上7
        ).let {
            ByteBuffer.allocateDirect(it.size*4).order(ByteOrder.nativeOrder()).asFloatBuffer()
                .apply {
                    put(it)
                    position(0)
                }
        }

        private val indexBuf = shortArrayOf(
            0,3,2,0,2,1,    //正面
            0,1,5,0,5,4,    //左面
            0,7,3,0,4,7,    //上面
            6,7,4,6,4,5,    //后面
            6,3,7,6,2,3,    //右面
            6,5,1,6,1,2     //下面
        ).let {
            ByteBuffer.allocateDirect(it.size*2).order(ByteOrder.nativeOrder()).asShortBuffer()
                .apply {
                    put(it)
                    position(0)
                }
        }

        private val colorBuf = floatArrayOf(
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f
        ).let {
            ByteBuffer.allocateDirect(it.size*4).order(ByteOrder.nativeOrder()).asFloatBuffer()
                .apply {
                    put(it)
                    position(0)
                }
        }
    }
}
