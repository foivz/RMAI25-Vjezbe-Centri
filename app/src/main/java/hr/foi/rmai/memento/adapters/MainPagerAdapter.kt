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

    val fragmentItems = listOf(
        FragmentItem(
            R.string.tasks_pending, R.drawable.baseline_assignment_late_24, PendingFragment::class
        ),
        FragmentItem(
            R.string.tasks_completed, R.drawable.baseline_assignment_turned_in_24, CompletedFragment::class
        ),
        FragmentItem(
            R.string.news, R.drawable.baseline_assignment_24, NewsFragment::class
        ),
    )

    override fun getItemCount(): Int = fragmentItems.size

    override fun createFragment(position: Int): Fragment {
        return fragmentItems[position].fragmentClass.java.newInstance() as Fragment
    }
}