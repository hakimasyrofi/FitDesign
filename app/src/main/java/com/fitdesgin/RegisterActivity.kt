package com.fitdesgin

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var email= ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In ...")
        progressDialog.setCanceledOnTouchOutside(false)

        // Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val btn_sign_up = findViewById<Button>(R.id.btn_sign_up)
        val btn_login = findViewById<TextView>(R.id.login)

        btn_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btn_sign_up.setOnClickListener{
            // before login, validate data
            validateData()
        }

    }

    private fun validateData() {
        val et_full_name: EditText = findViewById(R.id.et_full_name)
        val et_email = findViewById<EditText>(R.id.et_email)
        val et_passwrod = findViewById<EditText>(R.id.et_password)

        // get data
        email = et_email.text.toString().trim()
        password = et_passwrod.text.toString().trim()

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // invalid email format
            et_email.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)){
            // no password entered
            et_full_name.error = "Please enter full name"
        }
        else if(TextUtils.isEmpty(password)){
            // no password entered
            et_passwrod.error = "Please enter password"
        }
        else if(password.length < 6){
            // no password entered
            et_passwrod.error = "Password must at least 6 characters long"
        }
        else{
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        val et_full_name: EditText = findViewById(R.id.et_full_name)
        // Show progress dialog
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Register Success
                progressDialog.dismiss()
                // get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Account created with email $email", Toast.LENGTH_SHORT).show()

                val userId = firebaseAuth.currentUser!!.uid

                val userData = hashMapOf("name" to et_full_name.text.toString(), "poin" to 0)

                db.collection("users").document(userId).set(userData)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error writing document", e)
                    }

                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Sign Up failed due to ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }
}