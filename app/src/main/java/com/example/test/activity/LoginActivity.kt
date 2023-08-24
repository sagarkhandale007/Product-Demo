package com.example.test.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.test.R
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        progressBar = findViewById(R.id.progress_circular)
        mAuth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (isValid(username, password)) {
                lifecycleScope.launch(Dispatchers.IO){
                    signInWithEmailAndPassword(mAuth,username,password)
                }
            } else {
            }
        }
    }

    suspend fun signInWithEmailAndPassword(
        firebaseAuth: FirebaseAuth,
        emailId: String,
        password: String) : AuthResult? {

        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(emailId,password)
                .await()

            updateUI(result.user)
            result
        } catch (e :Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@LoginActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("AuthResult","${e.message}")
                progressBar.visibility = View.GONE
            }
            null
        }
    }

    private suspend fun updateUI(firebaseUser : FirebaseUser?) {
        Log.d("AuthResult","${firebaseUser?.email}")

        withContext(Dispatchers.Main){
            progressBar.visibility = View.GONE
            Toast.makeText(this@LoginActivity, "Success", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            finish()
        }
    }

    private fun isValid(username: String, password: String): Boolean {

        if (username.isEmpty() || password.isEmpty()) {
            displayToast("Username and password are required.")
            return false
        }
        return true
    }

    private fun displayToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth.currentUser
        if (currentUser!=null){
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            finish()
        }
    }

}
