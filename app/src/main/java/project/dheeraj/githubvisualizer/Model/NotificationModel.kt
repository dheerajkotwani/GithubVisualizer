class NotificationModel (

	val id : Int,
	val repository : Repository,
	val subject : Subject,
	val reason : String,
	val unread : Boolean,
	val updated_at : String,
	val last_read_at : String,
	val url : String
)

class Subject (

	val title : String,
	val url : String,
	val latest_comment_url : String,
	val type : String
)

class Repository (

	val id : Int,
	val node_id : String,
	val name : String,
	val full_name : String,
	val owner : Owner,
	val private : Boolean,
	val html_url : String,
	val description : String,
	val fork : Boolean,
	val url : String,
	val archive_url : String,
	val assignees_url : String,
	val blobs_url : String,
	val branches_url : String,
	val collaborators_url : String,
	val comments_url : String,
	val commits_url : String,
	val compare_url : String,
	val contents_url : String,
	val contributors_url : String,
	val deployments_url : String,
	val downloads_url : String,
	val events_url : String,
	val forks_url : String,
	val git_commits_url : String,
	val git_refs_url : String,
	val git_tags_url : String,
	val git_url : String,
	val issue_comment_url : String,
	val issue_events_url : String,
	val issues_url : String,
	val keys_urls_url : String,
	val labels_url : String,
	val languages_url : String,
	val merges_url : String,
	val milestones_url : String,
	val notifications_url : String,
	val pulls_url : String,
	val releases_url : String,
	val ssh_url : String,
	val stargazers_url : String,
	val statuses_url : String,
	val subscribers_url : String,
	val subscription_url : String,
	val tags_url : String,
	val teams_url : String,
	val trees_url : String
)