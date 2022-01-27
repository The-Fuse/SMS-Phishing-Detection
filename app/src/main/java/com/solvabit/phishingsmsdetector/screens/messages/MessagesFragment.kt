package com.solvabit.phishingsmsdetector.screens.messages

import android.os.Bundle
import android.util.Log
import android.telephony.SmsManager
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.solvabit.phishingsmsdetector.databinding.FragmentMessagesBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.solvabit.phishingsmsdetector.R

private const val TAG = "MessagesFragment"

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

        val messagesViewModelFactory = MessagesViewModelFactory(args.messages.toList(), requireContext())
        viewModel = ViewModelProvider(this, messagesViewModelFactory)[MessagesViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.isPhished.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it) {
                    setHasOptionsMenu(false)
                }
                else {
                    setHasOptionsMenu(true)
                }
            }
        })

        val adapter = AllMessagesAdapter(MessagesListener {
            this.findNavController().navigate(
                MessagesFragmentDirections.actionMessagesFragmentToMessageDetailsFragment(it)
            )
        })
        binding.recyclerAllMessages.adapter = adapter
        viewModel.allPhishedMessagesList.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getPhishedList()
                Log.i(TAG, "onCreateView: ${viewModel.allMessages.value}")
                adapter.submitList(viewModel.allMessages.value)
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
            R.id.report_group -> sendSMS()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendSMS() {
        val myNumber: String = "1909"
        val myMsg: String = "This message is sent by this no and message is this, please check it!"
        if (myNumber == "" || myMsg == "") {
            Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (TextUtils.isDigitsOnly(myNumber)) {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(myNumber, null, myMsg, null, null)
                Toast.makeText(context, "Message Sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please enter the correct number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}