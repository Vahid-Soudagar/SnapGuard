package com.example.snapguard.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.snapguard.databinding.FragmentForgetPasswordBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/**
 * Fragment responsible for handling password reset requests.
 */
class ForgetPasswordFragment : Fragment() {

    // Binding for the FragmentForgetPassword layout
    private lateinit var binding: FragmentForgetPasswordBinding

    /**
     * Inflates the layout for this fragment and sets up UI elements.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetPasswordBinding.inflate(layoutInflater)
        val view = binding.root

        // Set up click listener for the "Send Email" button
        binding.fragmentForgetPasswordBtnSendEmail.setOnClickListener {
            if (validation()) {
                val emailAddress = binding.fragmentForgetPasswordEmail.text.toString()
                sendPasswordResetEmail(emailAddress)
            }
        }

        return view
    }

    /**
     * Validate user input for email.
     * @return True if input is valid, false otherwise.
     */
    private fun validation(): Boolean {
        return com.example.snapguard.utils.Validator.emailValidator(
            binding.fragmentForgetPasswordEmail,
            binding.fragmentForgetPasswordLayoutEmail
        )
    }

    /**
     * Send a password reset email to the provided email address using Firebase Authentication.
     * Display a success message on successful email sending.
     */
    private fun sendPasswordResetEmail(emailAddress: String) {
        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    Toast.makeText(requireContext(), "Email Sent", Toast.LENGTH_LONG).show()
                } else {
                    // Password reset email sending failed
                    Toast.makeText(
                        requireContext(),
                        "Email sending failed, Try after sometime",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
