package me.leoyuu.opengl.ctrl.jump

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import me.leoyuu.opengl.ctrl.jump.nodes.Bg
import me.leoyuu.opengl.ctrl.jump.nodes.Jumper
import me.leoyuu.utils.loge

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/6
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class JumpRender : GLSurfaceView.Renderer {
    private val bg = Bg()
    private val jumper = Jumper()

    /** 注意绘制顺序 */
    private val shapes = listOf(
        bg,
        jumper
    )

    private var lastElapsed = 0L
    private val matrix = FloatArray(16)
    private val mProjectMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        glClearColor(0f, 1f, 1f, 1f)
        lastElapsed = SystemClock.elapsedRealtime()
        shapes.forEach { it.initProgram() }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        val ratio= width.toFloat()/height
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f);
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(matrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
        shapes.forEach { it.onSizeChange(width, height) }
        loge("surface change: ($width, $height)")
    }

    private fun checkFrame() {
        onUpdate(SystemClock.elapsedRealtime() - lastElapsed)
        lastElapsed = SystemClock.elapsedRealtime()
    }

    private fun onUpdate(dt:Long) {
        bg.dropBg(dt)
        jumper.drop(dt)
    }

    override fun onDrawFrame(gl: GL10) {
        checkFrame()
        glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)
        shapes.forEach { it.draw(matrix) }
    }

    fun onClick() {
        jumper.jump()
    }
}
