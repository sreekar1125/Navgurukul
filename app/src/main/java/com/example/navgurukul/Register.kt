package com.example.navgurukul

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Register : AppCompatActivity() {

    lateinit var emailEt : EditText
    lateinit var passwordEt : EditText
    lateinit var signupBtn : Button
    lateinit var gsignupBtn : Button
    lateinit var loginBtn : TextView

    private lateinit var auth : FirebaseAuth

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailEt = findViewById(R.id.email)
        passwordEt = findViewById(R.id.password)
        signupBtn = findViewById(R.id.register)
        gsignupBtn = findViewById(R.id.gregister)
        loginBtn = findViewById(R.id.login)

        auth = Firebase.auth

        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signupBtn.setOnClickListener{
            signUpUser()
        }

        loginBtn.setOnClickListener(){
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        gsignupBtn.setOnClickListener{
            signInGoogle()
        }

    }

    private fun signInGoogle() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                SavedPreference.setEmail(this, account.email.toString())
//                SavedPreference.setUsername(this, account.displayName.toString())
                val intent = Intent(this, Welcome::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(
                Intent(
                    this, Welcome
                    ::class.java
                )
            )
            finish()
        }
    }

    private fun signUpUser(){
        val email = emailEt.text.toString()
        val pass = passwordEt.text.toString()


        if (email.isBlank() || pass.isBlank() ) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }


        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if(it.isSuccessful){
                intent = Intent(this, Welcome::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "SignUp Failed ", Toast.LENGTH_LONG).show()
            }
        }

    }
}