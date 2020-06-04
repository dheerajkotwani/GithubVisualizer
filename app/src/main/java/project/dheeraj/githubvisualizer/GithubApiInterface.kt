package project.dheeraj.githubvisualizer

import EventsModel
import FeedModel
import GithubUserModel
import NotificationModel
import retrofit2.http.*
import kotlin.collections.ArrayList

interface GithubApiInterface {

    @GET("users/{user}")
    fun Check(@Query("user") user: String): retrofit2.Call<ProfileModel>

    @Headers("Content-Type: application/json")
    @GET("/user")
    fun getUserInfo(
        @Header("Authorization") user: String
    ): retrofit2.Call<GithubUserModel>

    @Headers("Content-Type: application/json")
    @GET("/feeds")
    fun getFeeds(
        @Header("Authorization") user: String
    ): retrofit2.Call<FeedModel>

  @Headers("Content-Type: application/json")
    @GET("/notifications")
    fun getNotifications(
        @Header("Authorization") user: String
    ): retrofit2.Call<ArrayList<NotificationModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/received_events")
    fun getEvents(
        @Header("Authorization") user: String,
        @Path("username") username:String
    ): retrofit2.Call<ArrayList<EventsModel>>


}