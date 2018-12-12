package me.leoyuu.opengl.esv3

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.gl_view.*
import me.leoyuu.opengl.R

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
        gl.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        gl.setOnClickListener {
            render.click()
            gl.requestRender()
        }
    }

    override fun onResume() {
        super.onResume()
        gl.onResume()
    }

    override fun onPause() {
        super.onPause()
        gl.onPause()
    }
}
