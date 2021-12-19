package com.febrian.startask.utils

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.febrian.startask.MainActivity
import com.febrian.startask.R
import com.febrian.startask.ui.child.ChildHomeActivity
import com.febrian.startask.ui.parent.ParentHomeActivity
import com.febrian.startask.utils.Constant.CHANNEL_ID
import com.febrian.startask.utils.Constant.CHANNEL_NAME
import com.febrian.startask.utils.Constant.NOTIFICATION_ID
import java.util.*

class SendNotification : BroadcastReceiver() {

    var title : String? = null
    var desc : String? = null
    var activity : AppCompatActivity = ParentHomeActivity()

    override fun onReceive(c: Context, p1: Intent) {

        val preferences = c
            .getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        val role: String? = preferences.getString(Constant.ROLE, null)
        if(role == Constant.FATHER || role == Constant.MOTHER){
            title = "Star Task"
            desc = "Don't forget to give tasks and rewards to children"
            activity = ParentHomeActivity()
        }else{
            title = "Star Task"
            desc = "Don't forget to do the task to get the reward"
            activity = ChildHomeActivity()
        }

        val intent = Intent(c, activity::class.java)
        val pendingIntent = PendingIntent.getActivity(c, 0, intent, 0)

        val mNotificationManager =
            c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mBuilder = c.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(desc)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            channel.description = CHANNEL_NAME
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()
        mNotificationManager.notify(NOTIFICATION_ID, notification)

    }

    fun setRepeat(context: Context) {

        val alarm: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SendNotification::class.java)

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 9
        calendar[Calendar.MINUTE] = 1
        calendar[Calendar.SECOND] = 0

        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)

        alarm.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}