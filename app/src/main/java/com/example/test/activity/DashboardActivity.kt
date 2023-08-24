package com.example.test.activity

import UserListAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.model.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class DashboardActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var data:ArrayList<User>
    private lateinit var userRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)
        userRecyclerView = findViewById<RecyclerView>(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        data = ArrayList<User>()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val dataSnapshot = fetchRealtimeDatabaseData()
                Log.d("dataSnapshot",dataSnapshot.toString())
            } catch (e: Exception) {
                Log.d("error",e.toString())
            }
        }
        val adapter = UserListAdapter(data)
        userRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                mAuth.signOut()
                Toast.makeText(this, "SignOut Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                finish()
            }
        }
        return true
    }

    suspend fun fetchRealtimeDatabaseData(): DataSnapshot {
        return suspendCoroutine { continuation ->

            val databaseReference = FirebaseDatabase.getInstance().getReference("data")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    continuation.resume(snapshot)

                    if(snapshot.exists()){
                        for(userSnapshot in snapshot.children){
                            val user = userSnapshot.getValue()
                            val ND=   listOf(user);
                            val jsonArray = JSONArray(ND)
                            val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                            val image= jsonObject.get("image")
                            val price= jsonObject.get("price")
                            val description= jsonObject.get("description")
                            val  rating = jsonObject.get("rating")
                            val rating_Object = JSONObject(rating.toString())
                            val rate = rating_Object.getDouble("rate")
                            val count = rating_Object.getInt("count")
                            val category= jsonObject.get("category")
                            val title= jsonObject.get("title")
                            val item = User(category, title, image, price, description, rate, count)
                            data.add(item)
                        }
                        val adapter = UserListAdapter(data)
                        userRecyclerView.adapter = adapter
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }
}
