package com.example.aabid.gittogether.data

data class User(var uid: String = "",
                var name: String = "",
                var latitude: Double = 0.0,
                var longitude: Double = 0.0,
                var groups: MutableList<String> = mutableListOf()
)