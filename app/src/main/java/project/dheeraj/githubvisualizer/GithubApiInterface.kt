package project.dheeraj.githubvisualizer

import android.database.Observable
import android.telecom.Call
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.ResponseCache
import java.util.*
interface GithubApiInterface {

    @GET("users/{user}")
    fun Check(@Query("user") user: String): retrofit2.Call<ProfileModel>


}