package com.example.snapguard.ui

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImage
import com.example.snapguard.R
import com.example.snapguard.databinding.FragmentProfileBinding
import com.example.snapguard.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var storageRef : StorageReference


    var fileUri: Uri? = null
    var isChanged : Boolean = false



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val view = binding.root
        database = Firebase.database.reference
        auth = Firebase.auth
        storageRef = FirebaseStorage.getInstance().reference

        setEditTextsEditable(false)

        prefillData()


        binding.fragmentProfileBtnEditProfile.setOnClickListener {
            setEditTextsEditable(true)
            binding.fragmentProfileBtnSubmit.visibility = View.VISIBLE
            binding.fragmentProfileBtnEditProfile.visibility = View.GONE
        }

        binding.fragmentProfileBtnSubmit.setOnClickListener {
            saveChanges()

            // Disable editing of EditText fields after submitting changes
            setEditTextsEditable(false)

            // Show the Edit Profile button and hide the Submit Changes button
            binding.fragmentProfileBtnEditProfile.visibility = View.VISIBLE
            binding.fragmentProfileBtnSubmit.visibility = View.GONE

            // Show a toast indicating that changes have been saved
            Toast.makeText(context, "Changes saved successfully", Toast.LENGTH_SHORT).show()
        }


        binding.fragmentProfileEditProfileText.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Image to Upload"), 0
            )

        }

        binding.fragmentProfileUploadProfileText.setOnClickListener {
            if (fileUri != null) {
                uploadImage()
            } else {
                Toast.makeText(requireContext(), "Please Select Image to Upload", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, fileUri)
                binding.fragmentProfileProfileImage.setImageBitmap(bitmap)
                isChanged = true
                updateButtonText()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error Loading image", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadImage() {
        if (fileUri != null) {
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setTitle("Uploading Image...")
            progressDialog.setMessage("Processing...")
            progressDialog.show()

            val userProfileImageRef = storageRef.child("ProfilePhotos")
                .child("${System.currentTimeMillis()}_profile.jpg")

            userProfileImageRef.putFile(fileUri!!).addOnSuccessListener { taskSnapshot ->
                progressDialog.dismiss()
                userProfileImageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveImageUrlToDatabase(uri.toString())
                    Toast.makeText(
                        requireContext(),
                        "File Uploaded Successfully",
                        Toast.LENGTH_LONG
                    ).show()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Error getting download URL", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "File Upload Failed...", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun prefillData() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        // Check if the user is authenticated
        if (userId != null) {
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setTitle("Getting your Profile")
            progressDialog.setMessage("Please Wait")
            progressDialog.show()
            // Reference to the "Users" node in the database
            val userRef = database.child("Users").child(userId)

            // Add a ValueEventListener to fetch data
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Check if the dataSnapshot exists and has value
                    if (dataSnapshot.exists()) {
                        // Retrieve user data
                        val email = dataSnapshot.child("email").getValue(String::class.java)
                        val name = dataSnapshot.child("name").getValue(String::class.java)
                        val phoneNumber = dataSnapshot.child("phoneNumber").getValue(String::class.java)
                        val profileImageUrl = dataSnapshot.child("profilePhotoUrl").getValue(String::class.java)

                        binding.fragmentProfileEmailView.setText(email)
                        binding.fragmentProfileNameView.setText(name)
                        binding.fragmentProfilePhoneNumberView.setText(phoneNumber)

                        if (!profileImageUrl.equals("")) {
                            Glide.get(requireContext())
                            Glide.with(requireContext())
                                .load(profileImageUrl)
                                .placeholder(R.drawable.person)
                                .into(binding.fragmentProfileProfileImage)
                        } else {
                            binding.fragmentProfileProfileImage.setImageResource(R.drawable.person)
                        }

                    } else {
                        Toast.makeText(context, "Snap shot not exist", Toast.LENGTH_LONG).show()
                    }
                    progressDialog.dismiss()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors here
                    Toast.makeText(context, "Error fetching data: ${databaseError.message}", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            })
        }
    }

    private fun saveChanges() {
        // Retrieve updated values from EditText fields
        val updatedEmail = binding.fragmentProfileEmailView.text.toString()
        val updatedName = binding.fragmentProfileNameView.text.toString()
        val updatedPhoneNumber = binding.fragmentProfilePhoneNumberView.text.toString()

        if (!updatedName.isNullOrEmpty() && !updatedEmail.isNullOrEmpty() || !updatedPhoneNumber.isNullOrEmpty()) {
            // Save the updated values to the database
            val currentUser = auth.currentUser
            val userId = currentUser?.uid

            if (userId != null) {
                val userRef = database.child("Users").child(userId)
                // Update the values in the database
                userRef.child("email").setValue(updatedEmail)
                userRef.child("name").setValue(updatedName)
                userRef.child("phoneNumber").setValue(updatedPhoneNumber)

            }
        } else {
            Toast.makeText(requireContext(), "Please Fill all the details", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveImageUrlToDatabase(imageUrl: String) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val userRef = database.child("Users").child(userId)
            // Update the profile image URL in the database
            userRef.child("profilePhotoUrl").setValue(imageUrl)
        }
    }
    private fun setEditTextsEditable(editable: Boolean) {
        binding.fragmentProfileEmailView.isEnabled = editable
        binding.fragmentProfileNameView.isEnabled = editable
        binding.fragmentProfilePhoneNumberView.isEnabled = editable

        val defaultTextColor = if (!editable) R.color.blue else R.color.grey  // Replace Color.BLACK with your actual default text color
        binding.fragmentProfileEmailView.setTextColor(ContextCompat.getColor(requireContext(), defaultTextColor))
        binding.fragmentProfileNameView.setTextColor(ContextCompat.getColor(requireContext(), defaultTextColor))
        binding.fragmentProfilePhoneNumberView.setTextColor(ContextCompat.getColor(requireContext(), defaultTextColor))

    }

    private fun updateButtonText() {
        if (fileUri != null && isChanged) {
            binding.fragmentProfileEditProfileText.visibility = View.GONE
            binding.fragmentProfileUploadProfileText.visibility = View.VISIBLE
        }
    }


}