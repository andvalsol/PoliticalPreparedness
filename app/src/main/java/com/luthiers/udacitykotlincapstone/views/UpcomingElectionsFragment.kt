package com.luthiers.udacitykotlincapstone.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.luthiers.udacitykotlincapstone.data.models.SingleElection

import com.luthiers.udacitykotlincapstone.databinding.FragmentUpcomingElectionsBinding
import com.luthiers.udacitykotlincapstone.viewModels.MainViewModel
import com.luthiers.udacitykotlincapstone.viewModels.UpcomingElectionsViewModel
import com.luthiers.udacitykotlincapstone.views.adapters.IUpcomingElectionsAdapter
import com.luthiers.udacitykotlincapstone.views.adapters.ElectionsAdapter


class UpcomingElectionsFragment : Fragment(), IUpcomingElectionsAdapter {

    private val viewModel by viewModels<UpcomingElectionsViewModel>()

    private lateinit var upcomingElectionsAdapter: ElectionsAdapter
    private lateinit var savedElectionsAdapter: ElectionsAdapter

    private lateinit var binding: FragmentUpcomingElectionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpcomingElectionsBinding.inflate(inflater, container, false)

        upcomingElectionsAdapter = ElectionsAdapter(this)
        savedElectionsAdapter = ElectionsAdapter(this)

        binding.rvUpcomingElections.apply {
            // All the items have the same size
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = upcomingElectionsAdapter
        }

        binding.rvSavedElections.apply {
            setHasFixedSize(true)
            adapter = savedElectionsAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.savedElections.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                savedElectionsAdapter.submitList(it)
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.pbElections.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })

        viewModel.elections.observe(viewLifecycleOwner, Observer { electionsList ->
            upcomingElectionsAdapter.submitList(electionsList)
        })
    }

    override fun onSingleElectionClick(view: View, singleElection: SingleElection) {
        viewModel.openSingleElection(view, singleElection)
    }
}
