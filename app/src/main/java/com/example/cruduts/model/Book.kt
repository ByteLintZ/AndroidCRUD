package com.example.cruduts.model

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val genre: String,
    val type: String, // Added to match the database structure
    val price: Double
)
