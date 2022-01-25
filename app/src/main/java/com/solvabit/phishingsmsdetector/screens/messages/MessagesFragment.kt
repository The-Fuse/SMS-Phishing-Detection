package com.solvabit.phishingsmsdetector.screens.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.solvabit.phishingsmsdetector.databinding.FragmentMessagesBinding
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer
import java.util.*


class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private lateinit var viewModel: MessagesViewModel
    private val args: MessagesFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(layoutInflater)
        val messagesViewModelFactory = MessagesViewModelFactory(args.messages.toList())
        viewModel = ViewModelProvider(this, messagesViewModelFactory)[MessagesViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        
        val adapter = AllMessagesAdapter(MessagesListener {
            Toast.makeText(context, "${getDateTime(it.date.toString())}", Toast.LENGTH_SHORT).show()
        })
        binding.recyclerAllMessages.adapter = adapter
        viewModel.allMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }


}