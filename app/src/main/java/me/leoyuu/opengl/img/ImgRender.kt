package me.leoyuu.opengl.img

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import me.leoyuu.opengl.App
import me.leoyuu.opengl.glsl.createProgram
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
class ImgRender: GLSurfaceView.Renderer {

    private val bmp = App.testBmp
    private var p = 0
    private var texture = 0
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

    var renderType:Int = RENDER_TYPE_NORMAL

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)
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

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        glClearColor(0.0f, 0.5f, 0.5f, 1.0f)
        p = createProgram("img/img.vert", "img/img.frag")

        aPosition = glGetAttribLocation(p, "aPosition")
        aCoordinate = glGetAttribLocation(p, "aCoordinate")
        uMatrix = glGetUniformLocation(p, "uMatrix")
        uTexture = glGetUniformLocation(p, "uTexture")
        iType = glGetUniformLocation(p, "iType")
        uTextureSize = glGetUniformLocation(p, "uTextureSize")

        genTexture()

        loge("aPosition: $aPosition, aCoordinate:$aCoordinate, uMatrix:$uMatrix, uTexture:$uTexture, texture: $texture, iType:$iType")
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)

        if (p==0) return
        glUseProgram(p)


        glUniformMatrix4fv(uMatrix,1,false, matrix,0)
        glUniform1i(iType, renderType)
        glUniform2i(uTextureSize, bmp.width, bmp.height)
        glEnableVertexAttribArray(aPosition)
        glEnableVertexAttribArray(aCoordinate)

        glBindTexture(GL_TEXTURE_2D, texture)

        glVertexAttribPointer(aPosition, 2, GL_FLOAT, false, 0, vBuf)
        glVertexAttribPointer(aCoordinate, 2, GL_FLOAT, false, 0, cBuf)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)

        glDisableVertexAttribArray(aCoordinate)
        glDisableVertexAttribArray(aPosition)
    }


    private fun genTexture() {
        val t = IntArray(2)
        glGenTextures(2, t, 0)
        texture = t[0]
        glBindTexture(GL_TEXTURE_2D, texture)

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST.toFloat())
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR.toFloat())
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE.toFloat())
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE.toFloat())
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bmp, 0)
    }


    companion object {
        @SuppressWarnings("unused")
        const val RENDER_TYPE_NORMAL    = 0
        @SuppressWarnings("unused")
        const val RENDER_TYPE_GREY      = 1
        @SuppressWarnings("unused")
        const val RENDER_TYPE_RED       = 2
        @SuppressWarnings("unused")
        const val RENDER_TYPE_GREEN     = 3
        @SuppressWarnings("unused")
        const val RENDER_TYPE_BLUE      = 4
        @SuppressWarnings("unused")
        const val RENDER_TYPE_FLOAT     = 5
        @SuppressWarnings("unused")
        const val RENDER_TYPE_MOSAIC    = 6
        @SuppressWarnings("unused")
        const val RENDER_TYPE_FILTER    = 7
        @SuppressWarnings("unused")
        const val RENDER_TYPE_VORTEX    = 8
        @SuppressWarnings("unused")
        const val RENDER_TYPE_MAX       = RENDER_TYPE_VORTEX

    }
}
