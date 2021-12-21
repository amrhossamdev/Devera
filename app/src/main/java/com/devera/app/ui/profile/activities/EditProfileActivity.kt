package com.devera.app.ui.profile.activities

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devera.app.R
import com.devera.app.network.ApiInterface
import com.devera.app.network.RetrofitInstance
import com.devera.app.ui.BaseModel.BaseResponse
import com.devera.app.ui.profile.models.Body.UpdatePassword
import com.devera.app.ui.profile.models.Body.UpdateProfile
import com.google.android.material.textfield.TextInputEditText
import com.learnawy.app.storage.AppReferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProfileActivity : AppCompatActivity() {
    private lateinit var fullName: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var userName: TextInputEditText
    private lateinit var confirmChanges: Button
    var ctx = this
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        initToolbar()
        initViews()
    }

    private fun initViews() {
        val changePassword: TextView = findViewById(R.id.changePassword)
        fullName = findViewById(R.id.fullname)
        email = findViewById(R.id.email)
        userName = findViewById(R.id.userName)
        confirmChanges = findViewById(R.id.confirmChanges)
        fullName.setText(AppReferences.getUserData(ctx)?.fullName)
        email.setText(AppReferences.getUserData(ctx)?.email)
        userName.setText(AppReferences.getUserData(ctx)?.userName)

        confirmChanges.setOnClickListener {
            var emailTxt = email.text.toString().trim()
            var userName = userName.text.toString().trim()
            var fullNameTxt = fullName.text.toString().trim()

            upDateUser(emailTxt, fullNameTxt, userName)
        }
        changePassword.setOnClickListener {
            showDialog(this@EditProfileActivity, "lol")
        }
    }

    private fun upDateUser(email: String, fullName: String, username: String) {
        val retIn =
            RetrofitInstance.getRetrofitInstance(ctx).create(ApiInterface::class.java)
        val updateProfile = UpdateProfile(
            email, fullName,
            AppReferences.getUserData(ctx)!!.id.toString(), username
        )
        retIn.updateProfile(updateProfile).enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.status) {
                        Toast.makeText(ctx, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(ctx, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updatePassword(newPassword: String, confirmPassword: String) {
        val retIn =
            RetrofitInstance.getRetrofitInstance(ctx).create(ApiInterface::class.java)
        val updatePassword = UpdatePassword(
            confirmPassword, AppReferences.getUserData(ctx)!!.id.toString(), newPassword
        )
        retIn.updatePassword(updatePassword).enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.status) {
                        dialog.dismiss()
                        Toast.makeText(ctx, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(ctx, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initToolbar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }
        title = "Edit Profile"
    }

    private fun showDialog(context: Context, msg: String?) {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val window: Window = dialog.window!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.setContentView(R.layout.change_password_dialog)
        var newPassword = dialog.findViewById<TextInputEditText>(R.id.newPassword)
        val confirmNewPassword = dialog.findViewById<TextInputEditText>(R.id.confirmNewPassword)
        var updatePassword = dialog.findViewById<ImageButton>(R.id.updatePassword)
        updatePassword.setOnClickListener {
            var newP = newPassword.text.toString().trim()
            var newPConfirm = confirmNewPassword.text.toString().trim()
            updatePassword(newP, newPConfirm)
        }
        dialog.show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}