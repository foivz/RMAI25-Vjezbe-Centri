package hr.foi.rmai.memento.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.fragments.CompletedFragment
import hr.foi.rmai.memento.fragments.NewsFragment
import hr.foi.rmai.memento.fragments.PendingFragment

class MainPagerAdapter(fragmentManager : FragmentManager, lifecycle : Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    val titleList = listOf(R.string.tasks_pending, R.string.tasks_completed, R.string.news)
    val iconList = listOf(
        R.drawable.baseline_assignment_late_24,
        R.drawable.baseline_assignment_turned_in_24,
        R.drawable.baseline_assignment_24
    )

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PendingFragment()
            1 -> CompletedFragment()
            else -> NewsFragment()
        }
    }
}