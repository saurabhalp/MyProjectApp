package com.saurabhalp.myprojectapp.ui
import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.saurabhalp.myprojectapp.R
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.saurabhalp.myprojectapp.MainActivity

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Update Notifications"
        val descriptionText = "Notifications for new updates"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("update_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}



@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun checkAndRequestNotificationPermission(
    context: Context,
    requestPermissionLauncher: ActivityResultLauncher<String>
): Boolean {
    return if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        true
    } else {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        false
    }
}

@SuppressLint("MissingPermission")
fun showNotification(context: Context, title: String, message: String) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent,
        PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, "update_channel")
        .setSmallIcon(R.drawable.notification)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        notify(0, builder.build())
    }
}
