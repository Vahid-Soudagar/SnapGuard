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
import com.example.snapguard.utils.Validator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

/**
 * Fragment representing the login screen of the SnapGuard app.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth

        navigations()

        binding.fragmentLoginBtnLogin.setOnClickListener {
            if (validation()) {
                val email = binding.fragmentLoginEmail.text.toString()
                val password = binding.fragmentLoginPassword.text.toString()
                signInWithEmailAndPassword(email, password)
            }
        }

        return view
    }

    /**
     * Set up click listeners for navigation buttons.
     */
    private fun navigations() {
        binding.fragmentLoginNewUser.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.fragmentLoginForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
    }

    /**
     * Perform input validation for email and password.
     *
     * @return `true` if both email and password are valid; otherwise, `false`.
     */
    private fun validation(): Boolean {
        val isEmailValid = Validator.emailValidator(binding.fragmentLoginEmail, binding.fragmentLoginLayoutEmail)
        val isPasswordValid = Validator.passwordValidator(binding.fragmentLoginPassword, binding.fragmentLoginLayoutPassword)

        // Check both email and password validity
        return isEmailValid && isPasswordValid
    }

    /**
     * Attempt to sign in with the provided email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     */
    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Login Successfully", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Toast.makeText(context, "Login Failed" + task.exception, Toast.LENGTH_LONG).show()
                }
            }
    }

    /**
     * Called when the fragment is visible to the user.
     * Check if the user is already signed in and navigate to the home screen if true.
     */
    override fun onStart() {
        super.onStart()
        // Check if the user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }
}
