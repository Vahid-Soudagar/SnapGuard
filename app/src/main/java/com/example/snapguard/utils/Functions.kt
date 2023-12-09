package com.example.snapguard.utils

import android.text.Editable
import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Functions {

    companion object {
        fun emailValidator(textInputEditText: TextInputEditText, textInputLayout: TextInputLayout) : Boolean {
            val email = textInputEditText.text.toString()
            val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            if (email.isNullOrEmpty()) {
                textInputLayout.error = "Email cannot be Empty"
                return false
            }
            if (!isValid) {
                textInputLayout.error = "Enter Valid Email Address"
                return false
            }
            textInputLayout.error = null
            return isValid;
        }

        fun passwordValidator(textInputEditText: TextInputEditText, textInputLayout: TextInputLayout) : Boolean {
            val password = textInputEditText.text.toString()
            val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+\$).{6,14}$".toRegex()

            if (password.isNullOrEmpty()) {
                textInputLayout.error = "Password cannot be Empty"
                return false
            }

            if (!passwordPattern.matches(password)) {
                textInputLayout.error =
                    "Password must be between 6 to 14 characters, contain 1 upper, 1 lower, 1 digit, and 1 special character"
                return false
            }

            textInputLayout.error = null
            return true
        }
        fun confirmPasswordValidator(passwordEditText: TextInputEditText,
                                     passwordLayout: TextInputLayout,
                                     confirmPasswordEditText: TextInputEditText,
                                     confirmPasswordLayout : TextInputLayout) : Boolean{

            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()



            if (password != confirmPassword) {
                confirmPasswordLayout.error = "Passwords do not match"
                return false
            }

            confirmPasswordLayout.error = null
            return true

        }
    }
}