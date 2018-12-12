package me.leoyuu.opengl.tmp

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * date 2018/12/8
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
fun FloatArray.toFloatBuf():FloatBuffer {
    return ByteBuffer.allocateDirect(4 * this.size).order(ByteOrder.nativeOrder()).asFloatBuffer()
        .apply { put(this@toFloatBuf).position(0) }
}