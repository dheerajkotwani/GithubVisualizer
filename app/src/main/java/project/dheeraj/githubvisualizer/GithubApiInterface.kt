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

package project.dheeraj.githubvisualizer

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
import retrofit2.http.*
import kotlin.collections.ArrayList

interface GithubApiInterface {


    // Other User
    @Headers("Content-Type: application/json")
    @GET("users/{user}")
    fun getPublicUser(
        @Header("Authorization") token: String,
        @Path("user") user: String
    ): retrofit2.Call<GithubUserModel>


    // UserLogin
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


    // Notifications
  @Headers("Content-Type: application/json")
    @GET("/notifications?per_page=100")
    fun getNotifications(
        @Header("Authorization") user: String,
        @Query ("page") page: Int
    ): retrofit2.Call<ArrayList<NotificationModel>>


    // Feeds
    @Headers("Content-Type: application/json")
    @GET("/users/{username}/received_events?per_page=100")
    fun getEvents(
        @Header("Authorization") user: String,
        @Path("username") username:String,
        @Query("page") page:Int
    ): retrofit2.Call<ArrayList<EventsModel>>


    // Search
    @Headers("Content-Type: application/json")
    @GET("/search/users?per_page=100")
    fun searchUser(
        @Header("Authorization") user: String,
        @Query("q") username:String
    ): retrofit2.Call<SearchModel>


    // Repositories
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
    @GET("/repos/{owner}/{repo}")
    fun getReposData(
        @Header("Authorization") user: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): retrofit2.Call<RepositoryModel>

    @Headers("Content-Type: application/json")
    @GET("/repos/{owner}/{repo}/readme")
    fun getReposReadme(
        @Header("Authorization") user: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): retrofit2.Call<Readme>


    // Gists
    @Headers("Content-Type: application/json")
    @GET("/users/{username}/gists?per_page=100")
    fun getGist(
        @Header("Authorization") user: String,
        @Path("username") username:String
    ): retrofit2.Call<ArrayList<GistModel>>

    // Organizations
    @Headers("Content-Type: application/json")
    @GET("/users/{username}/orgs")
    fun getOrgs(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): retrofit2.Call<ArrayList<OrganizationsModel>>

    // Issues
    @Headers("Content-Type: application/json")
    @GET("/issues?filter=assigned&filter=subscribed&state=all")
    fun getIssues(
        @Header("Authorization") user: String
    ): retrofit2.Call<ArrayList<IssuesModel>>


    // Stars
    @Headers("Content-Type: application/json")
    @GET("/user/starred?per_page=100")
    fun starredRepo(
        @Header("Authorization") user: String
    ): retrofit2.Call<ArrayList<RepositoryModel>>

    @Headers("Content-Type: application/json")
    @GET("/users/{username}/starred?per_page=100")
    fun starredRepoOfUser(
        @Header("Authorization") user: String,
        @Path("username") username:String
    ): retrofit2.Call<ArrayList<RepositoryModel>>

    @Headers("Content-Length: 0")
    @PUT("/user/starred/{owner}/{repo}")
    fun starTheRepo(
        @Header("Authorization") user: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): retrofit2.Call<String>

    @Headers("Content-Length: 0")
    @GET("/user/starred/{owner}/{repo}")
    fun getStar(
        @Header("Authorization") user: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): retrofit2.Call<String>

    @Headers("Content-Length: 0")
    @DELETE("/user/starred/{owner}/{repo}")
    fun removeStar(
        @Header("Authorization") user: String,
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): retrofit2.Call<String>


    // Followers and Following
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

    @Headers("Content-Length: 0")
    @PUT("/user/following/{username}")
    fun followUser(
        @Header("Authorization") token: String,
        @Path("username") username:String
    ): retrofit2.Call<String>

    @Headers("Content-Length: 0")
    @GET("/user/following/{username}")
    fun checkFollow(
        @Header("Authorization") token: String,
        @Path("username") username:String
    ):retrofit2.Call<String>

    @Headers("Content-Length: 0")
    @DELETE("/user/following/{username}")
    fun unfollowUser(
        @Header("Authorization") token: String,
        @Path("username") username:String
    ):retrofit2.Call<String>



}