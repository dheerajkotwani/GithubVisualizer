package project.dheeraj.githubvisualizer

object GithubModel {
    data class Profile (val login: String,
                                 val avatar_url: String,
                                 val url: String,
                                 val name: String,
                                 val bio: String,
                                 val email: String,
                                 val followers: String,
                                 val following: String,
                                 val location: String)
}