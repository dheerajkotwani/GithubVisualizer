package project.dheeraj.githubvisualizer

import android.database.Observable
import android.telecom.Call
import okhttp3.Response
import retrofit2.http.*
import java.net.ResponseCache
import java.util.*

interface GithubApiInterface {

    @GET("users/{user}")
    fun Check(@Query("user") user: String): retrofit2.Call<ProfileModel>

    @Headers("Content-Type: application/json")
    @GET("/user")
    fun getUserInfo(
        @Header("Authorization") user: String
    ): retrofit2.Call<ProfileModel>



}