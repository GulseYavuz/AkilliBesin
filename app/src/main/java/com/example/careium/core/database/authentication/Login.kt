package com.example.careium.core.database.authentication

import android.os.Handler
import android.os.Looper

class Login(private var email: String, private var password: String) : Auth() {

    override fun run() {
        // Simulate a fake login process
        val fakeLoginSuccessful = simulateLogin(this.email, this.password)

        // Use a Handler to simulate network delay
        Handler(Looper.getMainLooper()).postDelayed({
            // Set the view model with the fake login result
            this.authViewModel.mutableIsAuthComplete.value = fakeLoginSuccessful
        }, 2000) // Simulate 2 seconds delay
    }

    fun isUserLoggedIn(authViewModel: AuthViewModel) {
        this.authViewModel = authViewModel
        this.start()
    }

    // Method to simulate login
    private fun simulateLogin(email: String, password: String): Boolean {
        // Here you can define the logic for fake login
        // For example, let's say any email with "test" in it and password "1234" are valid
        return email.contains("test") && password == "1234"
    }
}

