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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.dheeraj.githubvisualizer.Network.GithubApiInterface
import project.dheeraj.githubvisualizer.Repository.NetworkRepository

class DeveloperViewModel: ViewModel() {

    private val repository = NetworkRepository(GithubApiInterface())

    private var mutableRepoData = MutableLiveData<RepositoryModel>()
    private var mutableStarData = MutableLiveData<Int>()
    private var mutableUserList = MutableLiveData<GithubUserModel>()
    private var mutableFollowData = MutableLiveData<Int>()

    val followData: LiveData<Int>
    val userList: LiveData<GithubUserModel>
    val repoData: LiveData<RepositoryModel>
    val starData: LiveData<Int>

    init {
        repoData = mutableRepoData
        starData = mutableStarData
        userList = mutableUserList
        followData = mutableFollowData
    }

    fun getStar (token: String, username:String, repo: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableStarData.postValue(repository.getStar(
                token,
                username,
                repo
            ))
        }
    }


    fun putStar (token: String, username:String, repo: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableStarData.postValue(repository.putStar(
                token,
                username,
                repo
            ))
        }
    }

    fun removeStar (token: String, username:String, repo: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableStarData.postValue(repository.deleteStar(
                token,
                username,
                repo
            ))
        }
    }

//    fun repoDetails (token: String, username:String, repo: String) {
//        viewModelScope.launch(Dispatchers.Main) {
//            mutableRepoData.postValue(repository.getRepoDetails(
//                token,
//                username,
//                repo
//            ))
//        }
//    }


    fun getFollow (token: String, username:String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableFollowData.postValue(
                repository.getFollow(token, username)
            )
        }
    }

    fun putFollow (token: String, username:String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableFollowData.postValue(
                repository.putFollow(token, username)
            )
        }
    }

    fun deleteFollow (token: String, username:String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableFollowData.postValue(
                repository.deleteFollow(token, username)
            )
        }
    }

    fun getUserDetails (token: String, username:String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableUserList.postValue(
                repository.getUserProfile(token, username)
            )
        }
    }



}
