package me.leoyuu.opengl.vr

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import android.opengl.Matrix
import me.leoyuu.opengl.glsl.createProgram

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.ArrayList

class SkySphere(private val mBitmap:Bitmap) {
    private val radius = 2f

    private val angleSpan = Math.PI / 90f// 将球进行单位切分的角度
    private var vCount = 0// 顶点个数，先初始化为0

    private var mHProgram: Int = 0
    private var mHUTexture: Int = 0
    private var mHProjMatrix: Int = 0
    private var mHViewMatrix: Int = 0
    private var mHModelMatrix: Int = 0
    private var mHRotateMatrix: Int = 0
    private var mHPosition: Int = 0
    private var mHCoordinate: Int = 0

    private var textureId: Int = 0

    private val mViewMatrix = FloatArray(16)
    private val mProjectMatrix = FloatArray(16)
    private val mModelMatrix = FloatArray(16)
    private val mRotateMatrix = FloatArray(16)

    private var posBuffer: FloatBuffer? = null
    private var cooBuffer: FloatBuffer? = null

    fun create() {
        mHProgram = createProgram("vr/skysphere.vert", "vr/skysphere.frag")
        mHProjMatrix = GLES20.glGetUniformLocation(mHProgram, "uProjMatrix")
        mHViewMatrix = GLES20.glGetUniformLocation(mHProgram, "uViewMatrix")
        mHModelMatrix = GLES20.glGetUniformLocation(mHProgram, "uModelMatrix")
        mHRotateMatrix = GLES20.glGetUniformLocation(mHProgram, "uRotateMatrix")
        mHUTexture = GLES20.glGetUniformLocation(mHProgram, "uTexture")
        mHPosition = GLES20.glGetAttribLocation(mHProgram, "aPosition")
        mHCoordinate = GLES20.glGetAttribLocation(mHProgram, "aCoordinate")
        textureId = createTexture()
        calculateAttribute()
    }

    fun setSize(width: Int, height: Int) {
        //计算宽高比
        val ratio = width.toFloat() / height
        //设置透视投影
        //Matrix.frustumM(mProjectMatrix, 0, -ratio*skyRate, ratio*skyRate, -1*skyRate, 1*skyRate, 1, 200);
        //透视投影矩阵/视锥
        perspectiveM(mProjectMatrix, 0, 45f, ratio, 1f, 300f)
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0f, 1.0f, 0.0f)
        //模型矩阵
        Matrix.setIdentityM(mModelMatrix, 0)
        //Matrix.scaleM(mModelMatrix,0,2,2,2);
    }

    fun setMatrix(matrix: FloatArray) {
        System.arraycopy(matrix, 0, mRotateMatrix, 0, 16)
    }

    fun draw() {

        GLES20.glUseProgram(mHProgram)
        GLES20.glUniformMatrix4fv(mHProjMatrix, 1, false, mProjectMatrix, 0)
        GLES20.glUniformMatrix4fv(mHViewMatrix, 1, false, mViewMatrix, 0)
        GLES20.glUniformMatrix4fv(mHModelMatrix, 1, false, mModelMatrix, 0)
        GLES20.glUniformMatrix4fv(mHRotateMatrix, 1, false, mRotateMatrix, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)

        GLES20.glEnableVertexAttribArray(mHPosition)
        GLES20.glVertexAttribPointer(mHPosition, 3, GLES20.GL_FLOAT, false, 0, posBuffer)
        GLES20.glEnableVertexAttribArray(mHCoordinate)
        GLES20.glVertexAttribPointer(mHCoordinate, 2, GLES20.GL_FLOAT, false, 0, cooBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount)

        GLES20.glDisableVertexAttribArray(mHPosition)
    }


    private fun calculateAttribute() {
        val alVertix = ArrayList<Float>()
        val textureVertix = ArrayList<Float>()
        var vAngle = 0.0
        while (vAngle < Math.PI) {

            var hAngle = 0.0
            while (hAngle < 2 * Math.PI) {
                val x0 = (radius.toDouble() * Math.sin(vAngle) * Math.cos(hAngle)).toFloat()
                val y0 = (radius.toDouble() * Math.sin(vAngle) * Math.sin(hAngle)).toFloat()
                val z0 = (radius * Math.cos(vAngle)).toFloat()

                val x1 = (radius.toDouble() * Math.sin(vAngle) * Math.cos(hAngle + angleSpan)).toFloat()
                val y1 = (radius.toDouble() * Math.sin(vAngle) * Math.sin(hAngle + angleSpan)).toFloat()
                val z1 = (radius * Math.cos(vAngle)).toFloat()

                val x2 = (radius.toDouble() * Math.sin(vAngle + angleSpan) * Math.cos(hAngle + angleSpan)).toFloat()
                val y2 = (radius.toDouble() * Math.sin(vAngle + angleSpan) * Math.sin(hAngle + angleSpan)).toFloat()
                val z2 = (radius * Math.cos(vAngle + angleSpan)).toFloat()

                val x3 = (radius.toDouble() * Math.sin(vAngle + angleSpan) * Math.cos(hAngle)).toFloat()
                val y3 = (radius.toDouble() * Math.sin(vAngle + angleSpan) * Math.sin(hAngle)).toFloat()
                val z3 = (radius * Math.cos(vAngle + angleSpan)).toFloat()

                alVertix.add(x1)
                alVertix.add(y1)
                alVertix.add(z1)
                alVertix.add(x0)
                alVertix.add(y0)
                alVertix.add(z0)
                alVertix.add(x3)
                alVertix.add(y3)
                alVertix.add(z3)

                val s0 = (hAngle / Math.PI / 2.0).toFloat()
                val s1 = ((hAngle + angleSpan) / Math.PI / 2.0).toFloat()
                val t0 = (vAngle / Math.PI).toFloat()
                val t1 = ((vAngle + angleSpan) / Math.PI).toFloat()

                textureVertix.add(s1)// x1 y1对应纹理坐标
                textureVertix.add(t0)
                textureVertix.add(s0)// x0 y0对应纹理坐标
                textureVertix.add(t0)
                textureVertix.add(s0)// x3 y3对应纹理坐标
                textureVertix.add(t1)

                alVertix.add(x1)
                alVertix.add(y1)
                alVertix.add(z1)
                alVertix.add(x3)
                alVertix.add(y3)
                alVertix.add(z3)
                alVertix.add(x2)
                alVertix.add(y2)
                alVertix.add(z2)

                textureVertix.add(s1)// x1 y1对应纹理坐标
                textureVertix.add(t0)
                textureVertix.add(s0)// x3 y3对应纹理坐标
                textureVertix.add(t1)
                textureVertix.add(s1)// x2 y3对应纹理坐标
                textureVertix.add(t1)
                hAngle = hAngle + angleSpan
            }
            vAngle = vAngle + angleSpan
        }
        vCount = alVertix.size / 3
        posBuffer = convertToFloatBuffer(alVertix)
        cooBuffer = convertToFloatBuffer(textureVertix)
    }

    private fun convertToFloatBuffer(data: ArrayList<Float>): FloatBuffer {
        val d = FloatArray(data.size)
        for (i in d.indices) {
            d[i] = data[i]
        }

        val buffer = ByteBuffer.allocateDirect(data.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        val ret = buffer.asFloatBuffer()
        ret.put(d)
        ret.position(0)
        return ret
    }

    private fun createTexture(): Int {
        val texture = IntArray(1)
        //生成纹理
        GLES20.glGenTextures(1, texture, 0)
        //生成纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0])
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST.toFloat())
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE.toFloat())
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE.toFloat())
        //根据以上指定的参数，生成一个2D纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0)
        return texture[0]
    }

    companion object {

        private val UNIT_SIZE = 1f// 单位尺寸
    }

}
