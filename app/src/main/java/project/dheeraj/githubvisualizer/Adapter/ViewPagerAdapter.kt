package project.dheeraj.githubvisualizer.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import project.dheeraj.githubvisualizer.Fragment.Main.FragmentLifecycle

class ViewPagerAdapter(fragmentManager: FragmentManager,
                       private var fragments: ArrayList<Fragment>): FragmentStatePagerAdapter(fragmentManager) {

    var oldPos = -1;

    override fun getItem(position: Int): Fragment {


        if (oldPos!=-1) {
//            var fragmentToShow: Fragment = fragments[position]
//            fragmentToShow.onResume()

            var fragmentToHide: Fragment = fragments[oldPos]
            fragmentToHide.onPause()
        }
        oldPos = position

        return fragments[position]

    }

    override fun getCount(): Int {
        return 5
    }

}