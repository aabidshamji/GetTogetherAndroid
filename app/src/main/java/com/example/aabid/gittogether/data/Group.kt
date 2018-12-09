package com.example.aabid.gittogether.data

data class Group(var uid: String = "",
                 var name: String = "",
                 var founder: String,
                 var latitude: Int = 0,
                 var longitude: Int = 0)