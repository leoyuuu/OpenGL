package me.leoyuu.glgame.game.base

import android.opengl.GLES20
import android.opengl.GLUtils
import me.leoyuu.glgame.game.GlShader
import java.nio.ByteBuffer
import java.nio.ByteOrder


/**
 * date 2019/1/3
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
open class Sprite(val glShader: GlShader) : Node() {

    /** 顶点坐标 */
    private val vertices = floatArrayOf(-1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f)

    /** 纹理坐标*/
    private val sCoord = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f)

    private val vBuf = ByteBuffer.allocateDirect(vertices.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(vertices)
        position(0)
    }

    private val cBuf = ByteBuffer.allocateDirect(sCoord.size * 4).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(sCoord)
        position(0)
    }

    var matrix:FloatArray = BASE_MATRIX

    var width: Float = 0f
    var height: Float = 0f
    var x: Float = 0f
    var y: Float = 0f
    var texture:Texture? = null

    override fun initNode() {
        val t = texture
        t?:return

        glShader.use()
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, t.textureId)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, t.bmp, 0)
    }

    override fun draw(dt:Long) {
        val t = texture
        t?:return

        glShader.use()
        glShader.glUniformMatrix4fv(matrix)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, t.textureId)
        glShader.glEnableVertexAttribArray(glShader.aPosition)
        glShader.glEnableVertexAttribArray(glShader.aCoordinate)
        glShader.glVertexAttribPointer(glShader.aPosition, 2, 0, vBuf)
        glShader.glVertexAttribPointer(glShader.aCoordinate, 2, 0, cBuf)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        glShader.glDisableVertexAttribArray(glShader.aPosition)
        glShader.glDisableVertexAttribArray(glShader.aCoordinate)
    }
}
