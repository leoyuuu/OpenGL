package me.leoyuu.opengl

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import me.leoyuu.opengl.jni.JniGl
import me.leoyuu.utils.utilInit

/**
 * date 2018/12/5
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        utilInit(this)
        App.app = this
        JniGl.init(this)
    }

    companion object {
        lateinit var app: App
        private set

        val icBmp:Bitmap
            get() = (app.getDrawable(R.mipmap.ic_launcher) as BitmapDrawable).bitmap

        val testBmp: Bitmap
            get() {
                return (app.getDrawable(R.drawable.lybg) as BitmapDrawable).bitmap
            }

        val spaceBmp: Bitmap
            get() = (app.getDrawable(R.drawable.space) as BitmapDrawable).bitmap

        val sp360Bmp:Bitmap
            get() {
                return BitmapFactory.decodeStream(app.assets.open("vr/360sp.jpg"))
            }
    }
}
