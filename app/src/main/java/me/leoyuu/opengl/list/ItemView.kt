package me.leoyuu.opengl.list

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import me.leoyuu.rv.BaseRvItemView

/**
 * date 2018/12/5
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class ItemView(context: Context) : BaseRvItemView<RenderItem>(context) {
    private val tv = TextView(context)
    init {
        addView(tv)
        tv.setTextColor(Color.BLACK)
        tv.setBackgroundColor(Color.GREEN)
        tv.setPadding(48, 15, 48, 15)
        setPadding(0, 0, 0, 3)
        setBackgroundColor(Color.LTGRAY)
    }

    override fun onBindData(data: RenderItem) {
        tv.text = data.desc
    }
}
