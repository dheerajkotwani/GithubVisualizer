
data class EventsModel (

	val id : Number,
	val type : String,
	val actor : Actor,
	val repo : Repo,
	val payload : Payload,
	val public : Boolean,
	val created_at : String
)