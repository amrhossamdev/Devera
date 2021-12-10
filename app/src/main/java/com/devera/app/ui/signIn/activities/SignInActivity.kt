package com.devera.app.ui.signIn.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.devera.app.MainActivity
import com.devera.app.R
import com.devera.app.ui.register.activities.RegisterActivity
import kotlin.math.sign

class SignInActivity : AppCompatActivity() {
    lateinit var createAccount: TextView
    lateinit var signInBtn : Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
    }

    private fun initViews() {
        createAccount = findViewById(R.id.createAccount)
        signInBtn = findViewById(R.id.login);
        createAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        signInBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}