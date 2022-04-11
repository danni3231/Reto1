package com.dannir.reto1.model.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannir.reto1.model.Post
import com.dannir.reto1.model.Reto1App.Companion.prefs
import com.dannir.reto1.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PublishViewModel : ViewModel() {

    fun uploadPost(post: Post) {
        prefs.savePost(post)
    }

    fun getUser(): User {
        return prefs.getCurrentUser()!!
    }
}