package com.yasinsenel.yapacaklarm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yasinsenel.yapacaklarm.databinding.ItemsLayoutBinding
import com.yasinsenel.yapacaklarm.model.TodoData

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.Holder>() {
    private lateinit var binding : ItemsLayoutBinding
    private val itemList : ArrayList<TodoData> = arrayListOf()

    inner class Holder(val binding : ItemsLayoutBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.tvDate.text = itemList[position].todoName
        holder.binding.tvDate.text = itemList[position].todoDate.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun fetchList(myList : ArrayList<TodoData>){
        itemList.addAll(myList)
        notifyDataSetChanged()
    }
}