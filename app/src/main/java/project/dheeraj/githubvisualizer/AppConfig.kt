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
