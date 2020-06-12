/*
 * MIT License
 *
 * Copyright (c) 2020 Dheeraj Kotwani
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package project.dheeraj.githubvisualizer.Network

import EventsModel
import FollowerModel
import GistModel
import project.dheeraj.githubvisualizer.Model.FeedModels.FeedModel
import GithubUserModel
import IssuesModel
import NotificationModel
import OrganizationsModel
import Readme
import RepositoryModel
import SearchModel
import StarredModel
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.Model.RepositoryModel.RepositoryContentModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.function.BinaryOperator
import kotlin.collections.ArrayList

interface GithubApiInterface {

    companion object{
        operator fun invoke(): GithubApiInterface {
            return Retrofit.Builder()
                .baseUrl(AppConfig.GITHUB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GithubApiClient.gson))
                .build()
                .create(GithubApiInterface::class.java)
        }
    }


    // Other User
    @Headers("Content-Type: application/json")
    @GET("users/{user}")
    suspend fun getPublicUser(
        @Header("Authorization") token: String,
        @Path("user") user: String
    ): Response<GithubUserModel>


    // UserLogin
    @Headers("Content-Type: application/json")
    @GET("/user")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): Response<GithubUserModel>

    @Headers("Content-Type: application/json")
    @GET("/user")
    fun getUserData(
        @Header("Authorization") token: String
    ): Call<GithubUserModel>

    // Notifications
    @Headers("Content-Type: application/json")
    @GET("/notifications?per_page=100")
    suspend fun getNotification(
        @Header("Authorization") token: String,
        @Query ("page") page: Int
    ): Response<ArrayList<NotificationModel>>


    // Feeds
    @Headers("Content-Type: application/json")
    @GET("/users/{username}/received_events?per_page=100")
    suspend fun getEvents(
        @Header("Authorization") user: String,
        @Path("username") username:String,
        @Query("page") page:Int
    ): Response<ArrayList<EventsModel>>

    @Headers("Content-Type: application/json")
    @GET("/repos/{owner}/{repo}/events?per_page=100")
    suspend fun getRepoEvents(
        @Header("Authorization") user: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String,
        @Query("page") page:Int
    ): Response<ArrayList<EventsModel>>

    // Search
    @Headers("Content-Type: application/json")
    @GET("/search/users?per_page=100")
    suspend fun searchUser(
        @Header("Authorization") user: String,
        @Query("q") username:String
    ): Response<SearchModel>

    @Headers("Content-Type: application/json")
    @GET("/search/users?per_page=100")
    suspend fun searchRepo(
        @Header("Authorization") user: String,
        @Query("q") username:String
    ): Response<SearchModel>


    // Repositories
    @Headers("Content-Type: application/json")
    @GET("/user/repos?sort=updated&per_page=100")
    suspend fun getMyRepositories(
        @Header("Authorization") user: String,
        @Query("page") page: Int
    ): Response<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/repos?sort=updated&per_page=100")
    suspend fun getUserRepositories(
        @Header("Authorization") user: String,
        @Path("username") username:String,
        @Query("page") page: Int
    ): Response<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/user/repos?sort=updated&per_page=5")
    suspend fun myTopRepositories(
        @Header("Authorization") user: String
    ): Response<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/repos?sort=updated&per_page=5")
    suspend fun topReposUser(
        @Header("Authorization") user: String,
        @Path("username") username:String
    ): Response<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/repos?sort=updated&per_page=5")
    suspend fun topRepositoriesUser(
        @Header("Authorization") user: String,
        @Path("username") username:String
    ): Response<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/repos/{owner}/{repo}")
    suspend fun getReposData(
        @Header("Authorization") token: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): Response<RepositoryModel>

    @Headers("Content-Type: application/json")
    @GET("/repos/{owner}/{repo}/contents/{path}")
    suspend fun getReposContent(
        @Header("Authorization") token: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String,
        @Path("path") path:String
    ): Response<ArrayList<RepositoryContentModel>>


    @Headers("Content-Type: application/json")
    @GET("/repos/{owner}/{repo}/readme")
    suspend fun getReposReadme(
        @Header("Authorization") user: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): Response<Readme>


    // Gists
    @Headers("Content-Type: application/json")
    @GET("/users/{username}/gists?per_page=100")
    suspend fun getGist(
        @Header("Authorization") user: String,
        @Path("username") username:String
    ): Response<ArrayList<GistModel>>

    // Organizations
    @Headers("Content-Type: application/json")
    @GET("/users/{username}/orgs")
    suspend fun getOrgs(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Response<ArrayList<OrganizationsModel>>

    // Issues
//    @Headers("performed_via_github_app")
    @Headers("Content-Type: application/json")
    @GET("/issues?state=all")
    suspend fun getIssues(
        @Header("Authorization") user: String,
        @Header("Accept") accept: String,
        @Query("filter") filter: String,
        @Query("per_page") per_page: Int
    ): Response<ArrayList<IssuesModel>>


    // Stars
    @Headers("Content-Type: application/json")
    @GET("/user/starred?per_page=100")
    fun starredRepo(
        @Header("Authorization") user: String,
        @Query ("page") page:Int
    ): retrofit2.Call<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/user/starred?per_page=100")
    suspend fun starredRepositoryLogin(
        @Header("Authorization") user: String,
        @Query ("page") page:Int
    ): Response<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/starred?per_page=100}")
    fun starredRepoOfUser(
        @Header("Authorization") user: String,
        @Path("username") username:String,
        @Query("page") page:Int
    ): retrofit2.Call<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/starred?per_page=100}")
    suspend fun starredRepositoryUser(
        @Header("Authorization") user: String,
        @Path("username") username:String,
        @Query("page") page:Int
    ): Response<ArrayList<RepositoryModel>>

    @Headers("Content-Length: 0")
    @PUT("/user/starred/{owner}/{repo}")
    suspend fun starTheRepo(
        @Header("Authorization") user: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): Response<Int>

    @Headers("Content-Length: 0")
    @GET("/user/starred/{owner}/{repo}")
    suspend fun getStar(
        @Header("Authorization") user: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): Response<Int>

    @Headers("Content-Length: 0")
    @DELETE("/user/starred/{owner}/{repo}")
    suspend fun removeStar(
        @Header("Authorization") user: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): Response<Int>


    // Followers and Following
    @Headers("Content-Type: application/json")
    @GET("/users/{username}/following?per_page=100")
    suspend fun getFollowing(
        @Header("Authorization") token: String,
        @Path("username") username:String,
        @Query("page") page:Int
    ): Response<ArrayList<FollowerModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/followers?per_page=100")
    suspend fun getFollowers(
        @Header("Authorization") token: String,
        @Path("username") username:String,
        @Query("page") page:Int
    ): Response<ArrayList<FollowerModel>>

    @Headers("Content-Length: 0")
    @PUT("/user/following/{username}")
    suspend fun followUser(
        @Header("Authorization") token: String,
        @Path("username") username:String
    ): Response<Int>

    @Headers("Content-Length: 0")
    @GET("/user/following/{username}")
    suspend fun checkFollow(
        @Header("Authorization") token: String,
        @Path("username") username:String
    ): Response<Int>

    @Headers("Content-Length: 0")
    @DELETE("/user/following/{username}")
    suspend fun unfollowUser(
        @Header("Authorization") token: String,
        @Path("username") username:String
    ): Response<String>



}