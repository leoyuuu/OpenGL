package me.leoyuu.opengl.nrenders.r02simple

import android.opengl.GLES20
import android.opengl.GLES20.*
import me.leoyuu.opengl.R
import me.leoyuu.opengl.glsl.createProgram
import me.leoyuu.opengl.nrenders.BasePreRender
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/5
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class SimpleRender : BasePreRender() {
    private var vPosition = 0
    private var vColor = 0
    private var p = 0
    private val vertices = floatArrayOf(
        0.0f,   0.5f,
        -0.5f, -0.5f,
        0.5f,  -0.5f
    )
    private val vertexBuf:FloatBuffer

    init {
        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder())
        vertexBuf = vbb.asFloatBuffer()
        vertexBuf.put(vertices)
        vertexBuf.position(0)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
        ensureProgram()

    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)

        if (p==0) return

        glUseProgram(p)
        glEnableVertexAttribArray(vPosition)
        glVertexAttribPointer(vPosition, 2, GL_FLOAT, false, 0, vertexBuf)
        glUniform4f(vColor, 0.0f, 1.0f, 0.0f, 1.0f)
        glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3)
    }

    private fun ensureProgram() {
        p = createProgram(R.raw.glsl_r02_v, R.raw.glsl_r02_f)
        vPosition = GLES20.glGetAttribLocation(p, "vPosition")
        vColor = GLES20.glGetUniformLocation(p, "vColor")

    }
}
