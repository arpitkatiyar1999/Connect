package com.example.socialmediaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.databinding.ItemStoryBinding
import com.example.socialmediaapp.models.StoryModel

class StoryAdapter(private val storyList:ArrayList<StoryModel>,private  val context: Context):RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story=storyList[position]
        with(holder.binding)
        {
            userNameTxt.text=story.name
            userStoryImg.setImageResource(story.story)
            userProfileImg.setImageResource(story.profilePic)
            storyTypeImg.setImageResource(story.storyType)
        }
    }

    override fun getItemCount(): Int =storyList.size
    class ViewHolder(val binding: ItemStoryBinding):RecyclerView.ViewHolder(binding.root)
}