package com.ava.alivakili.drink_water.tracking

import android.app.AlertDialog
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ava.alivakili.drink_water.R
import kotlinx.coroutines.launch
import com.ava.alivakili.drink_water.WaterAmountModel
import com.ava.alivakili.drink_water.databinding.TrackingLayoutBinding
import com.ava.alivakili.drink_water.goal.GoalViewModel
import com.ava.alivakili.drink_water.repository.database.DatabaseInstance
import com.ava.alivakili.drink_water.repository.datastore.DataRestore
import com.ava.alivakili.drink_water.repository.datastore.WaterAmountRepository

class TrackingFragment : Fragment() {
    private var _binding: TrackingLayoutBinding? = null
    private val binding get() = _binding!!
    private val addTaskSheet = AddWaterAmountBottomSheet(::addWaterAmount)
    private val viewModel: TrackingViewModel by viewModels {
        TrackingViewModel.factory(
            dao = DatabaseInstance.instance(
                requireContext()
            ).taskDao()
        )
    }
    private var totalWater: Int = 0
    private var drinkedWater: Int = 0
    private var percentage: Double = 0.0
    private var amount: Int = 0
    protected lateinit var repository: WaterAmountRepository
    private val goalViewModel: GoalViewModel by viewModels {
        GoalViewModel.factory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TrackingLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repository = WaterAmountRepository(requireContext())
        getData()
        configureOnClickListeners()
        observeViewModel()
    }

    private fun getData() {
        var data: DataRestore? = null
        lifecycleScope.launch {
            data = getFromDataRestore()
            if (data != null) {

                drinkedWater = data!!.drinkedWater
                totalWater = data!!.totalWater
                percentage = data!!.percentage
                showWaterAmountLayout(null)
//            properLayout()
            } else {
                val errorMessage = "You did not set a water usage amount"
                showErrorDialog(errorMessage)
            }
        }
    }

    private fun configureOnClickListeners() {
        binding.amount100mlCV.setOnClickListener {
            amount = 100
            addWaterAmount(amount)
        }
        binding.amount150mlCV.setOnClickListener {
            amount = 150
            addWaterAmount(amount)
        }
        binding.amount200mlCV.setOnClickListener {
            amount = 200
            addWaterAmount(amount)
        }
        binding.amount250mlCV.setOnClickListener {
            amount = 250
            addWaterAmount(amount)
        }
        binding.floatingActionButton.setOnClickListener {
            addTaskSheet.show(parentFragmentManager, AddWaterAmountBottomSheet.TAG)
        }
    }

//    private fun properLayout(waterAmount:WaterAmountModel) {

//        if(totalWater!=0){
//            binding.apply {
//                floatingActionButton.visibility=View.VISIBLE
//                amount100mlCV.visibility=View.VISIBLE
//                amount150mlCV.visibility=View.VISIBLE
//                amount200mlCV.visibility=View.VISIBLE
//                amount250mlCV.visibility=View.VISIBLE
//
//                goalTV.text= (drinkedWater?.toInt()!!*100).toString()
//                percentageTV.text=(drinkedWater!! / totalWater!!).toInt().toString()+" %"
//                remainingTV.text=((totalWater!! - drinkedWater!!)).toInt().toString()
//                progressBar.visibility=View.GONE
//            }
//        }
//    }

    private fun addWaterAmount(amount: Int) {
        if (totalWater!! > drinkedWater!! + amount) {
            drinkedWater += amount
            percentage = drinkedWater / totalWater.toDouble()
            goalViewModel.save(totalWater, drinkedWater, percentage)
            totalWater?.let {
                drinkedWater?.let { it1 ->
                    viewModel.onWaterAmountCreated(
                        amount, it,
                        it1
                    )
                }
            }
        } else {
            val errorMessage = "Your daily water consumption is more than the set limit"
            showErrorDialog(errorMessage)
        }
    }

    private suspend fun getFromDataRestore(): DataRestore? {
        val repository = WaterAmountRepository(requireContext())
        val data = repository.get()
        if (data?.totalWater != null) {

            return data
        } else {
            return null
        }
    }

    private fun showWaterAmountLayout(waterAmount: WaterAmountModel?) {
        showAddWaterAmount()
        showConstantWaterAmountButtons()
        binding.goalTV.text = (drinkedWater!!).toString() + " ml"
        if ((percentage * 100).toString().length >= 4)
            binding.percentageTV.text = (percentage * 100).toString().substring(0, 4) + " %"
        else
            binding.percentageTV.text = (percentage * 100).toString() + " %"
        binding.remainingTV.text =
            ((totalWater!! - drinkedWater!!)).toString() + " ml Remaining"


    }

    private fun showErrorDialog(errorMessage: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.setOnShowListener {
            val backgroundDrawable =
                resources.getDrawable(R.drawable.error_message_dialog_background)
            alertDialog.window?.setBackgroundDrawable(backgroundDrawable)
        }

        alertDialog.show()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect { state ->
                    when (state) {
                        TrackingState.Loading -> {
                        }

                        is TrackingState.WaterAmountList -> showWaterAmountLayout(state.tasks[0])
                    }

                }
            }


        }


    }


    private fun showConstantWaterAmountButtons() {
        binding.amount100mlCV.visibility = View.VISIBLE
        binding.amount150mlCV.visibility = View.VISIBLE
        binding.amount200mlCV.visibility = View.VISIBLE
        binding.amount250mlCV.visibility = View.VISIBLE
    }

    private fun showAddWaterAmount() {
        binding.floatingActionButton.visibility = View.VISIBLE
    }

    private fun hideAddWaterAmount() {
        binding.floatingActionButton.visibility = View.GONE
    }

    private fun hideConstantWaterAmountButtons() {
        binding.amount100mlCV.visibility = View.GONE
        binding.amount150mlCV.visibility = View.GONE
        binding.amount200mlCV.visibility = View.GONE
        binding.amount250mlCV.visibility = View.GONE
    }


}