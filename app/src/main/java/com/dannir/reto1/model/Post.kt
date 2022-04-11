package com.dannir.reto1.model

import java.util.*

data class Post(
    val id: String,
    val uid: String,
    val fileString: String,
    val description: String,
    val city: String,
    val date: Calendar,
)
