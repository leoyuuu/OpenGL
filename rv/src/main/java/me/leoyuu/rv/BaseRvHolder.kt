package me.leoyuu.rv

import androidx.recyclerview.widget.RecyclerView


/**
 * date 2018/5/13
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class BaseRvHolder<T> internal constructor(val view: BaseRvItemView<T>, val onItemClickListener: BaseOnItemClickListener<T>) : RecyclerView.ViewHolder(view) {
    var data:T? = null

    init {
        itemView.setOnClickListener {
            val d = data
            if (d != null) {
                onItemClickListener.onItemClick(adapterPosition, d)
            }
        }
    }

    fun onBindData(data:T) {
        this.data = data
        view.onBindData(data)
    }
}