package project.dheeraj.githubvisualizer.Fragment.Main

import NotificationModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import project.dheeraj.githubvisualizer.Adapter.NotificationsAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.GithubApiClient
import project.dheeraj.githubvisualizer.GithubApiInterface

import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var notificationReclerView: RecyclerView
    private lateinit var notificationsAdapter: NotificationsAdapter
    private lateinit var notificatins: ArrayList<NotificationModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_notification, container, false)

        notificationReclerView = view.findViewById(R.id.notification_recyclerView)
        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        notificatins = ArrayList()

        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);
        var call: Call<ArrayList<NotificationModel>> =
            apiInterface.getNotifications("token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}")
        call.enqueue(object : Callback<ArrayList<NotificationModel>> {
            override fun onFailure(call: Call<ArrayList<NotificationModel>>, t: Throwable) {
                Toast.makeText(
                    context,
                    "error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<ArrayList<NotificationModel>>, response: Response<ArrayList<NotificationModel>>) {
                Toast.makeText(context, "feed: ${response.body()!![0].subject}", Toast.LENGTH_LONG).show()
                notificatins = response.body()!!

                notificationsAdapter = NotificationsAdapter(context!!, notificatins)
                notificationReclerView.adapter = notificationsAdapter
                notificationsAdapter.notifyDataSetChanged()

            }
        })

        return view
    }


}
