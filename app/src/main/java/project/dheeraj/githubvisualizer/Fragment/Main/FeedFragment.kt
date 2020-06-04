package project.dheeraj.githubvisualizer.Fragment.Main

import EventsModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import project.dheeraj.githubvisualizer.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FeedFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_feed, container, false)

        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)

        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);
        var call: Call<ArrayList<EventsModel>> =
            apiInterface.getEvents("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}",
                "dheerajkotwani")
        call.enqueue(object : Callback<ArrayList<EventsModel>> {
            override fun onFailure(call: Call<ArrayList<EventsModel>>, t: Throwable) {
                Toast.makeText(
                    context,
                    "error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<ArrayList<EventsModel>>, response: Response<ArrayList<EventsModel>>) {
                Toast.makeText(context, "feed: ${response.body()!!.size}", Toast.LENGTH_LONG).show()
            }
        })


        return view
    }


}
