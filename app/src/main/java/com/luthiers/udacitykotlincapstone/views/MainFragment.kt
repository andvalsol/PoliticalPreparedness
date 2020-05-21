package com.luthiers.udacitykotlincapstone.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import com.luthiers.udacitykotlincapstone.databinding.FragmentMainBinding
import com.luthiers.udacitykotlincapstone.viewModels.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater , container, false)

        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }
}
