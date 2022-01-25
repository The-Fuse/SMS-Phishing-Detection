package com.solvabit.phishingsmsdetector.screens.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.solvabit.phishingsmsdetector.R
import com.solvabit.phishingsmsdetector.databinding.FragmentMessageDetailsBinding

class MessageDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMessageDetailsBinding
    private lateinit var viewModel: MessageDetailsViewModel
    val args: MessageDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageDetailsBinding.inflate(layoutInflater)
        val messageDetailsViewModelFactory = MessageDetailsViewModelFactory(args.message)
        viewModel = ViewModelProvider(this, messageDetailsViewModelFactory)[MessageDetailsViewModel::class.java]
        binding.message = args.message
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}