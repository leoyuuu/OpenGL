package me.leoyuu.rv

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList


/**
 * date 2018/5/9
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class BaseRvAdapter<T, V : BaseRvItemView<T>>(
                       private val c: Class<V>) : RecyclerView.Adapter<BaseRvHolder<T>>() {

    private val dataList = ArrayList<T>()
    var onItemClickListener:BaseOnItemClickListener<T>? = null
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var checked:Boolean = false
    private var selectedItem = -1

    fun refresh(list: List<T>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun addTail(list:List<T>) {
        val size = dataList.size
        dataList.addAll(list)
        notifyItemRangeInserted(size, list.size)
    }

    override fun onBindViewHolder(holder: BaseRvHolder<T>, position: Int) {
        holder.onBindData(dataList[position])
    }

    override fun getItemViewType(position: Int) = c.hashCode()

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvHolder<T> {
        val context = parent.context
        val v = c.getConstructor(Context::class.java).newInstance(context)
        return BaseRvHolder(v, localItemClickListener)
    }

    private val localItemClickListener = object: BaseOnItemClickListener<T> {
        override fun onItemClick(index: Int, data: T) {
            onItemClickListener?.onItemClick(index, data)
            if (data is RadioItem) {
                if (checked) {
                    data.setSelected(!data.isSelected())
                } else {
                    if (!data.isSelected()) {
                        data.setSelected(true)
                        if (selectedItem != -1) {
                            val selected = dataList[selectedItem]
                            if (selected is RadioItem) {
                                selected.setSelected(false)
                            }
                            selectedItem = index
                        }
                    }
                }
            }
        }
    }
}
