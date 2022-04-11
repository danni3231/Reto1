package com.dannir.reto1.model.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dannir.reto1.model.Reto1App.Companion.prefs
import com.dannir.reto1.model.User

class ProfileViewModel: ViewModel() {

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() {
            return _user
        }

    init {
        getUser()
    }

    private fun getUser() {
        _user.postValue(prefs.getCurrentUser())
    }

    fun updateUser(currentUser: User) {
        prefs.updateUser(currentUser)
    }

    fun logOut(){
        prefs.currentUserLogOut()
    }
}