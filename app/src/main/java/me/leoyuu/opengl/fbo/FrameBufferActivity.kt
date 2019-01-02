package me.leoyuu.opengl.fbo

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_gl_img.*
import me.leoyuu.opengl.R
import java.nio.ByteBuffer

/**
 * date 2018/12/25
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class FrameBufferActivity : AppCompatActivity(), FbRender.Callback {
    private val render = FbRender()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gl_img)
        gl_view.setEGLContextClientVersion(2)
        gl_view.setRenderer(render)
        gl_view.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        render.callback = this
    }

    override fun onResume() {
        super.onResume()
        gl_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        gl_view.onPause()
    }

    override fun onCall(data: ByteBuffer, width:Int, height:Int) {
        runOnUiThread {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(data)
            gl_img.setImageBitmap(bitmap)
            Log.e("fbo", "set img bitmap")
        }
    }
}
