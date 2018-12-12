package me.leoyuu.opengl.nrenders.r03triangle

import android.opengl.GLES20
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
class TriRender : BasePreRender() {
    private var vPosition = 0
    private var vColor = 0
    private var uMatrix = 0
    private var p = 0

    private val vertices = floatArrayOf(
        0.5f,   0.5f,
        -0.5f, -0.5f,
        0.5f,  -0.5f
    )

    private val vertexBuf: FloatBuffer

    init {
        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder())
        vertexBuf = vbb.asFloatBuffer()
        vertexBuf.put(vertices)
        vertexBuf.position(0)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
        ensureProgram()

    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        if (p==0) return

        GLES20.glUseProgram(p)
        GLES20.glUniformMatrix4fv(uMatrix,1,false, matrix,0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, vertexBuf)
        GLES20.glEnableVertexAttribArray(vPosition)
        GLES20.glUniform4f(vColor, 0.0f, 1.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3)
    }

    private fun ensureProgram() {
        p = createProgram(R.raw.glsl_r03_v, R.raw.glsl_r03_f)
        vPosition = GLES20.glGetAttribLocation(p, "vPosition")
        vColor = GLES20.glGetUniformLocation(p, "vColor")
        uMatrix = GLES20.glGetUniformLocation(p, "uMatrix")
    }
}
