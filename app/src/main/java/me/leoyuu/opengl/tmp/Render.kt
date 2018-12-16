package me.leoyuu.opengl.tmp

import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import me.leoyuu.opengl.glsl.createProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/8
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class Render : GLSurfaceView.Renderer {
    private var vPosition = 0
    private var vColor = 0
    private var p = 0
    private var vao = IntArray(1)
    private var vbo = IntArray(1)

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
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
        p = createProgram("tmp/tmp.vert", "tmp/tmp.frag")
        vPosition = glGetAttribLocation(p, "vPosition")
        vColor = glGetUniformLocation(p, "vColor")

        glUniform4f(vColor, 0.0f, 1.0f, 0.0f, 1.0f)
        glGenVertexArrays(1, vao, 0)
        glBindVertexArray(vao[0])
        glGenBuffers(1, vbo, 0)
        glBindBuffer(GL_ARRAY_BUFFER, vbo[0])
        glBufferData(GL_ARRAY_BUFFER, vertices.size * 4, vertexBuf, GL_STATIC_DRAW)
        glEnableVertexAttribArray(vPosition)
        glVertexAttribPointer(vPosition, 2, GL_FLOAT, false, 0, 0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)
        glUseProgram(p)
        glBindVertexArray(vao[0])
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 3)
        glBindVertexArray(0)
    }

}
