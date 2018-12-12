package me.leoyuu.opengl.jni

import android.opengl.GLSurfaceView

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/8
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class JniRender : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        JniGl.nativeInit()
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        JniGl.nativeResize(width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        JniGl.nativeRender()
    }
}
