package project.dheeraj.githubvisualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.*

class MainActivity : AppCompatActivity() {


    lateinit var apiInterface: GithubApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiInterface = RetrofitClient.getClient().create(GithubApiInterface::class.java)

        val call: Call<GithubModel.Profile> = apiInterface.Check("dheerajkotwani")
        call.enqueue(object : Callback<GithubModel.Profile>{
            override fun onFailure(call: Call<GithubModel.Profile>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<GithubModel.Profile>,
                response: Response<GithubModel.Profile>
            ) {
                Toast.makeText(this@MainActivity, response.body()!!.name, Toast.LENGTH_LONG).show()
            }

        }
        )
    }
//    private fun beginSearch(searchString: String) {
//        disposable = apiInterface.Check(searchString)
//            .subscribe(
//                { result -> txt_search_result.text = "${result} result found" },
//                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
//            )
//    }
}
