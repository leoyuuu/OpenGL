package me.leoyuu.glgame.game

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.opengl.GLES20
import androidx.core.content.ContextCompat
import me.leoyuu.glgame.R
import me.leoyuu.glgame.game.base.Label
import me.leoyuu.glgame.game.base.Sprite
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
    private val label = Label(basePicShader)

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        initShader()
        initTime()
        initTextures()
        initLabel()
        initNodes()
    }

    private fun initLabel() {
        label.setText("Hello, world", textures[0])
        nodes.add(label)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        label.x = (width - label.width) / 2
        label.y = (height - label.height) / 2
        label.matrix = matrix
    }

    override fun getTextureNum(): Int {
        return 1
    }
}
