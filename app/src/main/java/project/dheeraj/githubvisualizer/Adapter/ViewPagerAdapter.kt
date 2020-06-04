package project.dheeraj.githubvisualizer.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import project.dheeraj.githubvisualizer.Fragment.Main.*
import project.dheeraj.githubvisualizer.Model.FragmentModel
import androidx.viewpager2.adapter.FragmentStateAdapter as FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager,
                       private var fragments: ArrayList<FragmentModel>): FragmentStatePagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        var fragmentHome = HomeFragment();
        var fragmentFeed = FeedFragment();
        var fragmentSearch = SearchFragment();
        var fragmentNotification = NotificationFragment();
        var fragmentProfile = ProfileFragment();

        when (position){

            0 -> return fragmentHome
            1 -> return fragmentSearch
            2 -> return fragmentFeed
            3 -> return fragmentNotification
            4 -> return fragmentProfile

        }
        return fragmentFeed
    }

    override fun getCount(): Int {
        return 5
    }

}