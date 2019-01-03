package me.leoyuu.glgame.game.shooter

import android.opengl.GLES20
import android.opengl.GLSurfaceView

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2019/1/2
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class ShooterRender : GLSurfaceView.Renderer {
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(1f, 1f, 1f, 1f)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {

    }

    override fun onDrawFrame(gl: GL10) {

    }
}
