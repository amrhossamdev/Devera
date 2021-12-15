package com.devera.app.ui.register.activities

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.devera.app.R

class RegisterActivity : AppCompatActivity() {
    lateinit var back:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
    }
    private fun initView(){
        back = findViewById(R.id.backButton)
        back.setOnClickListener{
            finish();
        }
    }
}