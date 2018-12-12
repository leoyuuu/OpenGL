package me.leoyuu.opengl.tmp

import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import me.leoyuu.opengl.BuildConfig
import me.leoyuu.opengl.glsl.createProgram
import me.leoyuu.utils.loge

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/8
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class Render : GLSurfaceView.Renderer {

    private lateinit var shape:Shape

    private var p = -1
    private var vColor = -1

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        glClearColor(0f, 1f, 1f, 1f)
        p = createProgram("tmp/tmp.vert", "tmp/tmp.frag")
        vColor = glGetUniformLocation(p, "uColor")
        shape = Shape(p, "vPosition", 2, floatArrayOf(0.0f, 0.5f, -0.5f, -0.5f, 0.5f,  -0.5f))
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)

        glUseProgram(p)
        glUniform4f(vColor, 0.0f, 1.0f, 0.0f, 1.0f)

        glEnableVertexAttribArray(shape.id)
        glDrawArrays(GLES20.GL_TRIANGLES, 0, shape.count)
        glDisableVertexAttribArray(shape.id)
        glFlush()
    }
}
