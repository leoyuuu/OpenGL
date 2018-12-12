package me.leoyuu.opengl.ctrl.jump.nodes

import android.graphics.Bitmap
import android.opengl.GLES20.*
import android.opengl.GLUtils.texImage2D
import me.leoyuu.opengl.App
import me.leoyuu.opengl.anotation.GlThread
import me.leoyuu.opengl.glsl.createProgram
import me.leoyuu.utils.loge
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * date 2018/12/6
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class Bg : Node {
    private var v = -0.0001f
    private var p = 0
    private val textures = IntArray(2)

    /** 顶点坐标 */
    private val vertices0 = floatArrayOf(-1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f)
    private val vertices1 = floatArrayOf(-1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f)

    /** 纹理坐标*/
    private val sCoord = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f)

    private val v0Buf = ByteBuffer.allocateDirect(vertices0.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(vertices0)
        position(0)
    }

    private val v1Buf = ByteBuffer.allocateDirect(vertices1.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(vertices1)
        position(0)
    }

    private val cBuf = ByteBuffer.allocateDirect(sCoord.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(sCoord)
        position(0)
    }

    private var aPosition = -1
    private var aCoordinate = -1
    private var uTexture = -1
    private var uMatrix = -1
    private var height:Float = 1f
    private var width:Float = 1f

    override fun initProgram() {
        p = createProgram("jumper/bg.vert", "jumper/bg.frag")
        aPosition = glGetAttribLocation(p, "aPosition")
        aCoordinate = glGetAttribLocation(p, "aCoordinate")
        uTexture = glGetUniformLocation(p, "uTexture")
        uMatrix = glGetUniformLocation(p, "uMatrix")

        glGenTextures(2, textures, 0)
        bindTexture(textures[0], App.spaceBmp)
        bindTexture(textures[1], App.spaceBmp)

        loge("p: $p, aPosition: $aPosition, aCoordinate: $aCoordinate, uMatrix:$uMatrix , uTexture: $uTexture, textures:${textures[0]},${textures[1]}")
    }

    override fun onSizeChange(width: Int, height: Int) {
        if (width > height) {
            this.width = width.toFloat() / height
        } else {
            this.height = height.toFloat() / width
        }
        setBgProperties()
        updateBuf()
    }

    fun dropBg(dt:Long) {
        val d = dt * v
        mvVertices(vertices0, 0f, d)
        checkReset(vertices0)
        mvVertices(vertices1, 0f, d)
        checkReset(vertices1)
        updateBuf()
    }

    private fun setBgProperties() {
        muVertices(vertices0, width, height)
        muVertices(vertices1, width, height)
        mvVertices(vertices1, 0f, 2 * height)
    }

    private fun muVertices(floatArray: FloatArray, x:Float, y:Float) {
        for (i in 0 until floatArray.size) {
            floatArray[i] = if (i % 2 == 0) {
                floatArray[i] * x
            } else {
                floatArray[i] * y
            }
        }
    }

    private fun checkReset(floatArray: FloatArray) {
        if (floatArray[1] < - height) {
            mvVertices(floatArray, 0f, height * 4)
        }
    }

    private fun updateBuf() {
        v0Buf.position(0)
        v1Buf.position(0)
        v0Buf.put(vertices0)
        v1Buf.put(vertices1)
        v0Buf.position(0)
        v1Buf.position(0)
    }

    private fun mvVertices(floatArray: FloatArray, x:Float, y:Float) {
        for (i in 0 until floatArray.size) {
            floatArray[i] = if (i % 2 == 0) {
                floatArray[i] + x
            } else {
                floatArray[i] + y
            }
        }
    }

    @GlThread
    override fun draw(matrix:FloatArray) {
        glUseProgram(p)
        glEnableVertexAttribArray(aPosition)
        glEnableVertexAttribArray(aCoordinate)
        glUniformMatrix4fv(uMatrix,1,false, matrix,0)
        glUniform1i(uTexture, 0)
        glBindTexture(GL_TEXTURE_2D, textures[0])
        glVertexAttribPointer(aPosition, 2, GL_FLOAT, false, 0, v0Buf)
        glVertexAttribPointer(aCoordinate, 2, GL_FLOAT, false, 0, cBuf)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
        glBindTexture(GL_TEXTURE_2D, textures[1])
        glVertexAttribPointer(aPosition, 2, GL_FLOAT, false, 0, v1Buf)
        glVertexAttribPointer(aCoordinate, 2, GL_FLOAT, false, 0, cBuf)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
        glDisableVertexAttribArray(aCoordinate)
        glDisableVertexAttribArray(aPosition)
    }

    private fun bindTexture(textureHandler:Int, bmp:Bitmap) {
        glBindTexture(GL_TEXTURE_2D, textureHandler)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST.toFloat())
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR.toFloat())
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE.toFloat())
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE.toFloat())
        texImage2D(GL_TEXTURE_2D, 0, bmp, 0)
    }
}
