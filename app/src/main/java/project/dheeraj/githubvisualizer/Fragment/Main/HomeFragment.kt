package project.dheeraj.githubvisualizer.Fragment.Main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import kotlinx.android.synthetic.main.fragment_home.view.*
import project.dheeraj.githubvisualizer.Activity.RepositoriesActivity
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.AppConfig.LOGIN
import project.dheeraj.githubvisualizer.AppConfig.NAME
import project.dheeraj.githubvisualizer.AppConfig.SHARED_PREF

import project.dheeraj.githubvisualizer.R

class HomeFragment : Fragment(), FragmentLifecycle {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        sharedPreferences = context!!.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        view.pageDisplayName.text = sharedPreferences.getString(NAME, "User")

        // TODO
        view.cardIssues.setOnClickListener {
            DynamicToast.makeWarning(context!!, "Developing").show()
        }

        // TODO
        view.cardPullRequest.setOnClickListener {
            DynamicToast.makeWarning(context!!, "Developing").show()
        }

        // TODO
        view.cardRepo.setOnClickListener {

//            DynamicToast.makeWarning(context!!, "Developing").show()
            val intent = Intent(context, RepositoriesActivity::class.java)
            intent.putExtra(AppConfig.LOGIN, sharedPreferences.getString(LOGIN, "User"))
            intent.putExtra("USER_TYPE", "me")
            startActivity(intent)

        }

        // TODO
        view.cardOrganizations.setOnClickListener {
            DynamicToast.makeWarning(context!!, "Developing").show()
        }

        // TODO
        view.appInfoIcon.setOnClickListener {
            DynamicToast.makeWarning(context!!, "Developing").show()
        }

        return view

    }

    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
    }


}
