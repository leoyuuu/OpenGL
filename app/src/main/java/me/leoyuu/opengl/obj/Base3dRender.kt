package me.leoyuu.opengl.obj

import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import me.leoyuu.opengl.glsl.createProgram
import me.leoyuu.utils.loge
import java.nio.ByteBuffer
import java.nio.ByteOrder

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/11
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class Base3dRender(val obj: Obj3d) : GLSurfaceView.Renderer {
    private val matrix = FloatArray(16)
    private val mProjectMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    
    private var program = 0
    private var vPosition = -1
    private var uMatrix = -1
    private var aColor = -1

    private val indices = ByteBuffer.allocateDirect(2 * obj.indices.size).order(ByteOrder.nativeOrder())
        .asShortBuffer().apply {
            put(obj.indices)
            position(0)
        }
    private val points = ByteBuffer.allocateDirect(4 * obj.vertices.size).order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply {
            put(obj.vertices)
            position(0)
        }

    private val colors = ByteBuffer.allocateDirect(4 * obj.colors.size).order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply {
            put(obj.colors)
            position(0)
        }


    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        glClearColor(0f, 0f, 0f, 0f)
        program = createProgram("obj/base3d.vert", "obj/base3d.frag")
        vPosition = GLES20.glGetAttribLocation(program, "vPosition")
        uMatrix = GLES20.glGetUniformLocation(program, "uMatrix")
        aColor = GLES20.glGetAttribLocation(program, "aColor")
        loge("render init: $program, $vPosition, $uMatrix, $aColor")

//        val ebo = IntArray(1)
//        glGenBuffers(1, ebo, 0)
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.remaining(), indices, GL_STATIC_DRAW);
//
//        val vbo = IntArray(1)
//        glGenBuffers(1, vbo, 0)
//        glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
//        glBufferData(GL_ARRAY_BUFFER, points.remaining(), points, GL_STATIC_DRAW)
//        glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0)
//        loge("buffer bound")
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        val ratio= width.toFloat()/height
        // 透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20000f);
        // 相机位置
        Matrix.setLookAtM(mViewMatrix, 0, obj.cx + obj.size * 2, 0f, 0f, obj.cx, obj.cy, obj.cz, 0f, 0.0f, -1.0f)
        //
        Matrix.multiplyMM(matrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
        Log.e("Base3dRender", "cx:${obj.cx}, cy:${obj.cy}, cz:${obj.cz}")
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)
        glUseProgram(program)
        Matrix.rotateM(matrix, 0, 1f, 0f, 0f, 1f)
        glUniformMatrix4fv(uMatrix, 1, false, matrix, 0)
        glEnableVertexAttribArray(vPosition)
        glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, points)
        glEnableVertexAttribArray(aColor)
        glVertexAttribPointer(aColor, 3, GL_FLOAT, false, 0, colors)
        glDrawElements(GL_TRIANGLES, obj.indices.size, GL_UNSIGNED_SHORT, indices)
        glDisableVertexAttribArray(vPosition)
        glDisableVertexAttribArray(aColor)
    }

}
