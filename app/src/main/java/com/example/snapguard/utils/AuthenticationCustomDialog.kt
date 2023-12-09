package com.example.snapguard.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button  // Add this import statement
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.snapguard.R

class AuthenticationCustomDialog(private val fragment: Fragment) {
    fun show() {
        val builder = AlertDialog.Builder(fragment.requireContext())
        val inflater = LayoutInflater.from(fragment.requireContext())
        val dialogView: View = inflater.inflate(R.layout.layout_custom_dialog_box_authentication_banner, null)

        builder.setView(dialogView)

        val alertDialog = builder.create()

        dialogView.findViewById<Button>(R.id.btnCreateAccount).setOnClickListener {
            fragment.findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnLogin).setOnClickListener {
            fragment.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
