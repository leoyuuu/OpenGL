package me.leoyuu.glgame.game

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import me.leoyuu.glgame.R
import me.leoyuu.glgame.game.base.Node
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2019/1/2
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
abstract class GameRender(val context: Context) : GLSurfaceView.Renderer {
    val basePicShader = GlShader(context, R.raw.base_pic_frag, R.raw.base_pic_vertice)
    val nodes = mutableListOf<Node>()

    var width = 0
    private set
    var height = 0
    private set

    var textures = IntArray(1)

    private var lastDrawTime = 0L

    val matrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        initShader()
        initTime()
        initTextures()
        initNodes()
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        sizeChange(width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        drawNodes()
    }

    fun initShader() {
        basePicShader.init()
    }

    fun initTime() {
        lastDrawTime = System.currentTimeMillis()
    }

    fun initNodes() {
        nodes.forEach { it.initNode() }
    }

    fun initTextures() {
        val n = getTextureNum()
        textures = IntArray(n)
        GLES20.glGenTextures(n, textures, 0)
        textures.forEach {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, it)
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE.toFloat())
        }
    }

    open fun getTextureNum() = 1

    fun sizeChange(width:Int, height: Int) {
        this.width = width
        this.height = height
        GLES20.glViewport(0, 0, width, height)
        checkMatrix(width, height)
        nodes.forEach { it.sizeChange(width, height) }
    }

    private fun checkMatrix(width: Int, height: Int) {
        val aspectRatio = if (width > height) width.toFloat() / height else height.toFloat() / width
        if (width > height) {
            Matrix.orthoM(matrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1.0f, 1f);
        } else {
            Matrix.orthoM(matrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1.0f, 1f);
        }
    }


    fun drawNodes() {
        val dt = System.currentTimeMillis() - lastDrawTime
        lastDrawTime = System.currentTimeMillis()
        nodes.forEach { it.draw(dt) }
    }
}
