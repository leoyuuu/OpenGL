package me.leoyuu.opengl.esv3

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.gl_view.*
import me.leoyuu.opengl.R
import me.leoyuu.opengl.jni.JniGl

/**
 * date 2018/12/13
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class V3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gl_view)
        gl.setEGLContextClientVersion(3)
        val render = V3Render()
        gl.setRenderer(render)
        gl.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        gl.setOnClickListener {
            render.click()
        }
    }

    override fun onResume() {
        super.onResume()
        gl.onResume()
    }

    override fun onPause() {
        super.onPause()
        gl.onPause()
        JniGl.nativeDone()
    }
}
