package com.example.snapguard.utils

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.snapguard.R

/**
 * Utility class for creating and displaying a custom authentication dialog.
 *
 * This class provides a method to display a custom authentication dialog with "Create an Account" and "Login" buttons.
 *
 * @property fragment The fragment where the dialog is displayed.
 */
class AuthenticationCustomDialog(private val fragment: Fragment) {

    /**
     * Displays the custom authentication dialog.
     */
    fun show() {
        // Build the AlertDialog
        val builder = AlertDialog.Builder(fragment.requireContext())
        val inflater = LayoutInflater.from(fragment.requireContext())
        val dialogView: View =
            inflater.inflate(R.layout.layout_custom_dialog_box_authentication_banner, null)
        builder.setView(dialogView)

        // Create the AlertDialog
        val alertDialog = builder.setCancelable(false).create()

        // Set up click listeners for the dialog buttons
        dialogView.findViewById<Button>(R.id.btnCreateAccount).setOnClickListener {
            // Navigate to the registerFragment
            fragment.findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
            alertDialog.dismiss() // Close the dialog if needed
        }

        dialogView.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            // Navigate to the loginFragment
            fragment.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            alertDialog.dismiss() // Close the dialog if needed
        }

        // Show the dialog
        alertDialog.show()
    }
}
