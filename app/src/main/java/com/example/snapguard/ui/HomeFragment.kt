package com.example.snapguard.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.snapguard.R
import com.example.snapguard.databinding.FragmentHomeBinding
import com.example.snapguard.utils.AuthenticationCustomDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

/**
 * Fragment representing the home screen with a Toolbar and options menu.
 */
class HomeFragment : Fragment() {

    // Binding for the FragmentHome layout
    private lateinit var binding: FragmentHomeBinding

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Current user instance
    private lateinit var user: FirebaseUser

    /**
     * Inflates the layout for this fragment, sets up the Toolbar,
     * and enables the options menu.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root

        // Set up the Toolbar
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // Enable options menu
        setHasOptionsMenu(true)

        // Initialize Firebase Authentication
        auth = Firebase.auth

        return view
    }

    /**
     * Inflates the options menu.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Handles item selection in the options menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.options_menu_logout -> {
                // Handle logout menu item
                signOut()
                return true
            }
            // Add other cases if you have more menu items
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Signs out the user and navigates to the login screen.
     */
    private fun signOut() {
        auth.signOut()
        // Navigate to the appropriate screen after logout
        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    /**
     * Checks the authentication status on fragment start and displays
     * an authentication dialog if the user is not logged in.
     */
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Show authentication dialog if the user is not logged in
            val authenticationCustomDialog = AuthenticationCustomDialog(this)
            authenticationCustomDialog.show()
        } else {
            user = currentUser
        }
    }
}
