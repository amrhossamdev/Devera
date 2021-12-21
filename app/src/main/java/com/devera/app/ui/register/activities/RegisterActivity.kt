package com.devera.app.ui.register.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devera.app.R
import com.devera.app.network.ApiInterface
import com.devera.app.network.RetrofitInstance
import com.devera.app.ui.BaseModel.BaseResponse
import com.devera.app.ui.register.model.RegisterBody
import com.devera.app.ui.signIn.activities.SignInActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var back: ImageButton
    lateinit var choosePhoto: ImageView
    lateinit var fullName: TextInputEditText
    lateinit var userName: TextInputEditText
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var confirmPassword: TextInputEditText
    lateinit var signUp: Button
    lateinit var loading: View
    var ctx = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun initView() {
        back = findViewById(R.id.backButton)
        choosePhoto = findViewById(R.id.editProfile)
        fullName = findViewById(R.id.fullname)
        userName = findViewById(R.id.userName)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        signUp = findViewById(R.id.signUp)
        loading = findViewById(R.id.loading)
        back.setOnClickListener {
            finish();
        }
        choosePhoto.setOnClickListener {
            Toast.makeText(ctx, "Soon", Toast.LENGTH_SHORT).show()
        }
        signUp.setOnClickListener {
            val emailTxt = email.text.toString().trim()
            val fullName = fullName.text.toString().trim()
            val userName = userName.text.toString().trim()
            val password = password.text.toString().trim()
            val confirmPassword = confirmPassword.text.toString()
            if (emailTxt.isNotEmpty() && fullName.isNotEmpty()
                && password.isNotEmpty() && confirmPassword.isNotEmpty()
                && userName.isNotEmpty()
            ) {
                if (isEmailValid(emailTxt)) {
                    if (password == confirmPassword) {
                        signUp(fullName, userName, emailTxt, password, confirmPassword)
                    } else {
                        Toast.makeText(
                            ctx,
                            "Password should match confirm password",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(ctx, "Enter valid email", Toast.LENGTH_SHORT)
                        .show()

                }
            } else {
                Toast.makeText(ctx, "Please fill all data", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun signUp(
        fullName: String,
        userName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        loading.visibility = View.VISIBLE
        val retIn = RetrofitInstance.getRetrofitInstance(this).create(ApiInterface::class.java)
        val signUpBody = RegisterBody(fullName, userName, email, password, confirmPassword)
        retIn.signUp(signUpBody).enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                loading.visibility = View.GONE

                Toast.makeText(
                    ctx,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                loading.visibility = View.GONE

                if (response.body() != null) {
                    if (response.body()!!.status) {
                        Toast.makeText(
                            ctx,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        val i = Intent(ctx, SignInActivity::class.java);
                        startActivity(i)
                    } else {
                        Toast.makeText(
                            ctx,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        ctx,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}

