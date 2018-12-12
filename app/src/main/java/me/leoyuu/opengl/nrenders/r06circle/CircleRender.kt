package me.leoyuu.opengl.nrenders.r06circle

import android.opengl.GLES20
import android.opengl.GLES20.*
import me.leoyuu.opengl.R
import me.leoyuu.opengl.glsl.createProgram
import me.leoyuu.opengl.nrenders.BasePreRender
import java.nio.ByteBuffer
import java.nio.ByteOrder

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/6
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class CircleRender(private val pointNum:Int = 17) : BasePreRender() {
    private val vNumber = pointNum + 2
    private var p = 0
    private var vPosition = 0
    private var uMatrix = 0
    private val vertexBuf = FloatArray(vNumber * 2).apply {
        this[0] = 0f
        this[1] = 0f
        val dRad = 2 * Math.PI / pointNum
        for (i in 1 .. (pointNum+1)) {
            val index = i*2
            this[index] = (0.5 * Math.cos(i * dRad)).toFloat()
            this[index+1] = (0.5 * Math.sin(i * dRad)).toFloat()
        }
    }.let {
        ByteBuffer.allocateDirect(it.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
            .apply {
                put(it)
                position(0)
            }
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        glClearColor(0f, 1f, 1f, 1f)
        p = createProgram(R.raw.glsl_r06_v, R.raw.glsl_r06_f)
        vPosition = GLES20.glGetAttribLocation(p, "vPosition")
        uMatrix = glGetUniformLocation(p, "uMatrix")
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
        glUseProgram(p)

        glUniformMatrix4fv(uMatrix,1,false, matrix,0)

        glVertexAttribPointer(vPosition, 2, GL_FLOAT, false, 0, vertexBuf)
        glEnableVertexAttribArray(vPosition)
        glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vNumber)
    }
}
