package com.solvabit.phishingsmsdetector.screens.messages

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.solvabit.phishingsmsdetector.databinding.FragmentMessagesBinding
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.solvabit.phishingsmsdetector.MainActivity
import com.solvabit.phishingsmsdetector.R
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

        (activity as AppCompatActivity).supportActionBar?.title = args.messages[0].address
        setHasOptionsMenu(true)

        val messagesViewModelFactory = MessagesViewModelFactory(args.messages.toList())
        viewModel = ViewModelProvider(this, messagesViewModelFactory)[MessagesViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = AllMessagesAdapter(MessagesListener {
            this.findNavController().navigate(
                MessagesFragmentDirections.actionMessagesFragmentToMessageDetailsFragment(it)
            )
        })
        binding.recyclerAllMessages.adapter = adapter
        viewModel.allMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.msg_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.groupId) {
            R.id.report_group -> Toast.makeText(context, "Reporting now", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}