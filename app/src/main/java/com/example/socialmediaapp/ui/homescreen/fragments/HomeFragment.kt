package com.example.socialmediaapp.ui.homescreen.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapters.PostAdapter
import com.example.socialmediaapp.adapters.StoryAdapter
import com.example.socialmediaapp.databinding.FragmentHomeBinding
import com.example.socialmediaapp.models.PostModel
import com.example.socialmediaapp.models.StoryModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var storyItemList:ArrayList<StoryModel>
    private lateinit var postItemList:ArrayList<PostModel>
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var postAdapter: PostAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        initAll()
        return binding.root
    }

    private fun initAll() {
        storyItemList= ArrayList()
        postItemList= ArrayList()
        storyItemList.add(StoryModel(R.drawable.default_story_pic,R.drawable.ic_live,R.drawable.default_profile_pic,"Arpit Katiyar"))
        storyItemList.add(StoryModel(R.drawable.default_profile_pic,R.drawable.ic_live,R.drawable.default_story_pic,"Ayush Katiyar"))
        postItemList.add(PostModel(R.drawable.default_profile_pic,R.drawable.default_story_pic,0,"Arpit Katiyar","Learner","500",
        "966","50")
        )
        postItemList.add(PostModel(R.drawable.default_story_pic,R.drawable.default_profile_pic,0,"Ayush Katiyar","Friend","500",
            "966","50"))
        storyAdapter= StoryAdapter(storyItemList,requireContext())
        postAdapter=PostAdapter(postItemList,requireContext())
        with(binding)
        {
            storyRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            storyRv.adapter=storyAdapter
            postRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            postRv.adapter=postAdapter
        }
    }

}