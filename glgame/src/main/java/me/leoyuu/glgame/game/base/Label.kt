package me.leoyuu.glgame.game.base

import android.graphics.*
import android.text.TextPaint
import me.leoyuu.glgame.game.GlShader

/**
 * date 2019/1/3
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class Label(glShader: GlShader) : Sprite(glShader) {
    private val paint = TextPaint()
    init {
        paint.color = Color.WHITE
        paint.textSize = 20f
        paint.isAntiAlias = true
    }

    fun setText(text:String, texture:Int) {
        val bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawText(text, 0f, 100f, paint)
        this.texture = Texture(bmp, texture)
    }
}
