package com.dannir.reto1.model

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dannir.reto1.databinding.PostCardBinding
import com.dannir.reto1.model.Reto1App.Companion.prefs
import java.time.Month
import java.time.format.TextStyle
import java.util.*

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var post: Post? = null

    private val binding = PostCardBinding.bind(itemView)

    @SuppressLint("NewApi")
    fun bindPost(post: Post){
        this.post = post

        //date parser
        val day = post.date.get(Calendar.DAY_OF_MONTH).toString()
        val month = Month.of(post.date.get(Calendar.MONTH)+1)
        val monthParse = month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val year = post.date.get(Calendar.YEAR).toString()

        val hour = post.date.get(Calendar.HOUR_OF_DAY).toString()
        val minutes = post.date.get(Calendar.MINUTE).toString()

        val dateParse = "$day de $monthParse del $year - $hour:$minutes"

        val bitmapPost = BitmapFactory.decodeFile(post.fileString)

        //set post values
        binding.postDescriptionTv.text = post.description
        binding.postImg.setImageBitmap(bitmapPost)
        binding.postCityTv.text=post.city
        binding.postDateTv.text = dateParse

        //set post user values
        val user = prefs.getUserById(post.uid)

        binding.postUsernameTv.text = user.name

        if (user.profileImg.isNotEmpty()){
            val bitmapUser = BitmapFactory.decodeFile(user.profileImg)
            binding.postProfileImg.setImageBitmap(bitmapUser)
        }

    }
}