package cn.gc.module2

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * 使用Databinding的通用adapter
 * Created by 宫成 on 2019/4/17 下午10:39.
 */
class BaseBindingRecyclerAdapter<T, DB : ViewDataBinding>(
    private val dataList: MutableList<T>,
    private val itemLayoutId: Int,
    private val action: (T, DB, position: Int) -> Unit = { _, _, _ -> }
) : BaseRecyclerAdapter<BaseBindingRecyclerViewHolder<T, DB>>() {

    fun updateData(mutableList: MutableList<T>) = with(dataList) {
        clear()
        addAll(mutableList)
    }

    //    override fun _getItemCount(): Int = dataList.size
    override fun _getItemCount(): Int = Int.MAX_VALUE

    override fun _onCreateViewHolder(parent: ViewGroup?, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val itemBinding: DB = DataBindingUtil.inflate(LayoutInflater.from(parent?.context), itemLayoutId, parent, false)
        return BaseBindingRecyclerViewHolder<T, DB>(itemBinding)
    }

    override fun _onBindViewHolder(holder: BaseBindingRecyclerViewHolder<T, DB>?, realPosition: Int) {
        holder?.bind(action, dataList[realPosition % dataList.size], realPosition)
    }
}

class BaseBindingRecyclerViewHolder<T, DB : ViewDataBinding>(
    private val itemDataBinding: DB
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemDataBinding.root) {
    fun bind(action: (T, DB, position: Int) -> Unit, entity: T, position: Int) {
        action(entity, itemDataBinding, position)
        itemDataBinding.executePendingBindings()
    }
}