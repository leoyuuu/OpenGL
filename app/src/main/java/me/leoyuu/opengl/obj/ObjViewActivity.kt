package me.leoyuu.opengl.obj

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.gl_view.*
import me.leoyuu.opengl.R
import me.leoyuu.utils.ThreadUtil
import me.leoyuu.utils.getScreenWidth
import me.leoyuu.utils.loge

/**
 * date 2018/12/11
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class ObjViewActivity : AppCompatActivity(){
    private var renderSet = false
    lateinit var render: Base3dRender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gl_view)
        loge("start parse obj")
        val obj = Obj3d.parseFromFile(assets, "obj/girl.obj")
        loge("parse obj finished")
        render = Base3dRender(obj)
        gl.setEGLContextClientVersion(2)
        gl.setRenderer(render)
        gl.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        renderSet = true
    }

    override fun onResume() {
        super.onResume()
        if (renderSet) {
            gl.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (renderSet) {
            gl.onPause()
        }
    }

    companion object {
        private val DEFAULT_OBJ = "obj/girl.obj"
        private val DEFAULT_MTL = "obj/girl.mtl"
    }
}
