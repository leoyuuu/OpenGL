package me.leoyuu.opengl.ctrl.jump

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.gl_view.*
import me.leoyuu.opengl.R

/**
 * date 2018/12/6
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class JumpActivity : AppCompatActivity() {
    private val render = JumpRender()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gl_view)
        gl.setEGLContextClientVersion(2)
        gl.setRenderer(render)
        gl.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        gl.setOnClickListener {
            render.onClick()
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
