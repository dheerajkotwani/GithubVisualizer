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

package project.dheeraj.githubvisualizer.Repository

import project.dheeraj.githubvisualizer.Network.GithubApiInterface
import project.dheeraj.githubvisualizer.Network.SafeApiRequest

class NetworkRepository(
    private val api: GithubApiInterface
) : SafeApiRequest() {

    suspend fun getNotification (token:String, page:Int) = apiRequest { api.getNotification(token, page) }

    suspend fun getFeeds (token:String, username: String, page:Int) = apiRequest { api.getEvents(token, username, page) }

    suspend fun getLoginProfile (token:String) = apiRequest { api.getUserInfo(token) }

    suspend fun getMyTopRepositories (token:String) = apiRequest { api.myTopRepositories(token) }

    suspend fun getMyRepositories (token:String) = apiRequest { api.getMyRepositories(token) }

    suspend fun getOtherRepositories (token:String, username: String) = apiRequest { api.getUserRepositories(token, username) }

    suspend fun getMyStarredRepositories (token:String, page: Int) = apiRequest { api.starredRepositoryLogin(token, page) }

    suspend fun getOtherStarredRepositories (token:String, username: String, page: Int) = apiRequest { api.starredRepositoryUser(token, username, page) }

    suspend fun getSearchUser(token:String, username: String) = apiRequest { api.searchUser(token, username) }

    suspend fun getSearchRepo(token:String, username: String) = apiRequest { api.searchRepo(token, username) }

    suspend fun getFollowers(token:String, username: String, page: Int) = apiRequest { api.getFollowers(token, username, page) }

    suspend fun getFollowing(token:String, username: String, page: Int) = apiRequest { api.getFollowing(token, username, page) }

}