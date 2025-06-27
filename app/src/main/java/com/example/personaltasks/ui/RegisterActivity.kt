package com.example.personaltasks.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityRegisterBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.title = "Register"

        binding.signUpBtn.setOnClickListener {
            scope.launch {
                Firebase.auth.createUserWithEmailAndPassword(
                    binding.emailRegisterEt.text.toString(),
                    binding.passwordRegisterEt.text.toString()
                ).addOnSuccessListener {
                    Toast.makeText(this@RegisterActivity,
                        "Registration successful", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this@RegisterActivity,
                        "Registration failed. Cause: ${it.message}", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }

    }
}