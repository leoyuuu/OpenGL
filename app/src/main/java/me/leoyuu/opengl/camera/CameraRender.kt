package me.leoyuu.opengl.camera

import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.Matrix
import me.leoyuu.opengl.glsl.createProgram
import me.leoyuu.opengl.jni.JniGl
import me.leoyuu.utils.loge
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/7
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class CameraRender: Render {
    private var surfaceTexture:SurfaceTexture? = null

    var onSurfaceAvailable:OnSurfaceAvailable? = null

    init {
        JniGl.nativeInitType(JniGl.INIT_TYPE_CAMERA)
    }

    override fun onDrawFrame(gl: GL10?) {
        surfaceTexture?.updateTexImage()
        JniGl.nativeRender()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        JniGl.nativeResize(width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        JniGl.nativeInit()
        notifyTexture()
    }

    private fun notifyTexture() {
        val textureId = JniGl.nativeCameraRenderGetTexture()
        val surfaceTexture = SurfaceTexture(textureId)
        this.surfaceTexture = surfaceTexture
        onSurfaceAvailable?.onSurfaceAvailable(surfaceTexture)
    }
}
