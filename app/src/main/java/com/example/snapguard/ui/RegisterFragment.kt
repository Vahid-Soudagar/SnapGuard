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
import com.example.snapguard.models.User
import com.example.snapguard.utils.Validator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

/**
 * Fragment responsible for user registration.
 */
class RegisterFragment : Fragment() {

    // Binding for the FragmentRegister layout
    private lateinit var binding: FragmentRegisterBinding

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference

    /**
     * Inflates the layout for this fragment, sets up UI elements, and initializes Firebase Authentication.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth
        database = Firebase.database.reference

        // Set up navigation and button click listeners
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

    /**
     * Set up navigation and click listeners for UI elements.
     */
    private fun navigations() {
        binding.fragmentRegisterAlreadyHaveAnAccount.setOnClickListener {
            // Navigate to the loginFragment if the user already has an account
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    /**
     * Validate user input for email, password, and confirm password.
     * @return True if input is valid, false otherwise.
     */
    private fun validation(): Boolean {
        val isValidEmail =
            Validator.emailValidator(binding.fragmentRegisterEmail, binding.fragmentRegisterLayoutEmail)
        val isValidPassword =
            Validator.passwordValidator(binding.fragmentRegisterPassword, binding.fragmentRegisterLayoutPassword)
        val isValidConfirmPassword = Validator.confirmPasswordValidator(
            binding.fragmentRegisterPassword, binding.fragmentRegisterLayoutPassword,
            binding.fragmentRegisterConfirmPassword, binding.fragmentRegisterLayoutConfirmPassword
        )

        // Check both email and password validity
        return isValidEmail && isValidPassword && isValidConfirmPassword
    }

    /**
     * Create a new user with the provided email and password using Firebase Authentication.
     * Display a success message and navigate to the homeFragment on success.
     */
    private fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    var currentUser = auth.currentUser
                    val userId = currentUser?.uid
                    val user = userId?.let { User(it, email, password, "", "", "") };
                    if (userId != null) {
                        database.child("Users").child(userId).setValue(user)
                    }
                    Toast.makeText(context, "Register Successfully", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                }
            }.addOnFailureListener { task ->
                // Registration failed, display error message
                Toast.makeText(context, task.message, Toast.LENGTH_LONG).show()
            }
    }


    /**
     * Check if a user is already logged in on fragment start and navigate to the homeFragment if true.
     */
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User already logged in, navigate to homeFragment
            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
        }
    }
}
