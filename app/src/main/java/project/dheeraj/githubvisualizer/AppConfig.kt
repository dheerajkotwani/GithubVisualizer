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

import java.util.*

object AppConfig {
    const val GITHUB_BASE_URL = "https://github.com/"
    const val GITHUB_API_BASE_URL = "https://api.github.com/"
    const val OAUTH2_SCOPE = "user,repo,gist,notifications,admin"
    const val OAUTH2_URL = GITHUB_BASE_URL + "login/oauth/authorize"
    const val REDIRECT_URL = "https://github-visualizer.firebaseapp.com/__/auth/handler";
    const val AUTH_CODE = "project.dheeraj.githubvisualizer.AUTHORIZE"
    const val SHARED_PREF = "project.dheeraj.githubvisualizer.SHARED_PREF"
    const val ACCESS_TOKEN = "project.dheeraj.githubvisualizer.ACCESS_TOKEN"
    const val LOGIN = "project.dheeraj.githubvisualizer.LOGIN"
    const val NAME = "project.dheeraj.githubvisualizer.NAME"

}
