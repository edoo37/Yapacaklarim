package com.yasinsenel.yapacaklarm.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.AdsLayoutBinding
import com.yasinsenel.yapacaklarm.databinding.ItemsLayoutBinding
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.utils.diffUtilCallBack.DiffUtilCallBack


class TodoAdapter(private val removeitem: removeItem) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList : MutableList<TodoData> = mutableListOf()
    var isAd = false
    var newPos = 0



    companion object{
        const val AD_TYPE = 2
        const val CONTENT_TYPE = 1

    }


    interface removeItem{
        fun deleteItem(data: Int,dataSize : Int)


    }


    inner class Holder(val binding : ItemsLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : TodoData,position: Int){
            binding.apply {
                tvTaskName.text = data.todoName
                tvDate.text = data.todoDate
                tvTime.text = data.todoTime

                text.setOnClickListener {

                    removeitem.deleteItem(adapterPosition,itemCount)
                    if(itemList.size%4==0){
                        val list : MutableList<TodoData> = mutableListOf()
                        list.add(data)
                        val deneme = itemList.findLast { it.isAd == true }
                        list.add(deneme!!)
                        itemList.removeAll(list)
                        list.clear()
                    }
                    else{
                        itemList.remove(data)
                    }

                    //Navigation.findNavController(it).navigate(R.id.action_mainFragment_self)

                }

                itemsLayout.setOnClickListener {
                    val getData = data
                    val bundle = Bundle()
                    bundle.putParcelable("dataClass",getData)
                    Navigation.findNavController(it).navigate(R.id.action_mainFragment_to_updateItemFragment,bundle)

                }
            }

        }
    }
    inner class Holder1(val binding : AdsLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(){
            binding.apply {
                val adRequest = AdRequest.Builder().build()
                adView.loadAd(adRequest)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            AD_TYPE->
            {
                val binding = AdsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                Holder1(binding)
            }
            CONTENT_TYPE->
            {
                val binding = ItemsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                Holder(binding)
            }
            else->{
                val binding = ItemsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                Holder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        val positionNew = position +1
        return if (itemList.get(position).isAd!!) {
            AD_TYPE
        } else {
            CONTENT_TYPE
        }
    }

    fun setData(data: MutableList<TodoData>) {
        itemList = data.toMutableList()
    }

    fun setNewList(myNewList : MutableList<TodoData>){
        val diffUtilCallBack = DiffUtilCallBack(itemList,myNewList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)
        itemList.clear()
        itemList.addAll(myNewList)
        notifyDataSetChanged()
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val getViewType = getItemViewType(position)
        if(getViewType == AD_TYPE){
            (holder as Holder1).bind()
        }
        if(getViewType == CONTENT_TYPE){
            (holder as Holder).bind(itemList.get(position),position)
        }

    }


}