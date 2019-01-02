package me.leoyuu.opengl.fbo

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import me.leoyuu.opengl.App
import me.leoyuu.opengl.glsl.createProgram
import me.leoyuu.opengl.img.ImgRender
import me.leoyuu.utils.loge
import java.nio.ByteBuffer
import java.nio.ByteOrder

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/25
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class FbRender : GLSurfaceView.Renderer {

    private val bmp = App.testBmp
    private var p = 0
    private var iType = -1
    private var aPosition = -1
    private var aCoordinate = -1
    private var uMatrix = -1
    private var uTexture = -1
    private var uTextureSize = -1

    private val vertices = floatArrayOf(-1.0f,1.0f, -1.0f,-1.0f, 1.0f,1.0f, 1.0f,-1.0f)
    private val sCoordinates = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f)

    private val vBuf = ByteBuffer.allocateDirect(vertices.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(vertices)
        position(0)
    }

    private val cBuf = ByteBuffer.allocateDirect(sCoordinates.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(sCoordinates)
        position(0)
    }

    private val matrix = FloatArray(16)

    var renderType:Int = ImgRender.RENDER_TYPE_NORMAL

    private val fFrame = IntArray(1)
    private val fRender = IntArray(1)
    private val fTexture = IntArray(2)

    var callback:Callback? = null

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.5f, 0.5f, 1.0f)
        p = createProgram("img/img.vert", "img/img.frag")

        aPosition = GLES20.glGetAttribLocation(p, "aPosition")
        aCoordinate = GLES20.glGetAttribLocation(p, "aCoordinate")
        uMatrix = GLES20.glGetUniformLocation(p, "uMatrix")
        uTexture = GLES20.glGetUniformLocation(p, "uTexture")
        iType = GLES20.glGetUniformLocation(p, "iType")
        uTextureSize = GLES20.glGetUniformLocation(p, "uTextureSize")

        loge("aPosition: $aPosition, aCoordinate:$aCoordinate, uMatrix:$uMatrix, uTexture:$uTexture, texture: ${fTexture[0]}, iType:$iType")
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        checkMatrix(width, height)
    }

    private fun checkMatrix(width: Int, height: Int) {
        val aspectRatio = if (width > height) width.toFloat() / height else height.toFloat() / width
        if (width > height) {
            Matrix.orthoM(matrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1.0f, 1f)
        } else {
            Matrix.orthoM(matrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1.0f, 1f)
        }
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(p)
        if (p==0) return

        createEnv()
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fFrame[0])
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D, fTexture[1], 0)
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
            GLES20.GL_RENDERBUFFER, fRender[0])


        GLES20.glUniformMatrix4fv(uMatrix, 1, false, matrix, 0)
        GLES20.glUniform1i(iType, renderType)
        GLES20.glUniform2i(uTextureSize, bmp.width, bmp.height)
        GLES20.glEnableVertexAttribArray(aPosition)
        GLES20.glEnableVertexAttribArray(aCoordinate)

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[0])

        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_FLOAT, false, 0, vBuf)
        GLES20.glVertexAttribPointer(aCoordinate, 2, GLES20.GL_FLOAT, false, 0, cBuf)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(aCoordinate)
        GLES20.glDisableVertexAttribArray(aPosition)

        val mBuffer = ByteBuffer.allocate(bmp.width * bmp.height * 4)

        GLES20.glReadPixels(0, 0, bmp.width, bmp.height, GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE, mBuffer
        )
        callback?.onCall(mBuffer, bmp.width, bmp.height)
        deleteEnv()
    }

    private fun createEnv() {
        GLES20.glGenFramebuffers(1, fFrame, 0)
        GLES20.glGenRenderbuffers(1, fRender, 0)
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, fRender[0])
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16,
            bmp.width, bmp.height
        )
        GLES20.glFramebufferRenderbuffer(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
            GLES20.GL_RENDERBUFFER, fRender[0]
        )
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0)
        GLES20.glGenTextures(2, fTexture, 0)
        for (i in 0..1) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[i])
            if (i == 0) {
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp, 0)
            } else {
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp.width, bmp.height,
                    0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null
                )
            }
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE.toFloat())
        }
    }

    private fun deleteEnv() {
        GLES20.glDeleteTextures(2, fTexture, 0)
        GLES20.glDeleteRenderbuffers(1, fRender, 0)
        GLES20.glDeleteFramebuffers(1, fFrame, 0)
    }


    interface Callback {
        fun onCall(data: ByteBuffer, width:Int, height:Int)
    }
}
