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

package project.dheeraj.githubvisualizer.ViewModel

import GithubUserModel
import RepositoryModel
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.dheeraj.githubvisualizer.Network.GithubApiInterface
import project.dheeraj.githubvisualizer.Repository.NetworkRepository

class ProfileViewModel : ViewModel() {

    private val repository = NetworkRepository(GithubApiInterface())

    private var mutableStarList = MutableLiveData<ArrayList<RepositoryModel>>()
    val starList: LiveData<ArrayList<RepositoryModel>>

    private var mutableUserList = MutableLiveData<GithubUserModel>()
    val userList: LiveData<GithubUserModel>

    private var mutableTopRepositoryList = MutableLiveData<ArrayList<RepositoryModel>>()
    val topRepositoryList: LiveData<ArrayList<RepositoryModel>>

    private var mutableFollowData = MutableLiveData<Int>()
    val followData: LiveData<Int>

    init {
        userList = mutableUserList
        topRepositoryList = mutableTopRepositoryList
        followData = mutableFollowData
        starList = mutableStarList
    }

    fun getLoginProfile (token: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                mutableUserList.postValue(
                    repository.getLoginProfile(token)
                )
            }
            catch (e: Exception) {
//                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                Log.e("Get Login Profile", e.message)
            }
        }
    }

    fun getUserProfile (token: String, username: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                mutableUserList.postValue(
                    repository.getUserProfile(
                        token,
                        username
                    )
                )
            }
            catch (e: Exception) {
                Log.e("get User Profile", e.message)
            }
        }
    }


    fun getMyTopRepositories (token: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                mutableTopRepositoryList.postValue(
                    repository.getMyTopRepositories(token)
                )
            }
            catch (e: Exception) {
                Log.e("Get My Repo", e.message)
            }
        }
    }

    fun getUserTopRepositories (token: String, username: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                mutableTopRepositoryList.postValue(
                    repository.getUserTopRepositories(token, username)
                )
            }
            catch (e: Exception) {
                Log.e("Get User top repo", e.message)
            }
        }
    }

    fun getMyStarredRepo (token: String, page: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                mutableStarList.postValue(
                    repository.getMyStarredRepositories(token, page)
                )
            }
            catch (e: Exception) {
                Log.e("Get my Starred Repo", e.message)
            }
        }
    }

    fun getUserStarredRepo (token: String, username: String, page: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                mutableStarList.postValue(
                    repository.getOtherStarredRepositories(token, username, page)
                )
            }
            catch (e: Exception) {
                Log.e("Get User Starred", e.message)
            }
        }
    }

    fun getFollow (token: String, username:String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                mutableFollowData.postValue(
                    repository.getFollow(token, username)
                )
            }
            catch (e: Exception) {
                Log.e("Get Follow", e.message)
            }
        }
    }

    fun putFollow (token: String, username:String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                mutableFollowData.postValue(
                    repository.putFollow(token, username)
                )
            }
            catch (e: Exception) {
                Log.e("Put Follow", e.message)
            }
        }
    }

    fun deleteFollow (token: String, username:String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                mutableFollowData.postValue(
                    repository.deleteFollow(token, username)
                )
            }
            catch (e: Exception) {
                Log.e("Delete Follow", e.message)
            }
        }
    }


}
