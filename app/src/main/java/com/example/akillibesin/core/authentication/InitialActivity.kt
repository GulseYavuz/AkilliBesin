package com.example.akillibesin.core.authentication

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.akillibesin.core.database.authentication.SharedPreferences
import com.example.akillibesin.frontend.authentication.activities.AuthenticationActivity
import com.example.akillibesin.frontend.authentication.activities.SplashActivity
import com.example.akillibesin.frontend.home.activities.MainActivity

class InitialActivity : AppCompatActivity(){

    fun authLoginAction() {
        val intent = Intent(SplashActivity.instance, AuthenticationActivity::class.java)
        intent.putExtra("AuthOption", "login")
        SplashActivity.instance.startActivity(intent)
    }

    fun authRegisterAction() {
        val intent = Intent(SplashActivity.instance, AuthenticationActivity::class.java)
        intent.putExtra("AuthOption", "register")
        SplashActivity.instance.startActivity(intent)
    }


    fun authenticateUser(): Boolean {
        if (hasAccount()) {
            openMainActivity()
            return true
        }
        return false
    }


    private fun hasAccount(): Boolean {
        val preference = SharedPreferences(SplashActivity.instance)
        return preference.isSPHasValue()
    }
    
    private fun openMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(SplashActivity.instance, MainActivity::class.java)
            SplashActivity.instance.startActivity(intent)
            SplashActivity.instance.finish()
        }, 2000)
    }
}