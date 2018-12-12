package me.leoyuu.rv

import android.content.Context
import android.widget.FrameLayout


/**
 * date 2018/5/12
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
abstract class BaseRvItemView<in T>(context: Context): FrameLayout(context){
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }
    abstract fun onBindData(data:T)
}