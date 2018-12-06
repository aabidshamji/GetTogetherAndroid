package com.example.aabid.gittogether.touch

interface TouchHelperAdapter {
    fun onDismissed(position: Int)
    fun onCityMoved(fromPosition: Int, toPosition: Int)
}