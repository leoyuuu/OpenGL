package me.leoyuu.opengl.nrenders.r09cylinder

import android.opengl.GLES20
import android.opengl.GLES20.*
import me.leoyuu.opengl.nrenders.BasePreRender
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/6
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class CylinderRender() : BasePreRender() {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0f, 1f, 1f, 1f)
        glEnable(GL_DEPTH_TEST)
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
    }
}
