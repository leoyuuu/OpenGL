package me.leoyuu.opengl.tmp

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.gl_view.*
import me.leoyuu.opengl.R

/**
 * date 2018/12/7
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class TmpActivity : AppCompatActivity() {
    private val render = Render()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gl_view)
        gl.setEGLContextClientVersion(3)
        gl.setRenderer(render)
        gl.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }


    override fun onPause() {
        gl.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        gl.onResume()
    }
}
