package project.dheeraj.githubvisualizer.Model.EventsModel

import Base
import Head
import Labels
import Links
import MergedBy
import RequestedReviewers
import User

data class Pull_request (

	val url : String,
	val id : Int,
	val node_id : String,
	val html_url : String,
	val diff_url : String,
	val patch_url : String,
	val issue_url : String,
	val number : Int,
	val state : String,
	val locked : Boolean,
	val title : String,
	val user : User,
	val body : String,
	val created_at : String,
	val updated_at : String,
	val closed_at : String,
	val merged_at : String,
	val merge_commit_sha : String,
	val assignee : String,
	val assignees : List<String>,
	val requested_reviewers : List<RequestedReviewers>,
//	val requested_teams : List<RequestedReviewers>,
	val labels : List<Labels>,
//	val milestone : String,
	val draft : Boolean,
	val commits_url : String,
	val review_comments_url : String,
	val review_comment_url : String,
	val comments_url : String,
	val statuses_url : String,
	val head : Head,
	val base : Base,
	val _links : Links,
	val author_association : String,
	val merged : Boolean,
	val mergeable : String,
	val rebaseable : String,
	val mergeable_state : String,
	val merged_by : MergedBy,
	val comments : Int,
	val review_comments : Int,
	val maintainer_can_modify : Boolean,
	val commits : Int,
	val additions : Int,
	val deletions : Int,
	val changed_files : Int
)