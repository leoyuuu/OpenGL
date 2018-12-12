package me.leoyuu.opengl.nrenders.r08cone

import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.Matrix
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
class ConeRender(private val pointNum:Int = 30) : BasePreRender() {
    private val vNumber = pointNum + 2
    private var p = 0
    private var vPosition = 0
    private var uMatrix = 0
    private val vertexBuf = FloatArray(vNumber * 3).apply {
        this[0] = 0f
        this[1] = 0f
        this[2] = 0.8f
        val dRad = 2 * Math.PI / pointNum
        for (i in 1 .. (pointNum+1)) {
            val index = i*3
            this[index] = (0.5 * Math.cos(i * dRad)).toFloat()
            this[index+1] = (0.5 * Math.sin(i * dRad)).toFloat()
            this[index+2] = 0f
        }
    }.let {
        ByteBuffer.allocateDirect(it.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
            .apply {
                put(it)
                position(0)
            }
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(0f, 1f, 1f, 1f)
        p = createProgram(R.raw.glsl_r08_v, R.raw.glsl_r08_f)
        vPosition = GLES20.glGetAttribLocation(p, "vPosition")
        uMatrix = GLES20.glGetUniformLocation(p, "uMatrix")
        glEnable(GLES20.GL_DEPTH_TEST)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        val ratio= width.toFloat()/height
        // 透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f);
        // 相机位置
        Matrix.setLookAtM(mViewMatrix, 0, -2.0f, 3f, 1.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        //
        Matrix.multiplyMM(matrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
        glUseProgram(p)

        glUniformMatrix4fv(uMatrix, 1, false, matrix, 0)

        glVertexAttribPointer(vPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuf)
        glEnableVertexAttribArray(vPosition)
        glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vNumber)
    }
}
