package me.leoyuu.opengl.img

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_img.*
import me.leoyuu.opengl.R
import me.leoyuu.utils.loge

/**
 * date 2018/12/7
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class ImgActivity : AppCompatActivity() {
    private val render = ImgRender()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img)
        gl_img.setEGLContextClientVersion(2)
        gl_img.setRenderer(render)
        gl_img.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        initEvent()
    }

    private fun initEvent() {
        gl_type.setOnClickListener {
            render.renderType++
            if (render.renderType > ImgRender.RENDER_TYPE_MAX) {
                render.renderType = ImgRender.RENDER_TYPE_NORMAL
            }
            loge("render type: ${render.renderType}")
            gl_img.requestRender()
        }
    }

    override fun onResume() {
        super.onResume()
        gl_img.onResume()
    }

    override fun onPause() {
        super.onPause()
        gl_img.onPause()
    }
}
