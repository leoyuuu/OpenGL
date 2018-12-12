package me.leoyuu.opengl.camera

import android.Manifest
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import kotlinx.android.synthetic.main.activity_camera.*
import me.leoyuu.opengl.R
import me.leoyuu.utils.toast

/**
 * date 2018/12/7
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class CameraActivity : AppCompatActivity() {

    private lateinit var helper: CameraHelper

    private val mutableList = mutableListOf<SurfaceTexture>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        helper = CameraHelper(this)

        gl_camera.setEGLContextClientVersion(2)
        gl_camera.setRenderer(render)
        gl_camera.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        texture.surfaceTextureListener = textureListener
    }

    override fun onPause() {
        super.onPause()
        gl_camera.onPause()
        if (isFinishing) {
            helper.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        gl_camera.onResume()
    }

    private var render = CameraRender().apply {
        onSurfaceAvailable = object :OnSurfaceAvailable {
            override fun onSurfaceAvailable(surfaceTexture: SurfaceTexture) {
                mutableList.add(surfaceTexture)
                checkSetupCamera()
            }
        }
    }

    private var textureListener = object:TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?) = true

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            mutableList.add(surface)
            checkSetupCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            if (mutableList.size == 2) {
                helper.addSurfaceTextureAndOpenCamera(mutableList)
            }
        } else {
            toast("no permission, cannot open camera")
        }
    }

    private fun checkSetupCamera() {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED) {
            if (mutableList.size == 2) {
                helper.addSurfaceTextureAndOpenCamera(mutableList)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)
        }
    }
}
