package com.example.akillibesin.core.authentication

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.example.akillibesin.R
import com.example.akillibesin.backend.authentication.AuthenticationInterface
import com.example.akillibesin.core.models.User
import com.example.akillibesin.frontend.authentication.activities.AuthenticationActivity
import com.example.akillibesin.frontend.authentication.fragments.LoginFragment
import com.example.akillibesin.frontend.factory.AuthTitleViewModel


class Authenticator : AppCompatActivity(), AuthenticationInterface {

    private val authCompatActivity = AuthenticationActivity.instance as AppCompatActivity
    companion object {
        val user = User.getInstance() // singleton object
        val titleViewModel = ViewModelProviders
            .of(AuthenticationActivity.instance as FragmentActivity)
            .get(AuthTitleViewModel::class.java)
    }

    fun authenticate() {
        val option = AuthenticationActivity.instance.intent
            .getStringExtra("AuthOption").toString()
        User.reset(user)

        when (option) {
            "login" -> loadFragment(LoginFragment.newInstance())
            else -> Toast.makeText(AuthenticationActivity.instance,
                authCompatActivity.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
        }

        // observe AuthTitle Changes
        titleViewModel.mutableAuthTitleLD.observe(authCompatActivity) { fragmentTitle ->
            updateAuthToolbarTitle(fragmentTitle)
        }
    }
    private fun loadFragment(fragment: Fragment) {
        authCompatActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.auth_frame, fragment)
            .commit()
    }

}