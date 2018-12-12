package me.leoyuu.opengl.jni

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.gl_view.*
import me.leoyuu.opengl.R

/**
 * date 2018/12/8
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class JniActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gl_view)
        JniGl.nativeInitType(JniGl.INIT_TYPE_CUBE);
        val render = JniRender()
        gl.setEGLContextClientVersion(2)
        gl.setRenderer(render)
        gl.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        gl.setOnClickListener {
            JniGl.nativeClickBtn(JniGl.BTN_A)
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
        if (isFinishing) {
            JniGl.nativeDone()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
