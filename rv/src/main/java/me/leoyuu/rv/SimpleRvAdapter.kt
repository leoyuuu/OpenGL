package me.leoyuu.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList


/**
 * date 2018/5/9
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
abstract class SimpleRvAdapter<in T>(@param:LayoutRes @field:LayoutRes
                       private val layoutId: Int) : RecyclerView.Adapter<SimpleRvAdapter.Holder>() {

    private val dataList = ArrayList<T>()

    fun refresh(list: List<T>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        convert(holder, dataList[position])
    }

    override fun getItemViewType(position: Int) = layoutId

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val context = parent.context
        return Holder(LayoutInflater.from(context).inflate(viewType, parent, false))
    }

    abstract fun convert(holder:Holder, data:T)

    class Holder constructor(view: View) : RecyclerView.ViewHolder(view)
}
