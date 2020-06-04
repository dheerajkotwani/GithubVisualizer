package project.dheeraj.githubvisualizer.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager,
                       private var fragments: ArrayList<Fragment>): FragmentStatePagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {

        return fragments[position]

    }

    override fun getCount(): Int {
        return 5
    }

}