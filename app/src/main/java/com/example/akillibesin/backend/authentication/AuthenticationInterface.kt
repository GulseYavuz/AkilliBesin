package com.example.akillibesin.backend.authentication

import com.example.akillibesin.core.authentication.Authenticator
import com.example.akillibesin.core.authentication.InitialActivity
import com.example.akillibesin.frontend.authentication.activities.AuthenticationActivity
import com.example.akillibesin.frontend.authentication.activities.SplashActivity

/**
 * The Interface hooks between the **frontend**(SplashActivity) and the **core**(InitialActivity).
 *
 * @author Kareem Sherif
 * @see SplashActivity
 * @see InitialActivity
 */
interface AuthenticationInterface {

    private val initialActivity: InitialActivity get() = InitialActivity()

    /**
     * Fires the LoginAction Method in the Core Package.
     *
     * @author Kareem Sherif
     * @see InitialActivity.authLoginAction
     */
    fun authLogin() {
        initialActivity.authLoginAction()
    }

    /**
     * Fires the RegisterAction Method in the Core Package.
     *
     * @author Kareem Sherif
     * @see InitialActivity.authLoginAction
     */
    fun authRegister() {
        initialActivity.authRegisterAction()
    }

    /**
     * Fires the **CheckAuthentication** Method in the Core Package.
     *
     * @author Kareem Sherif
     * @see InitialActivity.authLoginAction
     */
    fun authenticateUser() {
        val isValidAuth = initialActivity.authenticateUser()
        if (isValidAuth)
            buttonsVisibilityGone()
    }

    /**
     * Fires the **authenticate** Method in the Core Package.
     *
     * @author Kareem Sherif
     * @see Authenticator.authenticate
     */
    fun authOptions() {
        val authenticator = Authenticator()
        authenticator.authenticate()
    }

    /**
     * Fires the **buttonsVisibilityGone** (Buttons Visibility Action)
     * Method in the Frontend Package.
     *
     * @author Kareem Sherif
     * @see SplashActivity.buttonsVisibilityGone
     */
    fun buttonsVisibilityGone() {}

    /**
     * Fires the **setAuthToolbarTitle** (AuthToolbar text change Action)
     * Method in the Frontend Package.
     *
     * @author Kareem Sherif
     * @see AuthenticationActivity.setAuthToolbarTitle
     */
    fun updateAuthToolbarTitle(title: String) {
        AuthenticationActivity.setAuthToolbarTitle(title)
    }

}