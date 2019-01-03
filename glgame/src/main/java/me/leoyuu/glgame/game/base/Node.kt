package me.leoyuu.glgame.game.base

import android.opengl.Matrix

/**
 * date 2019/1/3
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
abstract class Node {
    var sw = 0
    private set
    var sh = 0
    private set

    abstract fun initNode()
    abstract fun draw(dt:Long)

    fun sizeChange(w:Int, h:Int) {
        sw = w
        sh = h
    }

    fun getCollisionPoints():List<CPoint> {
        return emptyList()
    }

    companion object {
        val BASE_MATRIX = floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f)
    }
}
