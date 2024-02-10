package com.ava.alivakili.drink_water.goal

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.launch
import com.ava.alivakili.drink_water.repository.datastore.WaterAmountRepository
import com.ava.alivakili.drink_water.R
import com.ava.alivakili.drink_water.databinding.GoalLayoutBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ava.alivakili.drink_water.main.MainActivity

class GoalFragment:Fragment() {
    private var _binding: GoalLayoutBinding? = null
    private val binding get() = _binding!!
    protected lateinit var repository: WaterAmountRepository
    private val viewModel:GoalViewModel by viewModels{
        GoalViewModel.factory(repository)
    }
    private var totalWater:Int?=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GoalLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repository = WaterAmountRepository(requireContext())
        lifecycleScope.launch {
            setTotalWater()
        }
        configureSeekbar()
        observeViewModel()
        configureToolbar()

    }

    private fun configureToolbar(){
        binding.toolbar.inflateMenu(R.menu.home_menu_item)
        binding.toolbar.setOnMenuItemClickListener{
            save()
            true
        }
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.state.collect{state->
                    when(state){
                        GoalState.ErrorSaving -> Toast.makeText(requireContext(), "Unable to save data!", Toast.LENGTH_SHORT).show()
                        GoalState.Idle -> {}
                        GoalState.SuccessSaving ->Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                        GoalState.ZeroAmount->Toast.makeText(requireContext(), "Amount can't be 0 ml", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private suspend fun setTotalWater() {
        val repository= WaterAmountRepository(requireContext())
        totalWater=repository.get()?.totalWater
        if (totalWater != null) {
            enableBottomNavigationBar()
            binding.seekbarValue.text="$totalWater ml"
            Log.e(TAG, "setTotalWater: "+totalWater )
            binding.seekBar.progress= totalWater!! /100
        }else
            binding.seekbarValue.text="0 ml"
    }

    private fun save() {
        val totalAmount= binding.seekBar.progress
        viewModel.save(totalAmount*100,0,0.0)
        enableBottomNavigationBar()
    }

    private fun enableBottomNavigationBar() {
        val mainActivity = requireActivity() as MainActivity
        val bottomNavigationView = mainActivity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val menuItemId1 = R.id.history
        val menuItemId2 = R.id.tracking
        setMenuItemEnabled(bottomNavigationView,menuItemId1,true)
        setMenuItemEnabled(bottomNavigationView,menuItemId2,true)
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

    private fun configureSeekbar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val seekBarValue = progress*100
                binding.seekbarValue.text = "$seekBarValue ml"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Called when the user starts interacting with the SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Called when the user stops interacting with the SeekBar
            }
        })
    }

}