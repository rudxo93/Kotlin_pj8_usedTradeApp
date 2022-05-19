package com.duran.usedtradeapp.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duran.usedtradeapp.databinding.ItemActicleBinding
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter : ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemActicleBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(articleModel: ArticleModel) {

            // Long형식에서 날짜로 바꾸기
            var format = SimpleDateFormat("MM월 dd일")
            val date = Date(articleModel.createdAt)

            binding.titleTv.text = articleModel.title
            binding.dateTv.text = format.format(date).toString()
            binding.priceTv.text = articleModel.price

            // Glide로 이미지 불러오기
            if(articleModel.imageUrl.isNotEmpty()) {
                Glide.with(binding.thumbnailIv)
                    .load(articleModel.imageUrl)
                    .into(binding.thumbnailIv)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemActicleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>(){
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                // 현재 노툴하고 있는 아이템과 새로운 아이템이 같은지 비교한다
                return oldItem.createdAt == newItem.createdAt
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                // equals와 비교
                return oldItem == newItem
            }
        }
    }
}