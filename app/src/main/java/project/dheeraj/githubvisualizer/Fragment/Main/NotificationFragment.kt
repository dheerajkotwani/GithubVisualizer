package project.dheeraj.githubvisualizer.Fragment.Main

import NotificationModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_notification.*
import project.dheeraj.githubvisualizer.Adapter.NotificationsAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.GithubApiClient
import project.dheeraj.githubvisualizer.GithubApiInterface

import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class NotificationFragment : Fragment(), FragmentLifecycle {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var notificationReclerView: RecyclerView
    private lateinit var notificationsAdapter: NotificationsAdapter
    private lateinit var notificatins: ArrayList<NotificationModel>
    private var page = 1
    private lateinit var notificationsProgressBar: ProgressBar

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
        notificationsProgressBar = view.findViewById(R.id.NotificationsProgressBar)
        sharedPref = context!!.getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        notificatins = ArrayList()

        notificationsAdapter = NotificationsAdapter(context!!, notificatins)
        notificationReclerView.adapter = notificationsAdapter

        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);

        page = 1
        fetchFeeds(apiInterface)

        notificationReclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)
                    && (notificationsProgressBar.visibility==View.GONE)) {
//                    Toast.makeText(context, "Last", Toast.LENGTH_LONG).show()
                    page++
                    fetchFeeds(apiInterface)
                }
            }
        })

        return view
    }

    private fun fetchFeeds(apiInterface: GithubApiInterface) {

        notificationsProgressBar.visibility = View.VISIBLE

        var call: Call<ArrayList<NotificationModel>> =
            apiInterface.getNotifications(
                "token ${sharedPref.getString(
                    AppConfig.ACCESS_TOKEN,
                    ""
                )}", page
            )
        try {
            call.enqueue(object : Callback<ArrayList<NotificationModel>> {
                override fun onFailure(call: Call<ArrayList<NotificationModel>>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    notificationsProgressBar.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<ArrayList<NotificationModel>>,
                    response: Response<ArrayList<NotificationModel>>
                ) {
                    try {

                        for (i in response.body()!!) {
                            notificatins.add(i)
                        }

                        notificationsAdapter.notifyDataSetChanged()
                        notificationsProgressBar.visibility = View.GONE
                    } catch (e: Exception) {
                        Timber.e(e)
                        notificationsProgressBar.visibility = View.GONE
                    }

                }
            })
        } catch (e: Exception) {
            Timber.e(e)
            notificationsProgressBar.visibility = View.GONE
        }
    }

    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
    }


}
