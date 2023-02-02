package com.yasinsenel.yapacaklarm.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.findColumnIndexBySuffix
import com.orhanobut.hawk.Hawk
import com.yasinsenel.yapacaklarm.MainActivity
import com.yasinsenel.yapacaklarm.R
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
            Hawk.put("myData2",itemList)
            notifyDataSetChanged()
            val intent = Intent(context,MainActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
            Toast.makeText(context,R.string.txt_delete_message,Toast.LENGTH_SHORT).show()
        }

        binding.itemsLayout.setOnClickListener {
            val getPosition = itemList[position]
            val bundle = Bundle()
            bundle.putParcelable("dataClass",getPosition)
            Navigation.findNavController(it).navigate(R.id.action_mainFragment_to_addTaskFragment,bundle)

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