package com.example.navgurukul

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class MainActivity : AppCompatActivity() {

    lateinit var emailEt : EditText
    lateinit var passwordEt : EditText
    lateinit var loginBtn : Button
    lateinit var gloginBtn : Button
    lateinit var signupBtn : Button
    lateinit var forgotPass : TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEt = findViewById(R.id.email)
        passwordEt = findViewById(R.id.password)
        loginBtn = findViewById(R.id.login)
        gloginBtn = findViewById(R.id.glogin)
        signupBtn = findViewById(R.id.signup)
        forgotPass = findViewById(R.id.forgotPassword)

        auth = FirebaseAuth.getInstance()


        loginBtn.setOnClickListener{
            login()
        }

        signupBtn.setOnClickListener(){
            intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        forgotPass.setOnClickListener{
            forgotPassword()
        }
    }

    private fun forgotPassword() {
        val email = emailEt.text.toString()

        auth.sendPasswordResetEmail(email).addOnCompleteListener(this){
            if(it.isSuccessful){

                Toast.makeText(applicationContext, "Password reset link sent... ", Toast.LENGTH_LONG).show()
            }else{

                Toast.makeText(applicationContext, "Password reset Failed ", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun login(){
        val email = emailEt.text.toString()
        val pass = passwordEt.text.toString()


        if (email.isBlank() || pass.isBlank() ) {
            Toast.makeText(applicationContext, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if(it.isSuccessful){
                intent =Intent(this, Welcome::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext, "Login Failed ", Toast.LENGTH_LONG).show()
            }
        }
    }
}