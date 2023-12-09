package com.example.snapguard.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.snapguard.R
import com.example.snapguard.databinding.FragmentRegisterBinding
import com.example.snapguard.utils.Functions

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        navigations()

        binding.fragmentRegisterBtnRegister.setOnClickListener {
            if (validation()) {
                Toast.makeText(context, "Register Successfully", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            }
        }

        return view
    }

    private fun navigations() {
        binding.fragmentRegisterAlreadyHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun validation(): Boolean {
        val isValidEmail = Functions.emailValidator(binding.fragmentRegisterEmail, binding.fragmentRegisterLayoutEmail)
        val isValidPassword = Functions.passwordValidator(binding.fragmentRegisterPassword, binding.fragmentRegisterLayoutPassword)
        val isValidConfirmPassword =
            Functions.confirmPasswordValidator(binding.fragmentRegisterPassword, binding.fragmentRegisterLayoutPassword,
                binding.fragmentRegisterConfirmPassword, binding.fragmentRegisterLayoutConfirmPassword)

        return isValidEmail && isValidPassword && isValidConfirmPassword
    }

}