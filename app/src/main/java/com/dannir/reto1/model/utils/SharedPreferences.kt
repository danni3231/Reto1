package com.dannir.reto1.model.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.dannir.reto1.model.Post
import com.dannir.reto1.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


class SharedPreferences(context: Context) {
    private var _dataBase: SharedPreferences? = context.getSharedPreferences("DB", Context.MODE_PRIVATE)
    private val db get() = _dataBase!!
    private val gson = Gson()
    private val currentUserRef = "CURRENT_USER"
    private val postRef = "POSTS"
    private var currentUser: String? = null

    //current user db manage

    fun saveCurrentUser(userCode:String){
        db.edit().putString(currentUserRef, userCode).apply()
    }

    fun getCurrentUser(): User? {

        currentUser = db.getString(currentUserRef,"").toString()

        if(currentUser != null){
            val userString = db.getString(currentUser,"")
            return gson.fromJson(userString, User::class.java)
        }

        else return null
    }

    fun currentUserLogOut(){
        db.edit().remove(currentUserRef).apply()
    }

    //users db manage

    fun createUser(){
        if(db.getString("USER_1","").toString().isEmpty()||db.getString("USER_2","").toString().isEmpty()){
            val user = User(UUID.randomUUID().toString(),"Daniel Rojas","")
            val user2 = User(UUID.randomUUID().toString(),"John Doe","")
            val userString = gson.toJson(user)
            val userString2 = gson.toJson(user2)
            db.edit().putString("USER_1", userString).apply()
            db.edit().putString("USER_2", userString2).apply()
        }
        else return

    }

    fun updateUser(user: User){
        val userString = gson.toJson(user)
        db.edit().putString(currentUser,userString).apply()
    }

    fun getUserById(uuid: String):User{

        val user1 = gson.fromJson(db.getString("USER_1",""),User::class.java)
        val user2 = gson.fromJson(db.getString("USER_2",""),User::class.java)

        if(user1.id.contentEquals(uuid)) return user1

        return user2
    }

    //posts db manage

    fun savePost(post: Post){
        val posts = getPost()
        posts.add(post)
        db.edit().putString(postRef,gson.toJson(posts)).apply()
    }

    fun getPost():ArrayList<Post>{

        var postList = ArrayList<Post>()
        val posts = db.getString(postRef,"")

        Log.e(">>>", posts.toString())

        if(posts!!.isEmpty()) return postList

        val arrayListPostType: Type = object : TypeToken<ArrayList<Post>>() {}.type
        Log.e(">>>", arrayListPostType.toString())
        postList = gson.fromJson(posts, arrayListPostType)

        Log.e(">>>", postList.toString())


        return postList

    }



}