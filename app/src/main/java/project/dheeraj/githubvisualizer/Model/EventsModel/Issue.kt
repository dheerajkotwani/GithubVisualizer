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

import project.dheeraj.githubvisualizer.Model.EventsModel.Pull_request

data class Issue (

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
//	val assignee : String,
//	val assignees : List<String>,
//	val milestone : String,
	val comments : Int,
	val created_at : String,
	val updated_at : String,
	val closed_at : String,
	val author_association : String,
	val pull_request : Pull_request,
	val body : String
)