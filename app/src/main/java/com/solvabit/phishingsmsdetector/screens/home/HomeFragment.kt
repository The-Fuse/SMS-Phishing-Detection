package com.solvabit.phishingsmsdetector.screens.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.solvabit.phishingsmsdetector.MainActivity
import com.solvabit.phishingsmsdetector.R
import com.solvabit.phishingsmsdetector.databinding.FragmentHomeBinding

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)


        val contentResolver = requireActivity().contentResolver
        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null, null, null, null
        )
        val homeViewModelFactory = HomeViewModelFactory(requireContext(), cursor!!)
        viewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = HomeAdapter(HomeAdapterListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMessagesFragment(viewModel.getList(it.address)))
        })

        binding.recyclerViewSender.adapter = adapter

        setHasOptionsMenu(true)

        createChannel(
            getString(R.string.phishing_notification_channel_id),
            getString(R.string.phishing_notification_channel_name)
        )

        viewModel.phishedList.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.refreshListPhishedData()
                adapter.submitList(viewModel.msgList.value)
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Message"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        if(searchItem!=null) {
            val searchView = MenuItemCompat.getActionView(searchItem) as SearchView

            val searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
            searchPlate.hint = "Search"
//            val searchPlateView: View =
//                searchView.findViewById(androidx.appcompat.R.id.search_plate)

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(context, query, Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "This gives Phishing reports"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}