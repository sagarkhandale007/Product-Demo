package com.example.test.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth


class UserDetailsActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val categoryTextView = findViewById<TextView>(R.id.categoryTextView)
        val priceTextView = findViewById<TextView>(R.id.priceTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        val productImageView = findViewById<ImageView>(R.id.productImageView)

        val title = intent.getSerializableExtra("title")
        val category = intent.getSerializableExtra("category")
        val price = intent.getSerializableExtra("price")
        val description = intent.getSerializableExtra("description")
        val image = intent.getSerializableExtra("image")

        if (title != null) {

            titleTextView.text = title.toString()
            categoryTextView.text = category.toString()
            priceTextView.text = "Rs."+ price.toString()
            descriptionTextView.text = description.toString()
            Glide.with(this)
                .load(image)
                .into(productImageView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {

                mAuth.signOut()
                Toast.makeText(this, "SignOut Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@UserDetailsActivity, LoginActivity::class.java))
                finish()
            }
        }
        return true
    }
}
