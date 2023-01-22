package com.yasinsenel.yapacaklarm.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.MainActivity
import com.yasinsenel.yapacaklarm.databinding.ItemsLayoutBinding
import com.yasinsenel.yapacaklarm.diffUtilCallBack.DiffUtilCallBack
import com.yasinsenel.yapacaklarm.model.TodoData

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.Holder>() {
    private lateinit var binding : ItemsLayoutBinding
    private var itemList : ArrayList<TodoData> = arrayListOf()

    inner class Holder(val binding : ItemsLayoutBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.tvTaskName.text = itemList[position].todoName.toString()
        holder.binding.tvDate.text = itemList[position].todoDate.toString()
        holder.binding.tvTime.text = itemList[position].todoTime.toString()

        binding.text.setOnClickListener {
            val getPosition = itemList[position]
            val context = holder.itemView.context
            itemList.remove(getPosition)
            Hawk.put("data",itemList)
            notifyDataSetChanged()
            val intent = Intent(context,MainActivity::class.java)
            context.startActivity(intent)
            Toast.makeText(context,"Item Silindi",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setData(data: ArrayList<TodoData>) {
        this.itemList = data
    }

    fun setNewList(myNewList : ArrayList<TodoData>){
        val diffUtilCallBack = DiffUtilCallBack(itemList,myNewList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)
        itemList.clear()
        itemList.addAll(myNewList)
        diffResult.dispatchUpdatesTo(this)

    }

}