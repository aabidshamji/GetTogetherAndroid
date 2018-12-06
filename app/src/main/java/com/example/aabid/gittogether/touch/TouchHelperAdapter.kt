package com.example.aabid.gittogether.touch

interface TouchHelperAdapter {
    fun onDismissed(position: Int)
    fun onUserMoved(fromPosition: Int, toPosition: Int)
}