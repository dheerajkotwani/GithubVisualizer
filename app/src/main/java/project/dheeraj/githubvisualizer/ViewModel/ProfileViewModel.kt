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

class ProfileViewModel : ViewModel() {

    private val repository = NetworkRepository(GithubApiInterface())

    private var mutableUserList = MutableLiveData<GithubUserModel>()
    val userList: LiveData<GithubUserModel>

    private var mutableTopRepositoryList = MutableLiveData<ArrayList<RepositoryModel>>()
    val topRepositoryList: LiveData<ArrayList<RepositoryModel>>

    init {
        userList = mutableUserList
        topRepositoryList = mutableTopRepositoryList
    }

    fun getLoginProfile (token: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableUserList.postValue(
                repository.getLoginProfile(token)
            )
        }
    }


    fun getTopRepositories (token: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableTopRepositoryList.postValue(
                repository.getMyTopRepositories(token)
            )
        }
    }

}
