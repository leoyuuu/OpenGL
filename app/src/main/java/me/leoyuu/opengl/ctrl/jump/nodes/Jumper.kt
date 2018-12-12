package me.leoyuu.opengl.ctrl.jump.nodes

import android.opengl.GLES20.*
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
class Jumper : Node {
    private var v = -0.0001f
    private var top = 0f
    private val vertices = floatArrayOf(0f, 0f, -0.1f, -0.1f, 0.1f, -0.1f)

    private val vBuf = ByteBuffer.allocateDirect(vertices.size * 4).order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .apply {
            put(vertices)
            position(0)
        }

    private var p = 0
    private var vPosition = 0
    private var uMatrix = 0


    fun jump() {
        moveVertices(0f, 0.1f)
    }

    fun drop(dt:Long) {
        val d = v * dt

        moveVertices(0f, d)
        vBuf.position(0)
        vBuf.put(vertices)
        vBuf.position(0)
    }

    private fun moveVertices(dx:Float, dy:Float) {
        if (top + dy > 1 || top + dy < -0.9) return

        vertices[0] += dx
        vertices[2] += dx
        vertices[4] += dx
        vertices[1] += dy
        vertices[3] += dy
        vertices[5] += dy
        top += dy
    }

    override fun initProgram() {
        p = createProgram("jumper/jumper.vert", "jumper/jumper.frag")
        vPosition = glGetAttribLocation(p, "vPosition")
        uMatrix = glGetUniformLocation(p, "uMatrix")
        loge("p: $p position:$vPosition")
    }

    override fun onSizeChange(width: Int, height: Int) {}

    @GlThread
    override fun draw(matrix:FloatArray) {
        glUseProgram(p)
        glUniformMatrix4fv(uMatrix,1,false, matrix,0)
        glEnableVertexAttribArray(vPosition)
        glVertexAttribPointer(vPosition, 2, GL_FLOAT, false, 0, vBuf)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
        glDisableVertexAttribArray(vPosition)
    }
}
