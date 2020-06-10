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

import NotificationModel
import SearchModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.dheeraj.githubvisualizer.Network.GithubApiInterface
import project.dheeraj.githubvisualizer.Repository.NetworkRepository

class SearchViewModel: ViewModel() {

    private val repository = NetworkRepository(GithubApiInterface())

    private var mutableSearchUserList = MutableLiveData<SearchModel>()
    private var mutableSearchRepoList = MutableLiveData<SearchModel>()

    val searchUserList: LiveData<SearchModel>
    val searchRepoList: LiveData<SearchModel>

    init {
        searchUserList = mutableSearchUserList
        searchRepoList = mutableSearchRepoList
    }

    fun getSearchUser (token: String, username: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableSearchUserList.postValue(repository.getSearchUser(
                token,
                username
            ))
        }
    }

    fun getSearchRepo (token: String, username: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableSearchUserList.postValue(repository.getSearchRepo(
                token,
                username
            ))
        }
    }


}
