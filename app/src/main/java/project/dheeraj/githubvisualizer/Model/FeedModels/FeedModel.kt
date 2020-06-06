package project.dheeraj.githubvisualizer.Model.FeedModels

import _links

class FeedModel (

	val timeline_url : String,
	val user_url : String,
	val current_user_public_url : String,
	val current_user_url : String,
	val current_user_actor_url : String,
	val current_user_organization_url : String,
	val current_user_organization_urls : List<String>,
	val security_advisories_url : String,
	val _links : _links
)