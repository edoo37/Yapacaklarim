package com.yasinsenel.yapacaklarm.diffUtilCallBack

import androidx.recyclerview.widget.DiffUtil
import com.yasinsenel.yapacaklarm.model.TodoData

class DiffUtilCallBack(private val oldList : List<TodoData>, private val newList : List<TodoData>)
    : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.javaClass == newItem.javaClass
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.hashCode() == newItem.hashCode()
    }

}