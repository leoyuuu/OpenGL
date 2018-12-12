package me.leoyuu.opengl.nrenders.r01empty

import me.leoyuu.opengl.nrenders.BasePreRender

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20.*

/**
 * date 2018/12/5
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class EmptyRender : BasePreRender() {

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        glClearColor(0.0f, 0.5f, 0.5f, 1.0f)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)

    }
}
