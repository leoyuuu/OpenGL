package me.leoyuu.opengl.nrenders

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_gl.*
import me.leoyuu.opengl.R
import me.leoyuu.opengl.nrenders.r02simple.SimpleRender
import java.lang.Exception

/**
 * date 2018/12/5
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class GlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkEs()) {
            setContentView(R.layout.activity_gl)
            setGl()
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkEs():Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = manager.deviceConfigurationInfo
        return configurationInfo.reqGlEsVersion >= 0x20000
    }

    private fun setGl() {
        val s = intent.getStringExtra(KEY_RENDER)
        try {
            gl_surface.setEGLContextClientVersion(2)
            val render = if (s == null) {
                SimpleRender()
            } else {
                Class.forName(s).newInstance()
            }
            if (render is GLSurfaceView.Renderer) {
                gl_surface.setRenderer(render)
                gl_surface.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
            } else {
                Toast.makeText(this, "class error: $s", Toast.LENGTH_SHORT).show()
            }
        } catch (e:Exception) {
            Log.e(TAG, "error", e)
            Toast.makeText(this, "class error: $e", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        gl_surface.onPause()
    }

    override fun onResume() {
        super.onResume()
        gl_surface.onResume()
    }

    companion object {
        private val TAG = GlActivity::class.java.simpleName
        private const val KEY_RENDER = "render_name"

        fun start(context: Context, preRender:BasePreRender) {
            val i = Intent(context, GlActivity::class.java)
            i.putExtra(KEY_RENDER, preRender.javaClass.name)
            context.startActivity(i)
        }
    }

}
