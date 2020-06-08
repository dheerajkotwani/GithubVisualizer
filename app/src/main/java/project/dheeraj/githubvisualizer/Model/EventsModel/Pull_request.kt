/*
 * MIT License
 *
 * Copyright (c) 2020 Dheeraj Kotwani
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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