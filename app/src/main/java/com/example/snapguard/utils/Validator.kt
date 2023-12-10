package com.example.snapguard.utils

import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Utility class containing functions for validating email, password, and confirming passwords.
 *
 * This class provides methods to validate email addresses, passwords, and confirm that passwords
 * match. It supports integration with Android's Material Design components like TextInputEditText
 * and TextInputLayout for seamless integration into modern Android UIs.
 *
 * @property emailValidator Validates the format of an email address and checks for emptiness.
 * @property passwordValidator Validates the strength of a password based on specified criteria.
 * @property confirmPasswordValidator Checks if a password matches a confirmed password.
 */
class Validator {

    companion object {

        /**
         * Validates the format of an email address and checks for emptiness.
         *
         * @param textInputEditText The TextInputEditText containing the email address.
         * @param textInputLayout The TextInputLayout associated with the email input field.
         * @return `true` if the email is valid; otherwise, `false`.
         */
        fun emailValidator(textInputEditText: TextInputEditText, textInputLayout: TextInputLayout): Boolean {
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
            return isValid
        }

        /**
         * Validates the strength of a password based on specified criteria.
         *
         * @param textInputEditText The TextInputEditText containing the password.
         * @param textInputLayout The TextInputLayout associated with the password input field.
         * @return `true` if the password is valid; otherwise, `false`.
         */
        fun passwordValidator(textInputEditText: TextInputEditText, textInputLayout: TextInputLayout): Boolean {
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

        /**
         * Checks if a password matches a confirmed password.
         *
         * @param passwordEditText The TextInputEditText containing the original password.
         * @param passwordLayout The TextInputLayout associated with the original password input field.
         * @param confirmPasswordEditText The TextInputEditText containing the confirmed password.
         * @param confirmPasswordLayout The TextInputLayout associated with the confirmed password input field.
         * @return `true` if the passwords match; otherwise, `false`.
         */
        fun confirmPasswordValidator(
            passwordEditText: TextInputEditText,
            passwordLayout: TextInputLayout,
            confirmPasswordEditText: TextInputEditText,
            confirmPasswordLayout: TextInputLayout
        ): Boolean {
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
