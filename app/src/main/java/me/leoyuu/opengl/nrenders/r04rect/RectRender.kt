package me.leoyuu.opengl.nrenders.r04rect

import android.opengl.GLES20
import me.leoyuu.opengl.R
import me.leoyuu.opengl.glsl.createProgram
import me.leoyuu.opengl.nrenders.BasePreRender
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
class RectRender : BasePreRender() {
    private var p = 0
    private val COORD_PER_V = 4
    private val vertices = floatArrayOf(
        -0.5f, 0.5f, 0f, 0f,
        -0.5f, -0.5f, 0f, 0f,
        0.5f,  0.5f, 0f, 0f,
        0.5f,  -0.5f, 0f, 0f
    )
    private val vNumber = vertices.size / 4
    /** 4 byte per v */
    private val vStride = COORD_PER_V * 4
    private val colors = floatArrayOf(
        0.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        1.0f, 0.0f, 1.0f, 1.0f
    )
    private val cStride = COORD_PER_V * 4

    private val vertexBuf = ByteBuffer.allocateDirect(vertices.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(vertices)
        position(0)
    }

    private val colorBuf = ByteBuffer.allocateDirect(colors.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(colors)
        position(0)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.5f, 0.5f, 1.0f)
        ensureProgram()
    }


    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        if (p==0) return
        GLES20.glUseProgram(p)

        val vPosition = GLES20.glGetAttribLocation(p, "vPosition")
        val aColor = GLES20.glGetAttribLocation(p, "aColor")
        val uMatrix = GLES20.glGetUniformLocation(p, "uMatrix")

        GLES20.glUniformMatrix4fv(uMatrix,1,false, matrix,0);

        GLES20.glEnableVertexAttribArray(vPosition)
        GLES20.glEnableVertexAttribArray(aColor)

        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, vStride, vertexBuf)
        GLES20.glVertexAttribPointer(aColor, 4, GLES20.GL_FLOAT, false, cStride, colorBuf)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vNumber)

        GLES20.glDisableVertexAttribArray(aColor)
        GLES20.glDisableVertexAttribArray(vPosition)
    }

    private fun ensureProgram() {
        p = createProgram(R.raw.glsl_r04_v, R.raw.glsl_r04_f)
    }
}
