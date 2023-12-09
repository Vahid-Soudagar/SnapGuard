package com.example.snapguard.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.snapguard.R
import com.example.snapguard.databinding.FragmentLoginBinding
import com.example.snapguard.utils.Functions


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val view = binding.root


        navigations()


        binding.fragmentLoginBtnLogin.setOnClickListener {
            if (validation()) {
                Toast.makeText(context, "Login Successfully", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        return view
    }

    private fun navigations() {
        binding.fragmentLoginNewUser.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.fragmentLoginForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
    }

    private fun validation(): Boolean {
        val isEmailValid = Functions.emailValidator(binding.fragmentLoginEmail, binding.fragmentLoginLayoutEmail)
        val isPasswordValid = Functions.passwordValidator(binding.fragmentLoginPassword, binding.fragmentLoginLayoutPassword)

        // Check both email and password validity
        return isEmailValid && isPasswordValid
    }
}