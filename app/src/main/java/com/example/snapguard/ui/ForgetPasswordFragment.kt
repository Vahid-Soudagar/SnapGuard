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


class ForgetPasswordFragment : Fragment() {

    private lateinit var binding : FragmentForgetPasswordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgetPasswordBinding.inflate(layoutInflater)
        val view = binding.root

        binding.fragmentForgetPasswordBtnSendEmail.setOnClickListener {
            if (validation()) {
                val emailAddress = binding.fragmentForgetPasswordEmail.text.toString()
                sendPasswordResetEmail(emailAddress)
            }
        }


        return view
    }

    private fun validation(): Boolean {
        return com.example.snapguard.utils.Validator.emailValidator(
            binding.fragmentForgetPasswordEmail,
            binding.fragmentForgetPasswordLayoutEmail
        )
    }

    private fun sendPasswordResetEmail(emailAddress: String) {
        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Email Send", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Email sending failed, Try after sometime", Toast.LENGTH_LONG).show()
                }
            }
    }

}