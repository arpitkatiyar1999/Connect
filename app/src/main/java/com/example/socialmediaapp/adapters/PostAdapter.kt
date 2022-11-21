package com.example.socialmediaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.databinding.ItemPostBinding
import com.example.socialmediaapp.models.PostModel

class PostAdapter(private val postItem:ArrayList<PostModel>,private val context: Context):RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post=postItem[position]
        with(holder.binding)
        {
            userProfileImg.setImageResource(post.profilePic)
            userPostImg.setImageResource(post.postedItem)
            likeTxt.text=post.like
            commentTxt.text=post.comment
            shareTxt.text=post.share
           userAboutTxt.text=post.about
            nameTxt.text=post.name
        }
    }

    override fun getItemCount(): Int =postItem.size
    class ViewHolder(val binding: ItemPostBinding):RecyclerView.ViewHolder(binding.root)

}