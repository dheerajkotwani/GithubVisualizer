package project.dheeraj.githubvisualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    lateinit var apiInterface: GithubApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiInterface = RetrofitClient.getClient().create(GithubApiInterface::class.java)

        val call: Call<ProfileModel> = apiInterface.Check("dheerajkotwani")
        call.enqueue(object : Callback<ProfileModel>{
            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<ProfileModel>,
                response: Response<ProfileModel>
            ) {
                if (response.body()!=null) {
                    Toast.makeText(this@MainActivity, response.body()!!.name, Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }
}
