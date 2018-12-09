package com.example.aabid.gettogether.touch

interface TouchHelperAdapter {
    fun onDismissed(position: Int)
    fun onCityMoved(fromPosition: Int, toPosition: Int)
}