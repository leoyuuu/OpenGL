package me.leoyuu.opengl.camera

import android.Manifest
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

/**
 * date 2018/12/11
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class CameraHelper(val context: Context) {
    companion object {
        const val TAG = "CameraHelper"
    }
    private val thread = HandlerThread(TAG)
    private val handler: Handler

    private val surfaceTextures = mutableListOf<SurfaceTexture>()

    private var cameraDevice:CameraDevice? = null

    init {
        thread.start()
        handler = Handler(thread.looper)
    }


    fun stop() {
        cameraDevice?.close()
        surfaceTextures.forEach { it.release() }
    }

    fun addSurfaceTextureAndOpenCamera(surfaceTexture: List<SurfaceTexture>) {
        surfaceTextures.addAll(surfaceTexture)
        openCamera()
    }


    private fun openCamera() {
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val id = manager.cameraIdList[0]
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED) {
                manager.openCamera(id, dscb, handler)
            }
        } catch (e:AccessDeniedException) {
            e.printStackTrace()
        }
    }


    private val dscb = object: CameraDevice.StateCallback() {

        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createSession(camera)
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
            cameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
            cameraDevice = null
        }
    }

    private fun createSession(camera: CameraDevice) {
        val builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        val previewSurfaces = surfaceTextures.map {
            val s = Surface(it)
            builder.addTarget(s)
            s
        }
        val sscb = object : CameraCaptureSession.StateCallback() {
            override fun onConfigureFailed(session: CameraCaptureSession) {}

            override fun onConfigured(session: CameraCaptureSession) {
                session.setRepeatingRequest(builder.build(), captureCallback, handler)
            }
        }
        camera.createCaptureSession(previewSurfaces, sscb, handler)
    }

    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {}
}
