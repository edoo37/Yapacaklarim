package com.yasinsenel.yapacaklarm.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.databinding.AdsLayoutBinding
import com.yasinsenel.yapacaklarm.databinding.ItemsLayoutBinding
import com.yasinsenel.yapacaklarm.diffUtilCallBack.DiffUtilCallBack
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.viewmodel.MainFragmentViewModel


class TodoAdapter(private val mainFragmentViewModel: MainFragmentViewModel,private val removeitem: removeItem) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var binding : ItemsLayoutBinding
    private var itemList : MutableList<TodoData> = mutableListOf()
    private var adList : MutableList<Any> = mutableListOf()
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
        fun bind(data : TodoData){
            binding.apply {
                tvTaskName.text = data.todoName
                tvDate.text = data.todoDate
                tvTime.text = data.todoTime

                text.setOnClickListener {
                    if(adapterPosition == 0 || adapterPosition == 1 || adapterPosition == 2){
                        removeitem.deleteItem(adapterPosition,itemCount)
                    }
                    else{
                        removeitem.deleteItem(newPos,itemCount)
                    }

                    itemList.remove(data)
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
        if(itemList.size>2){
            val myListsize : Double= itemList.size.toDouble()
            val deneme = Math.ceil(myListsize / 4).toInt() //will be 2
            return itemList.size + deneme
        }
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        val positionNew = position +1 
        return if (positionNew % 4 == 0 && position > 0) {
            isAd = true
            AD_TYPE
        } else {
            isAd = false
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
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val getViewType = getItemViewType(position)
        if(getViewType == AD_TYPE){
            (holder as Holder1).bind()
        }
        if(getViewType == CONTENT_TYPE){
            newPos = position - position/4
            (holder as Holder).bind(itemList[newPos])
        }

    }


}