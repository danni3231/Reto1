package com.dannir.reto1.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dannir.reto1.R

class PostAdapter : RecyclerView.Adapter<PostViewHolder>() {

    private val posts = ArrayList<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //XML -> View
        val view = inflater.inflate(R.layout.post_card, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindPost(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun addPost(post: Post) {
        posts.add(post)
        notifyItemInserted(posts.size-1)
    }

    fun clear() {
        val size = posts.size
        posts.clear()
        notifyItemRangeRemoved(0, size)
    }
}