package com.luthiers.udacitykotlincapstone.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.luthiers.udacitykotlincapstone.R
import com.luthiers.udacitykotlincapstone.data.models.SingleElection
import com.luthiers.udacitykotlincapstone.databinding.FragmentSingleElectionBinding
import com.luthiers.udacitykotlincapstone.viewModels.SingleElectionViewModel

class SingleElectionFragment : Fragment() {

    private val _viewModel by viewModels<SingleElectionViewModel>()

    private lateinit var election: SingleElection

    private lateinit var _binding: FragmentSingleElectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_election, container, false)

        // Get the single election object
        arguments?.let {
            // Get the single election from the arguments
            election = SingleElectionFragmentArgs.fromBundle(it).singleElection
            _viewModel.setElectionID(election.id)
        }

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.election.observe(viewLifecycleOwner, Observer{ election ->
            with(_binding) {
                viewModel = _viewModel
                singleElection = election
            }
        })
    }
}
