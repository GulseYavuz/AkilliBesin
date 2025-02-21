package com.example.akillibesin.frontend.authentication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.akillibesin.R
import com.example.akillibesin.core.authentication.Authenticator
import com.example.akillibesin.core.database.authentication.AuthViewModel
import com.example.akillibesin.core.database.authentication.InternetConnection
import com.example.akillibesin.core.database.authentication.Login
import com.example.akillibesin.core.database.authentication.SharedPreferences
import com.example.akillibesin.databinding.LayoutErrorCustomViewBinding
import com.example.akillibesin.databinding.FragmentLoginBinding
import com.example.akillibesin.frontend.authentication.activities.SplashActivity
import com.example.akillibesin.frontend.factory.ErrorAlertDialog
import com.example.akillibesin.frontend.home.activities.MainActivity

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var authViewModel: AuthViewModel

    companion object {
        fun newInstance() = LoginFragment().apply {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        Authenticator.titleViewModel.mutableAuthTitleLD.value = getString(R.string.login)
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        observeAuthCallBackChange()

        handleClickButtons()
    }


    private fun handleClickButtons() {
        binding.loginBtn.setOnClickListener {
            email = binding.loginEmail.text?.trim().toString()
            password = binding.loginPassword.text.toString()

            if (isValidLoginInput(email, password)) {
                hasAccount()
            } else {
                alert(getString(R.string.error_title), getString(R.string.error_message))
            }
        }
    }

    fun isValidLoginInput(
        email: String,
        password: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty())
            return false
        return true
    }

    private fun hasAccount() {
        if (InternetConnection.isConnected(this.requireContext())) {
            val login = Login(email, password)
            login.isUserLoggedIn(authViewModel)
        } else
            Toast.makeText(activity, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                .show()
    }

    private fun observeAuthCallBackChange() {
        authViewModel.mutableIsAuthComplete.observe(viewLifecycleOwner) { isLogged ->
            if (isLogged) {
                showProgress()
                commitEmailOnSharedPreference()
                openMainActivity()
            } else
                Toast.makeText(activity, getString(R.string.failed_to_login), Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun commitEmailOnSharedPreference() {
        val preference = this.context?.let { SharedPreferences(it) }
        preference?.write(email)
    }

    private fun alert(title: String, message: String) {
        val view: LayoutErrorCustomViewBinding = binding.loginErrorView
        ErrorAlertDialog.alert(view, title, message)
    }

    private fun showProgress() {
        binding.loginProgress.visibility = View.VISIBLE
    }

    private fun openMainActivity() {
        Toast.makeText(activity, getString(R.string.confirmation_logged_msg), Toast.LENGTH_SHORT)
            .show()
        startActivity(Intent(activity, MainActivity::class.java))
        SplashActivity.instance.finish()
        activity?.finish()
    }
}