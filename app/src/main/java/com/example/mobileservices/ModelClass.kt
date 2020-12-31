package com.example.mobileservices

import androidx.recyclerview.widget.DiffUtil

data class ModelClass(
    var id: Int = -1,
    var userid: Int=-1,
    var model: String = "",
    var brand: String = "",
    var problem: String = "",
    var name: String = "",
    var imageUri: List<String> = ArrayList()
) {
    companion object {
        var diff = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}