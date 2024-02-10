package com.ava.alivakili.drink_water.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ava.alivakili.drink_water.WaterAmountModel
import com.ava.alivakili.drink_water.databinding.RecyclerViewItemBinding

class HistoryRecyclerViewAdapter (
        ) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder>(){

    private val diffCallback=object : DiffUtil.ItemCallback<WaterAmountModel>(){
        override fun areItemsTheSame(oldItem: WaterAmountModel, newItem: WaterAmountModel): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: WaterAmountModel, newItem: WaterAmountModel): Boolean {
            return oldItem==newItem
        }


    }

    val differ= AsyncListDiffer(this,diffCallback)

    class HistoryViewHolder(
        private val binding: RecyclerViewItemBinding,
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(amount: WaterAmountModel){
            binding.waterScaleTV.text=amount.amount.toString()+" ml"
            binding.timeTV.text=amount.time
            if ((amount.percentage.toDouble() * 100).toString().length >= 4)
                binding.percentageTV.text = (amount.percentage.toDouble() * 100).toString().substring(0, 4) + " %"
            else
                binding.percentageTV.text = (amount.percentage.toDouble() * 100).toString() + " %"
        }


        companion object{
            fun create(parent: ViewGroup): HistoryViewHolder {
                val binding= RecyclerViewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
                return HistoryViewHolder(
                    binding = binding,
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}