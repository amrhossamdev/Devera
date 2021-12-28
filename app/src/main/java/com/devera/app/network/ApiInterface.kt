package com.devera.app.network

import android.content.Context
import com.devera.app.ui.baseModel.BaseResponse
import com.devera.app.ui.groups.models.Body.AddPostBody
import com.devera.app.ui.groups.models.Body.GroupBody
import com.devera.app.ui.groups.models.GroupResponse
import com.devera.app.ui.home.models.BodyRequestsModel.AddCommentBody
import com.devera.app.ui.home.models.BodyRequestsModel.CommentsBody
import com.devera.app.ui.home.models.BodyRequestsModel.ProfileBody
import com.devera.app.ui.home.models.BodyRequestsModel.UpVotingBody
import com.devera.app.ui.home.models.CommentModel
import com.devera.app.ui.home.models.HomeBody
import com.devera.app.ui.home.models.VoteActionsModel
import com.devera.app.ui.profile.models.Body.UpdatePassword
import com.devera.app.ui.profile.models.Body.UpdateProfile
import com.devera.app.ui.profile.models.ProfileResponse
import com.devera.app.ui.register.model.RegisterBody
import com.devera.app.ui.signIn.model.LoginModel
import com.devera.app.ui.signIn.model.SignInBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiInterface {

    @POST("login")
    fun signIn(
        @Body info: SignInBody
    ): Call<LoginModel>

    @POST("signUp")
    fun signUp(
        @Body info: RegisterBody
    ): Call<BaseResponse>

    @POST("homePage")
    fun getFeed(
        @Body info: HomeBody
    ): Call<ProfileResponse>

    @POST("upVote")
    fun upVote(
        @Body info: UpVotingBody
    ): Call<VoteActionsModel>

    @POST("downVote")
    fun downVote(
        @Body info: UpVotingBody
    ): Call<VoteActionsModel>

    @POST("postComments")
    fun getComments(
        @Body info: CommentsBody
    ): Call<CommentModel>

    @POST("addComment")
    fun addComment(
        @Body info: AddCommentBody
    ): Call<BaseResponse>

    @POST("ProfilePosts")
    fun getProfile(
        @Body info: ProfileBody
    ): Call<ProfileResponse>

    @GET("groupData")
    fun getGroups(
    ): Call<GroupResponse>

    @POST("groupPage")
    fun getGroupPage(
        @Body info: GroupBody
    ): Call<ProfileResponse>

    @POST("addPost")
    fun addPost(
        @Body info: AddPostBody
    ): Call<BaseResponse>


    @POST("updateNames")
    fun updateProfile(
        @Body info: UpdateProfile
    ): Call<BaseResponse>


    @POST("updatePassword")
    fun updatePassword(
        @Body info: UpdatePassword
    ): Call<BaseResponse>

}


class RetrofitInstance {
    companion object {
        fun getRetrofitInstance(context: Context): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor).build() // for loging

            return Retrofit.Builder()
                .baseUrl("http://192.168.43.144:4200/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}
