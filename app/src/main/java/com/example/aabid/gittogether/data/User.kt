package com.example.aabid.gittogether.data

data class User(var uid: String = "",
                var name: String = "",
                var latitude: Int = 0,
                var longitude: Int = 0,
                var groups: MutableList<String> = mutableListOf()
)