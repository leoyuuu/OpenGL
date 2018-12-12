package me.leoyuu.opengl.tmp

import android.opengl.GLES20.*
import me.leoyuu.utils.loge

/**
 * date 2018/12/8
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class Shape(val program: Int, val name: String, val size:Int, val vertices: FloatArray) {
    val id = glGetAttribLocation(program, name)
    val bufData = vertices.toFloatBuf()
    private val bufId:Int
    init {
        val buf = IntArray(1)
        glGenBuffers(1, buf, 0)
        bufId = buf[0]
        glBindBuffer(GL_ARRAY_BUFFER, bufId)
        glBufferData(GL_ARRAY_BUFFER, vertices.size * 4, bufData, GL_STATIC_DRAW)
        glVertexAttribPointer(id, size, GL_FLOAT, false, 0, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        loge("buf id: $bufId, $name:$id")
    }

    val count = vertices.size / size
}
