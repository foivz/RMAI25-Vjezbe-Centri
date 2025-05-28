package hr.foi.rmai.memento

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.rmai.memento.adapters.MainPagerAdapter
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.helpers.MockDataLoader

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var navView: NavigationView
    lateinit var navDrawerLayout: DrawerLayout
    lateinit var onSharedPreferencesChangeListener: OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupQualityOfLifeImprovements()

        TasksDatabase.buildInstance(this)
        MockDataLoader.loadMockData()

        setupTabNavigation()

        val channel = NotificationChannel ("task-timer", "Task Timer Channel",
            NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        PreferenceManager.getDefaultSharedPreferences(this)?.let { pref ->
            PreferencesActivity.switchDarkMode(pref.getBoolean("preference_dark_mode", false))
        }
    }

    private fun setupTabNavigation() {
        tabLayout = findViewById(R.id.tabs)
        viewPager2 = findViewById(R.id.viewpager)
        navView = findViewById(R.id.nav_view)
        navDrawerLayout = findViewById(R.id.nav_drawer_layout)

        val mainPagerAdapter = MainPagerAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = mainPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(mainPagerAdapter.fragmentItems[position].titleRes)
            tab.setIcon(mainPagerAdapter.fragmentItems[position].iconRes)
        }.attach()

        setupNavigationDrawer(mainPagerAdapter)

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                navView.menu.getItem(position).isChecked = true
            }
        })
    }

    private fun setupNavigationDrawer(viewPagerAdapter: MainPagerAdapter) {
        viewPagerAdapter.fragmentItems.withIndex().forEach { (index, fragmentItem) ->
            navView.menu
                .add(fragmentItem.titleRes)
                .setIcon(fragmentItem.iconRes)
                .setCheckable(true)
                .setChecked(index == 0)
                .setOnMenuItemClickListener {
                    viewPager2.setCurrentItem(index, true)
                    navDrawerLayout.closeDrawers()

                    return@setOnMenuItemClickListener true
                }
        }

        val tasksCounterItem = navView.menu.add(2, 0, 0, "")
        attachMenuItemToTasksCreatedCount(tasksCounterItem)

        navView.menu
            .add(3, 0, 0, getString(R.string.settings_menu_item))
            .setIcon(R.drawable.baseline_app_settings_alt_24)
            .setOnMenuItemClickListener {
                val intent = Intent(this, PreferencesActivity::class.java)
                startActivity(intent)
                navDrawerLayout.closeDrawers()

                return@setOnMenuItemClickListener true
            }
    }

    private fun attachMenuItemToTasksCreatedCount(tasksCounterItem: MenuItem) {
        val sharedPreferences = getSharedPreferences("tasks_preferences", Context.MODE_PRIVATE)

        onSharedPreferencesChangeListener = OnSharedPreferenceChangeListener { _, key ->
            if (key == "tasks_created_counter") {
                updateTasksCreatedCounter(tasksCounterItem)
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferencesChangeListener)
        updateTasksCreatedCounter(tasksCounterItem)
    }

    private fun updateTasksCreatedCounter(tasksCounterItem: MenuItem) {
        val sharedPreferences = getSharedPreferences("tasks_preferences", Context.MODE_PRIVATE)

        tasksCounterItem.title = "Tasks created ${sharedPreferences.getInt("tasks_created_counter", 0)}"
    }

    private fun setupQualityOfLifeImprovements() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}