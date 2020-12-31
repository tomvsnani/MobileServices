package com.example.mobileservices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileservices.databinding.ImagerowLayoutBinding

class ImageAdapter :
    androidx.recyclerview.widget.ListAdapter<String, ImageAdapter.ImageViewHolder>(ModelClass.diff) {
    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var binding: ImagerowLayoutBinding

        init {
            binding = ImagerowLayoutBinding.bind(view)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.imagerow_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.binding.mobileimage1.context)
            .load(currentList[position]).into(holder.binding.mobileimage1)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun submitList(list: List<String>?) {

        super.submitList(list?.toList())
    }

}