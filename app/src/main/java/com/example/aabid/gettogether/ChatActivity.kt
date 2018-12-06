package com.example.aabid.gettogether

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.chatsdk.firebase.file_storage.FirebaseFileStorageModule
import co.chatsdk.core.error.ChatSDKException
import co.chatsdk.firebase.FirebaseNetworkAdapter
import co.chatsdk.ui.manager.BaseInterfaceAdapter
import co.chatsdk.core.session.ChatSDK
import co.chatsdk.core.session.Configuration
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider


class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val context = applicationContext
        // Create a new configuration
        val builder = Configuration.Builder(context)

        // Perform any configuration steps (optional)
        builder.firebaseRootPath("prod")

        // Initialize the Chat SDK
        try {
            ChatSDK.initialize(builder.build(), BaseInterfaceAdapter(context), FirebaseNetworkAdapter())
        } catch (e: ChatSDKException) {
        }


        // File storage is needed for profile image upload and image messages
        FirebaseFileStorageModule.activate()
        FirebasePushModule.activate()

        FirebaseUIModule.activate(context, GoogleAuthProvider.PROVIDER_ID, PhoneAuthProvider.PROVIDER_ID);

    }
}
