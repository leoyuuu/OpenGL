package me.leoyuu.glgame.game.base

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import java.lang.Exception

/**
 * date 2019/1/3
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
data class Texture (
    val bmp:Bitmap,
    var textureId:Int = 0
) {
    val width = bmp.width
    val height = bmp.height

    fun release() {
        if (textureId != 0) {
            val textureIds = IntArray(1)
            textureIds[0] = textureId
            GLES20.glDeleteTextures(textureIds.size, textureIds, 0)
            GLES20.glFlush()
            textureId = 0
        }
        if (!bmp.isRecycled) {
            bmp.recycle()
        }
    }
    companion object {
        fun createTexture(path:String):Bitmap? {
            return try {
                BitmapFactory.decodeFile(path)
            } catch (e:Exception) {
                null
            }
        }
    }
}
