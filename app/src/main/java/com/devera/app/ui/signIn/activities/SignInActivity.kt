package com.devera.app.ui.signIn.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devera.app.MainActivity
import com.devera.app.R
import com.devera.app.network.ApiInterface
import com.devera.app.network.RetrofitInstance
import com.devera.app.ui.register.activities.RegisterActivity
import com.devera.app.ui.signIn.model.LoginModel
import com.devera.app.ui.signIn.model.SignInBody
import com.google.android.material.textfield.TextInputEditText
import com.learnawy.app.storage.AppReferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    lateinit var createAccount: TextView
    lateinit var signInBtn: Button;
    lateinit var emailEdit: TextInputEditText;
    lateinit var passwordEdit: TextInputEditText
    lateinit var loading: View
    var ctx = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        if (AppReferences.getLoginState(ctx)) {
            val i = Intent(ctx, MainActivity::class.java)
            startActivity(i)
            finish()
        }
        initViews()

    }

    private fun initViews() {
        createAccount = findViewById(R.id.createAccount)
        signInBtn = findViewById(R.id.login);
        createAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        emailEdit = findViewById(R.id.userName)
        passwordEdit = findViewById(R.id.password)
        loading = findViewById(R.id.loading)
        signInBtn.setOnClickListener {
            val email = emailEdit.text.toString().trim()
            val password = passwordEdit.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                Toast.makeText(ctx, "Please enter fill all information ", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun signIn(email: String, password: String) {
        loading.visibility = View.VISIBLE
        val retIn = RetrofitInstance.getRetrofitInstance(this).create(ApiInterface::class.java)
        val signInInfo = SignInBody(email, password)

        retIn.signIn(signInInfo).enqueue(object : Callback<LoginModel> {
            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                loading.visibility = View.GONE
                Toast.makeText(
                    this@SignInActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                loading.visibility = View.GONE
                if (response.body() != null) {
                    if (response.body()!!.status) {
                        Toast.makeText(
                            this@SignInActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        AppReferences.setLoginState(ctx, true)
                        AppReferences.setUserData(ctx, response.body()!!.user)
                        val i = Intent(ctx, MainActivity::class.java);
                        startActivity(i)
                        finish()
                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@SignInActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
