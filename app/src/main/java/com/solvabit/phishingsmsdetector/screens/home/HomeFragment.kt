package com.solvabit.phishingsmsdetector.screens.home

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.solvabit.phishingsmsdetector.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val contentResolver = requireActivity().contentResolver
        val homeViewModelFactory = HomeViewModelFactory(contentResolver)
        viewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = HomeAdapter(HomeAdapterListener {
            Toast.makeText(context, "Clicked on - ${it.address}", Toast.LENGTH_SHORT).show()
        })

        binding.recyclerViewSender.adapter = adapter

        displaySmsLog()


        return binding.root
    }



    private fun displaySmsLog() {
        val allMessages: Uri = Uri.parse("content://sms/")
        //Cursor cursor = managedQuery(allMessages, null, null, null, null); Both are same
        val cursor = activity?.contentResolver?.query(allMessages, null, null, null, null) ?: return

        while (cursor.moveToNext()) {
            for (i in 0 until cursor.getColumnCount()) {
                Log.d(cursor.getColumnName(i).toString() + "", cursor.getString(i).toString() + "")
            }
            Log.d(
                "One row finished",
                "**************************************************"
            )
        }
    }

}