package me.leoyuu.glgame.game

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2019/1/2
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class EmptyRender : GameRender {
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {

    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {

    }

    override fun onDrawFrame(gl: GL10) {

    }
}
