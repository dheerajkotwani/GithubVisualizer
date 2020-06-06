import project.dheeraj.githubvisualizer.Model.EventsModel.Pull_request

data class Payload (

	val push_id : Number,
	val size : Number,
	val distinct_size : Number,
	val ref : String,
	val ref_type : String,
	val description: String,
	val head : String,
	val before : String,
	val commits : List<Commits>,
	val action : String,
	val number : Int,
	val pull_request : Pull_request,
	val issue : Issue,
	val comment : Comment,
	val forkee : Forkee,
	val member : project.dheeraj.githubvisualizer.Model.EventsModel.Member

)