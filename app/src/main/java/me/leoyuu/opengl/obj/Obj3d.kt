package me.leoyuu.opengl.obj

import android.content.res.AssetManager
import java.lang.Exception
import java.util.*
import kotlin.math.sqrt

/**
 * date 2018/12/11
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class Obj3d(val vertices:FloatArray, val colors:FloatArray, val indices:ShortArray){
    var minX = 0f
        private set
    var minY = 0f
        private set
    var minZ = 0f
        private set
    var maxX = 0f
        private set
    var maxY = 0f
        private set
    var maxZ = 0f
        private set

    init {
        updateBoxInfo()
    }

    private fun updateBoxInfo() {
        vertices.forEachIndexed { index, fl ->
            val mode = index % 3
            when (mode) {
                0 -> {
                    if (fl > maxX) {
                        maxX = fl
                    } else if(fl < minX) {
                        minX = fl
                    }
                }
                1 -> {
                    if (fl > maxY) {
                        maxY = fl
                    } else if(fl < minY) {
                        minY = fl
                    }
                }
                else -> {
                    if (fl > maxZ) {
                        maxZ = fl
                    } else if(fl < minZ) {
                        minZ = fl
                    }
                }
            }
        }
    }

    val cx :Float
    get() = (maxX + minX) / 2


    val cy :Float
        get() = (maxY + minY) / 2

    val cz :Float
        get() = (maxY + minY) / 2

    val size:Float
        get() {
            val dx = maxX - minX
            val dy = maxY - minY
            val dz = maxZ - minZ
            return sqrt(dx * dx + dy * dy + dz * dz)
        }

    companion object {

        fun parseFromFile(assetManager: AssetManager, assetPath:String):Obj3d {
            val i = assetManager.open(assetPath)
            val vertices = mutableListOf<Float>()
            val colors = mutableListOf<Float>()
            val indexed = mutableListOf<Short>()
            try {
                val reader = i.bufferedReader()
                var line = reader.readLine()
                while (line != null) {
                    val s = line.trim()
                    if (s.isNotEmpty()) {
                        val c = s[0]
                        if (c == 'v' && s[1] == ' ') {
                            s.split(" ").forEachIndexed { index, v ->
                                if (index != 0 && v.isNotBlank()) {
                                    vertices.add(v.toFloat())
                                    colors.add(Math.random().toFloat())
                                }
                            }
                        } else if (c == 'f') {
                            s.split(" ").forEachIndexed { index, f ->
                                if (index != 0 && f.isNotBlank()) {
                                    val j = f.split("/")[0].toShort() - 1
                                    indexed.add(j.toShort())
                                }
                            }
                        }
                    }
                    line = reader.readLine()
                }
            } catch (e:Exception) {
                i.close()
                e.printStackTrace()
            }

            return Obj3d(vertices.toFloatArray(), colors.toFloatArray(), indexed.toShortArray())
        }
    }

    override fun toString(): String {
        return "Obj3d(vertices=${Arrays.toString(vertices)}, indices=${Arrays.toString(indices)})"
    }
}
