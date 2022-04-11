package com.dannir.reto1.model.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannir.reto1.model.Post
import com.dannir.reto1.model.Reto1App.Companion.prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private var _postList: MutableLiveData<ArrayList<Post>> = MutableLiveData()
    val postList: LiveData<ArrayList<Post>>
        get() {
            return _postList
        }

    init {
        getPostList()
    }

    fun getPostList(){
        val posts = prefs.getPost()
        _postList.value = posts
    }
}