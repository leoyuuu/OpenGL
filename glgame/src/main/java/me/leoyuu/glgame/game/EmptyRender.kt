package me.leoyuu.glgame.game

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import me.leoyuu.glgame.R
import me.leoyuu.glgame.game.base.Label
import me.leoyuu.glgame.game.base.Texture
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2019/1/2
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class EmptyRender(context: Context) : GameRender(context) {
    private val labelHello = Label(basePicShader)
    private val labelGamer = Label(basePicShader)

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        initShader()
        initTime()
        initTextures()
        initLabel()
        initNodes()
    }

    private fun initLabel() {
        labelHello.setText("Hello", textures[0])
        labelGamer.setText("Gamer", textures[1])
        nodes.add(labelHello)
        nodes.add(labelGamer)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        labelHello.matrix = matrix.clone()
        labelGamer.matrix = matrix.clone()
        labelHello.wrapBmpSize()
        labelGamer.wrapBmpSize()
        labelGamer.move(-1f, -1f)
    }

    override fun getTextureNum(): Int {
        return 2
    }
}
