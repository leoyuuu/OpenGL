package me.leoyuu.opengl.ctrl.jump.nodes

/**
 * date 2018/12/6
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
internal interface Node {
    fun initProgram()
    fun draw(matrix:FloatArray)
    fun onSizeChange(width:Int, height:Int)
}
