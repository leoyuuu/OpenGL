package me.leoyuu.glgame.game.base

import android.graphics.*
import android.text.TextPaint
import me.leoyuu.glgame.game.GlShader
import me.leoyuu.utils.dp2px

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
        paint.textSize = dp2px(14).toFloat()
        paint.isAntiAlias = true
    }

    fun setText(text:String, texture:Int) {
        val w = paint.measureText(text)
        val bmp = Bitmap.createBitmap((w + 10).toInt(), (paint.textSize * 2).toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawText(text, 5f, paint.textSize, paint)
        this.texture = Texture(bmp, texture)
    }
}
