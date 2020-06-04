package project.dheeraj.githubvisualizer

import java.util.*

object AppConfig {
    const val GITHUB_BASE_URL = "https://github.com/"
    const val GITHUB_API_BASE_URL = "https://api.github.com/"
    const val GITHUB_CONTENT_BASE_URL = "https://raw.githubusercontent.com/"
    /**
     * This link are for OpenHub only. Please do not use this endpoint in your applications.
     * If you want to get trending repositories, you may stand up your own instance.
     * https://github.com/thedillonb/GitHub-Trending
     */
    const val HTTP_TIME_OUT = 32 * 1000
    const val HTTP_MAX_CACHE_SIZE = 32 * 1024 * 1024
    const val IMAGE_MAX_CACHE_SIZE = 32 * 1024 * 1024
    const val CACHE_MAX_AGE = 4 * 7 * 24 * 60 * 60
    const val CLIENT_ID: String = "e5f4203ad416624865dd"
    const val CLIENT_SECRET: String = "68c29a037c13960d75e6c01ca0234e6f37ef8375"
    const val OAUTH2_SCOPE = "user,repo,gist,notifications"
    const val OAUTH2_URL = GITHUB_BASE_URL + "login/oauth/authorize"
    const val REDIRECT_URL = "https://github-visualizer.firebaseapp.com/__/auth/handler";
    const val AUTH_CODE = "project.dheeraj.githubvisualizer.AUTHORIZE"
    const val SHARED_PREF = "project.dheeraj.githubvisualizer.SHARED_PREF"
    const val ACCESS_TOKEN = "project.dheeraj.githubvisualizer.ACCESS_TOKEN"

}
