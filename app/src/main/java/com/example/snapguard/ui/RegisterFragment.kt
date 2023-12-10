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
import com.example.snapguard.utils.Validator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth

        navigations()

        binding.fragmentRegisterBtnRegister.setOnClickListener {
            if (validation()) {
                val email = binding.fragmentRegisterEmail.text.toString()
                val password = binding.fragmentRegisterPassword.text.toString()
                createUserWithEmailAndPassword(email, password)
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
        val isValidEmail = Validator.emailValidator(binding.fragmentRegisterEmail, binding.fragmentRegisterLayoutEmail)
        val isValidPassword = Validator.passwordValidator(binding.fragmentRegisterPassword, binding.fragmentRegisterLayoutPassword)
        val isValidConfirmPassword =
            Validator.confirmPasswordValidator(binding.fragmentRegisterPassword, binding.fragmentRegisterLayoutPassword,
                binding.fragmentRegisterConfirmPassword, binding.fragmentRegisterLayoutConfirmPassword)

        return isValidEmail && isValidPassword && isValidConfirmPassword
    }


    private fun createUserWithEmailAndPassword(email: String, password: String)  {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Register Successfully", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                }
            }.addOnFailureListener() {task ->
                Toast.makeText(context, task.message, Toast.LENGTH_LONG).show()
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
        }
    }


}