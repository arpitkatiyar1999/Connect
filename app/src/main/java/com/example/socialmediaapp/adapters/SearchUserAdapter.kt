package com.example.socialmediaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.ItemSearchProfileBinding
import com.example.socialmediaapp.models.UserModel
import com.squareup.picasso.Picasso

class SearchUserAdapter(private val userList:ArrayList<UserModel>):RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSearchProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user=userList[position]
        with(holder.binding)
        {
            userNameTxt.text=user.name
            userProfessionTxt.text=user.profession
            if(user.profilePhoto.isNotBlank())
                Picasso.get().load(user.profilePhoto).placeholder(R.drawable.img_placeholder).into(userProfileImg)
            else
                userProfileImg.setImageResource(R.drawable.img_placeholder)
        }
    }

    override fun getItemCount()=userList.size
    class ViewHolder(val binding:ItemSearchProfileBinding):RecyclerView.ViewHolder(binding.root)

}