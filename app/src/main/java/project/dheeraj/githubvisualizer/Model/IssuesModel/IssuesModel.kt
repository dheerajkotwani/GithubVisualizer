import project.dheeraj.githubvisualizer.Model.EventsModel.Pull_request

data class IssuesModel (

	val url : String,
	val repository_url : String,
	val labels_url : String,
	val comments_url : String,
	val events_url : String,
	val html_url : String,
	val id : Int,
	val node_id : String,
	val number : Int,
	val title : String,
	val user : User,
	val labels : List<Labels>,
	val state : String,
	val locked : Boolean,
	val assignee : Assignee,
	val assignees : List<Assignees>,
	val milestone : String,
	val comments : Int,
	val created_at : String,
	val updated_at : String,
	val closed_at : String,
	val author_association : String,
	val repository : Repository,
	val pull_request : Pull_request,
	val body : String
)