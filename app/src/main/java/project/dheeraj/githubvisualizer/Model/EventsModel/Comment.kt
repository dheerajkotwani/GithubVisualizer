

data class Comment (

	val url : String,
	val html_url : String,
	val issue_url : String,
	val id : Int,
	val node_id : String,
	val user : User,
	val created_at : String,
	val updated_at : String,
	val author_association : String,
	val body : String
)