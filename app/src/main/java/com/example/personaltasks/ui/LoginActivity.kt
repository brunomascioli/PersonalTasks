package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.title = "Login"

        binding.signInBtn.setOnClickListener {
            scope.launch {
                Firebase.auth.signInWithEmailAndPassword(
                    binding.emailEt.text.toString(),
                    binding.passwordEt.text.toString()
                ).addOnFailureListener {
                    Toast.makeText(this@LoginActivity,
                        "Login failed. Cause ${it.message}", Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    openMainActivity()
                }
            }
        }

        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.resetPasswordBtn.setOnClickListener {
            scope.launch {
                val email = binding.emailEt.text.toString()
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Firebase.auth.sendPasswordResetEmail(email).addOnSuccessListener {
                        Toast.makeText(this@LoginActivity, "Email de recuperação enviado",
                            Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { error ->
                        Toast.makeText(
                            this@LoginActivity,
                            "Erro ao enviar email: ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Por favor, informe um email válido",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser != null) {
            openMainActivity()
        }
    }

    private fun openMainActivity() {
        startActivity(Intent(
            this@LoginActivity, MainActivity::class.java)
        )
        finish()
    }
}