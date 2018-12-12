package me.leoyuu.opengl.glsl

import android.opengl.GLES20
import android.util.Log
import me.leoyuu.opengl.App
import me.leoyuu.utils.loge
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * date 2018/12/5
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

fun textFromRawResource(resId:Int):String {
    val br = BufferedReader(InputStreamReader(App.app.resources.openRawResource(resId)))
    val lines = br.readLines()
    br.close()
    return lines.joinToString(separator = "\n", truncated = "")
}

fun textFromAssetsResource(path:String):String {
    val br = BufferedReader(InputStreamReader(App.app.assets.open(path)))
    val lines = br.readLines()
    br.close()
    return lines.joinToString(separator = "\n", truncated = "")
}

fun loadShader(type:Int, path: String):Int {
    val shader = GLES20.glCreateShader(type)
    if (shader != 0) {
        val code = textFromAssetsResource(path)
        GLES20.glShaderSource(shader, code)
        GLES20.glCompileShader(shader)
        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            //若编译失败则显示错误日志并删除此shader
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

fun loadShader(type:Int, resId: Int):Int {
    val shader = GLES20.glCreateShader(type)
    if (shader != 0) {
        val code = textFromRawResource(resId)
        GLES20.glShaderSource(shader, code)
        GLES20.glCompileShader(shader)
        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            //若编译失败则显示错误日志并删除此shader
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

    // 创建程序
    val program = GLES20.glCreateProgram()
    // 若程序创建成功则向程序中加入顶点着色器与片元着色器
    if (program != 0) {
        // 向程序中加入顶点着色器
        GLES20.glAttachShader(program, vertexShader);
        // 向程序中加入片元着色器
        GLES20.glAttachShader(program, pixelShader);
        // 链接程序
        GLES20.glLinkProgram(program);
        // 存放链接成功program数量的数组
        val linkStatus = IntArray(1)
        // 获取program的链接情况
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        // 若链接失败则报错并删除程序
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e("ES20_ERROR", "Could not link program: ");
            Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            return 0
        }
    }
    return program;
}

fun createProgram(vPath:String, fPath:String):Int {
    //加载顶点着色器
    val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vPath);
    if (vertexShader == 0) {
        loge("vertex shader error")
        return 0
    }

    // 加载片元着色器
    val pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fPath)
    if (pixelShader == 0) {
        loge("fragment shader error")
        return 0
    }

    return createProgramInternal(vertexShader, pixelShader)
}

fun createProgram(vertexId:Int, fragmentId:Int):Int {
    //加载顶点着色器
    val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexId);
    if (vertexShader == 0) {
        loge("vertex shader error")
        return 0
    }

    // 加载片元着色器
    val pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentId)
    if (pixelShader == 0) {
        loge("fragment shader error")
        return 0
    }

    return createProgramInternal(vertexShader, pixelShader)
}