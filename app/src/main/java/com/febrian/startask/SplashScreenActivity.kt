package com.febrian.startask

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.febrian.startask.utils.Constant

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val sharedPreference = getSharedPreferences(Constant.SharedPreferences, Context.MODE_PRIVATE)
        Handler(Looper.getMainLooper()).postDelayed({
            val key = sharedPreference.getString(Constant.ROLE, "")
            if (key == Constant.FATHER || key == Constant.MOTHER) {
                targetIntent(ParentHomeActivity())
            } else if (key == Constant.SON || key == Constant.DAUGHTER) {
                targetIntent(ChildHomeActivity())
            } else {
                targetIntent(MainActivity())
            }

        }, DELAY)


    }

    private fun targetIntent(activity: Activity) {
        val intent = Intent(applicationContext, activity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val DELAY = 800L
    }
}