package me.leoyuu.opengl.nrenders

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import me.leoyuu.opengl.list.RenderItem
import me.leoyuu.opengl.nrenders.r02simple.SimpleRender
import me.leoyuu.opengl.nrenders.r01empty.EmptyRender
import me.leoyuu.opengl.nrenders.r03triangle.TriRender
import me.leoyuu.opengl.nrenders.r04rect.RectRender
import me.leoyuu.opengl.nrenders.r05pic.BitmapRender
import me.leoyuu.opengl.nrenders.r06circle.CircleRender
import me.leoyuu.opengl.nrenders.r08cone.ConeRender
import me.leoyuu.opengl.nrenders.r07cube.CubeRender
import me.leoyuu.opengl.nrenders.r09cylinder.CylinderRender
import me.leoyuu.opengl.nrenders.r10ball.BallRender
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/5
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
abstract class BasePreRender:GLSurfaceView.Renderer{
    internal val matrix = FloatArray(16)
    internal val mProjectMatrix = FloatArray(16)
    internal val mViewMatrix = FloatArray(16)

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        checkMatrix(width, height)
    }


    private fun checkMatrix(width: Int, height: Int) {
        // 进行矩阵变换，存储 matrix 信息在 onDraw 的时候设置绘制区域的形状
        val aspectRatio = if (width > height) width.toFloat() / height else height.toFloat() / width
        if (width > height) {
            Matrix.orthoM(matrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1.0f, 1f);
        } else {
            Matrix.orthoM(matrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1.0f, 1f);
        }
    }


    companion object {

        fun getRenderList():List<RenderItem> =
            listOf(
                RenderItem(EmptyRender(), "空 render"),
                RenderItem(SimpleRender(), "简单三角形"),
                RenderItem(TriRender(), "等腰三角形"),
                RenderItem(RectRender(), "矩形"),
                RenderItem(BitmapRender(), "图片"),
                RenderItem(CircleRender(), "正 n 边形"),
                RenderItem(CubeRender(), "立方体"),
                RenderItem(ConeRender(), "圆锥"),
                RenderItem(CylinderRender(), "圆柱体（待完成）"),
                RenderItem(BallRender(), "球")
            )

    }
}