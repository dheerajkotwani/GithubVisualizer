package project.dheeraj.githubvisualizer

import EventsModel
import FollowerModel
import project.dheeraj.githubvisualizer.Model.FeedModels.FeedModel
import GithubUserModel
import NotificationModel
import RepositoryModel
import SearchModel
import retrofit2.http.*
import kotlin.collections.ArrayList

interface GithubApiInterface {

    @Headers("Content-Type: application/json")
    @GET("users/{user}")
    fun getPublicUser(
        @Header("Authorization") token: String,
        @Path("user") user: String
    ): retrofit2.Call<GithubUserModel>

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
    @GET("/notifications?per_page=100")
    fun getNotifications(
        @Header("Authorization") user: String,
        @Query ("page") page: Int
    ): retrofit2.Call<ArrayList<NotificationModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/received_events?per_page=100")
    fun getEvents(
        @Header("Authorization") user: String,
        @Path("username") username:String,
        @Query("page") page:Int
    ): retrofit2.Call<ArrayList<EventsModel>>

    @Headers("Content-Type: application/json")
    @GET("/search/users?per_page=100")
    fun searchUser(
        @Header("Authorization") user: String,
        @Query("q") username:String
    ): retrofit2.Call<SearchModel>

    @Headers("Content-Type: application/json")
    @GET("/user/repos?sort=updated&per_page=100")
    fun getMyRepos(
        @Header("Authorization") user: String
    ): retrofit2.Call<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/repos?sort=updated&per_page=100")
    fun getUserRepos(
        @Header("Authorization") user: String,
        @Path("username") username:String
    ): retrofit2.Call<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/user/repos?sort=updated&per_page=5")
    fun topRepos(
        @Header("Authorization") user: String
    ): retrofit2.Call<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/repos?sort=updated&per_page=5")
    fun topReposUser(
        @Header("Authorization") user: String,
        @Path("username") username:String
    ): retrofit2.Call<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/following?per_page=100")
    fun getFollowing(
        @Header("Authorization") token: String,
        @Path("username") username:String,
        @Query("page") page:Int
    ): retrofit2.Call<ArrayList<FollowerModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/followers?per_page=100")
    fun getFollowers(
        @Header("Authorization") token: String,
        @Path("username") username:String,
        @Query("page") page:Int
    ): retrofit2.Call<ArrayList<FollowerModel>>


}