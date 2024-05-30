package com.example.akillibesin.backend.authentication

import com.example.akillibesin.core.authentication.Authenticator
import com.example.akillibesin.core.authentication.InitialActivity
import com.example.akillibesin.frontend.authentication.activities.AuthenticationActivity
import com.example.akillibesin.frontend.authentication.activities.SplashActivity

interface AuthenticationInterface {

    private val initialActivity: InitialActivity get() = InitialActivity()

    fun authLogin() {
        initialActivity.authLoginAction()
    }

    fun authRegister() {
        initialActivity.authRegisterAction()
    }

    fun authenticateUser() {
        val isValidAuth = initialActivity.authenticateUser()
        if (isValidAuth)
            buttonsVisibilityGone()
    }

    fun authOptions() {
        val authenticator = Authenticator()
        authenticator.authenticate()
    }

    fun buttonsVisibilityGone() {}

    fun updateAuthToolbarTitle(title: String) {
        AuthenticationActivity.setAuthToolbarTitle(title)
    }

}