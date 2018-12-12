package me.leoyuu.rv

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * date 2018/5/16
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class SimpleTextList @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs){
    init {
        adapter = SimpleTextRvAdapter().apply { init(100) }
        layoutManager = LinearLayoutManager(context)
    }
}
