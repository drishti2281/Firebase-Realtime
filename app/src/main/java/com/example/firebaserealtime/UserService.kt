package com.example.firebaserealtime

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class UserService : FirebaseMessagingService() {
    private val TAG = "FirebaseService"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        var notificationData = message.notification
        print("notification title ${notificationData?.title}")
        print("notification body ${notificationData?.body}")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG ,"refreshed token :$token")
    }


}