package project.dheeraj.githubvisualizer.Activity

import OrganizationsModel
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_organizations.*
import project.dheeraj.githubvisualizer.Adapter.OrganizationsAdapter
import project.dheeraj.githubvisualizer.AppConfig
import project.dheeraj.githubvisualizer.GithubApiClient
import project.dheeraj.githubvisualizer.GithubApiInterface
import project.dheeraj.githubvisualizer.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class OrganizationsActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var orgs: ArrayList<OrganizationsModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizations)

        sharedPref = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        orgs = ArrayList()
        var apiInterface =
            GithubApiClient.getClient().create(GithubApiInterface::class.java);
        val username = "${sharedPref.getString(AppConfig.LOGIN, "")}"

        buttonBack.setOnClickListener {
            onBackPressed()
        }

        getOrganizations(apiInterface, username)
    }

    private fun getOrganizations(apiInterface: GithubApiInterface, username:String) {

        val call = apiInterface.getOrgs(
            "token ${sharedPref.getString(AppConfig.ACCESS_TOKEN, "")}", username
        )

        try {

            call.enqueue(object : Callback<ArrayList<OrganizationsModel>> {
                override fun onFailure(call: Call<ArrayList<OrganizationsModel>>, t: Throwable) {
                    Timber.e(t)
                    if (noOrgFound.visibility == View.VISIBLE)
                        noOrgFound.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<ArrayList<OrganizationsModel>>,
                    response: Response<ArrayList<OrganizationsModel>>
                ) {

                    try {
                        orgs = response.body()!!
                        orgRecyclerView.adapter = OrganizationsAdapter(this@OrganizationsActivity, orgs)
                        if (orgProgressbar.visibility == View.VISIBLE)
                            orgProgressbar.visibility = View.GONE
                        if (noOrgFound.visibility == View.VISIBLE)
                            noOrgFound.visibility = View.GONE
                    } catch (e: Exception) {
                        Timber.e(e)
                        if (noOrgFound.visibility == View.VISIBLE)
                            noOrgFound.visibility = View.GONE
                    }

                }

            })

        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
