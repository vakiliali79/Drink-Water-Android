package com.ava.alivakili.drink_water.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ava.alivakili.drink_water.R
import com.ava.alivakili.drink_water.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import com.ava.alivakili.drink_water.goal.GoalFragment
import com.ava.alivakili.drink_water.goal.GoalViewModel
import com.ava.alivakili.drink_water.history.HistoryFragment
import com.ava.alivakili.drink_water.repository.datastore.WaterAmountRepository
import com.ava.alivakili.drink_water.tracking.TrackingFragment

class MainActivity : AppCompatActivity() {
    protected lateinit var repository: WaterAmountRepository
    private val viewModel: GoalViewModel by viewModels{
        GoalViewModel.factory(repository)
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCurrFragment(GoalFragment())
        getData()
        properBottomNavigationBar()
    }

    private fun getData() {
        repository = WaterAmountRepository(this)
        lifecycleScope.launch {
            val repository= WaterAmountRepository(this@MainActivity)
            val data = repository.get()
            if (data != null) {

            }else{
                val menuItemId1 = R.id.history
                val menuItemId2 = R.id.tracking
                setMenuItemEnabled(binding.bottomNavigationView,menuItemId1,false)
                setMenuItemEnabled(binding.bottomNavigationView,menuItemId2,false)
            }
        }


    }

    private fun setMenuItemEnabled(bottomNavigationView: BottomNavigationView, menuItemId: Int, enabled: Boolean) {
        val menu = bottomNavigationView.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            if (menuItem.itemId == menuItemId) {
                menuItem.isEnabled = enabled
                break
            }
        }
    }

    private fun properBottomNavigationBar() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.goal -> {
                    setCurrFragment(GoalFragment())
                    true
                }
                R.id.tracking -> {
                    setCurrFragment(TrackingFragment())
                    true
                }
                R.id.history -> {
                    setCurrFragment(HistoryFragment())
                    true
                }

                else -> {
                    Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }

    }






    private fun setCurrFragment(fragment : Fragment){

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,fragment)
            commit()
        }

    }


}