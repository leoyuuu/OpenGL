package me.leoyuu.glgame.game

import android.content.Context
import android.opengl.GLES20
import me.leoyuu.glgame.gl.createProgram
import me.leoyuu.utils.loge
import java.nio.FloatBuffer

/**
 * date 2019/1/3
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class GlShader(val context: Context, val fid:Int, val vid:Int) {
    var program:Int = 0
    private set

    var aPosition = -1
    private set
    var aCoordinate = -1
    private set
    var uMatrix = -1
    private set
    var uTexture = -1
    private set

    fun init() {
        program = createProgram(context, vid, fid)
        aPosition = GLES20.glGetAttribLocation(program, "aPosition")
        aCoordinate = GLES20.glGetAttribLocation(program, "aCoordinate")
        uMatrix = GLES20.glGetUniformLocation(program, "uMatrix")
        uTexture = GLES20.glGetUniformLocation(program, "uTexture")
        loge("aPosition: $aPosition, aCoordinate:$aCoordinate, uMatrix:$uMatrix, uTexture:$uTexture")
    }

    fun use() {
        GLES20.glUseProgram(program)
    }

    fun glUniformMatrix4fv(matrix:FloatArray) {
        GLES20.glUniformMatrix4fv(uMatrix,1,false, matrix,0)
    }

    fun glEnableVertexAttribArray(handle:Int) {
        GLES20.glEnableVertexAttribArray(handle)
    }

    fun glVertexAttribPointer(handle: Int, size:Int, stride:Int, buffer: FloatBuffer) {
        GLES20.glVertexAttribPointer(handle, size, GLES20.GL_FLOAT, false, stride, buffer)
    }

    fun glDisableVertexAttribArray(handle: Int) {
        GLES20.glDisableVertexAttribArray(handle)
    }

    companion object {

    }
}
