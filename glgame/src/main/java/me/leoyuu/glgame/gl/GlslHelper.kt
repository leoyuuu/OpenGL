package me.leoyuu.glgame.gl

import android.content.Context
import android.opengl.GLES20
import android.util.Log
import me.leoyuu.utils.loge
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * date 2018/12/5
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

fun textFromRawResource(context: Context, resId:Int):String {
    val br = BufferedReader(InputStreamReader(context.resources.openRawResource(resId)))
    val lines = br.readLines()
    br.close()
    return lines.joinToString(separator = "\n", truncated = "")
}

fun loadShader(context: Context, type:Int, resId: Int):Int {
    val shader = GLES20.glCreateShader(type)
    if (shader != 0) {
        val code = textFromRawResource(context, resId)
        GLES20.glShaderSource(shader, code)
        GLES20.glCompileShader(shader)
        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e("ES20_ERROR", "Could not compile shader $code:");
            Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader))
            GLES20.glDeleteShader(shader)
            return 0
        }

    } else {
        loge("glCreateShader error")
    }
    return shader
}

fun createProgramInternal(vertexShader:Int, pixelShader:Int):Int {
    val program = GLES20.glCreateProgram()
    if (program != 0) {
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, pixelShader);
        GLES20.glLinkProgram(program);
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e("ES20_ERROR", "Could not link program: ");
            Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            return 0
        }
    }
    return program;
}


fun createProgram(context: Context, vertexId:Int, fragmentId:Int):Int {
    //加载顶点着色器
    val vertexShader = loadShader(context, GLES20.GL_VERTEX_SHADER, vertexId);
    if (vertexShader == 0) {
        loge("vertex shader error")
        return 0
    }

    // 加载片元着色器
    val pixelShader = loadShader(context, GLES20.GL_FRAGMENT_SHADER, fragmentId)
    if (pixelShader == 0) {
        loge("fragment shader error")
        return 0
    }

    return createProgramInternal(vertexShader, pixelShader)
}