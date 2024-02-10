package com.ava.alivakili.drink_water.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import com.ava.alivakili.drink_water.WaterAmountModel
import com.ava.alivakili.drink_water.databinding.HistoryLayoutBinding
import com.ava.alivakili.drink_water.repository.database.DatabaseInstance
import com.ava.alivakili.drink_water.repository.datastore.WaterAmountRepository

class HistoryFragment : Fragment() {
    private var _binding: HistoryLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel:HistoryViewModel by viewModels {  HistoryViewModel.factory(DatabaseInstance.instance(requireContext()).taskDao())}
    private lateinit var adapter:HistoryRecyclerViewAdapter
    protected lateinit var repository: WaterAmountRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureRecyclerView()
        observeViewModel()
        repository = WaterAmountRepository(requireContext())
        binding.clear.setOnClickListener {
            clearList()
        }

    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.state.collect{state->
                    when(state){
                        HistoryState.Loading -> {
                            showProgressBar()
                        }
                        is HistoryState.WaterAmountList ->showAmountList(state.amounts)
                    }
                }
            }
        }
    }

    private fun clearList(){
        lifecycleScope.launch {
            val dao=DatabaseInstance.instance(requireContext()).taskDao()
            dao.deleteAll()

            repository.deleteData()
        }

        binding.recyclerView.visibility=View.GONE
        binding.noHistory.visibility=View.VISIBLE
        binding.clear.visibility=View.GONE

    }

    private fun showAmountList(amounts: List<WaterAmountModel>) {
        binding.progressBar.visibility=View.GONE
        if (amounts.isEmpty()){
            Toast.makeText(requireContext(),"There is no history!",Toast.LENGTH_SHORT).show()
        }else{
            binding.recyclerView.visibility= View.VISIBLE
            binding.clear.visibility=View.VISIBLE
            binding.noHistory.visibility=View.GONE
        }
        adapter.differ.submitList(amounts.reversed())

    }

    private fun showProgressBar(){

        binding.progressBar.visibility=View.VISIBLE
    }

    private fun configureRecyclerView(){
        adapter=HistoryRecyclerViewAdapter ()
        binding.recyclerView.apply {
            adapter=this@HistoryFragment.adapter
            layoutManager= LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
    }




}