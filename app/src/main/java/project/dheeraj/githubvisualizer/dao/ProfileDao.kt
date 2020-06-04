package project.dheeraj.githubvisualizer.dao

import GithubUserModel
import androidx.room.*
import retrofit2.http.DELETE

@Dao
abstract class ProfileDao {

    @Insert
    abstract fun insert(githubUserModel: GithubUserModel)

    @Update
    abstract fun update(githubUserModel: GithubUserModel)

    @Delete
    abstract fun delete(githubUserModel: GithubUserModel)

}