package me.leoyuu.opengl.esv3

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import me.leoyuu.opengl.jni.JniGl

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/13
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class V3Render : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        JniGl.nativeInitType(JniGl.INIT_TYPE_V3)
        JniGl.nativeInit()
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        JniGl.nativeResize(width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        JniGl.nativeRender()
    }

    fun click() {
        JniGl.nativeClickBtn(JniGl.BTN_A)
    }
}
