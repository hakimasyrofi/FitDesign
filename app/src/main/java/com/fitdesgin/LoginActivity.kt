package com.fitdesgin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    private var email= ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In ...")
        progressDialog.setCanceledOnTouchOutside(false)

        // Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        val btn_sign_in = findViewById<Button>(R.id.btn_sign_in)
        val register = findViewById<TextView>(R.id.register)

        btn_sign_in.setOnClickListener{
            // before login, validate data
            validateData()
        }
        register.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
    }

    private fun validateData() {
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
            et_passwrod.error = "Please enter password"
        }
        else{
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        // Show progress dialog
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener{
                    // Login Success
                    progressDialog.dismiss()
                    // get user info
                    val firebaseUser = firebaseAuth.currentUser
                    val email = firebaseUser!!.email
                    Toast.makeText(this,"Logged In as $email", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    // Login Failed
                    progressDialog.dismiss()
                    Toast.makeText(this,"Login failed due to ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun checkUser(){
        //If user alredy logged in, go to dashboard
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            // User alredy logged in
            startActivity(Intent(this,DashboardActivity::class.java))
            finish()
        }
    }
}